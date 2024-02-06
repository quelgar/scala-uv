package scalauv

import LibUv.*
import scala.scalanative.unsafe.*
import scala.scalanative.libc.*
import scala.scalanative.unsigned.*
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.charset.Charset
import scala.util.control.NonFatal
import scala.collection.mutable.Stack

/** More convenient Scala 3 version of a `Zone` block. Instead of `Zone {
  * implicit zone => f }` you can use `withZone { f }`.
  *
  * @param f
  *   The function that requires a given `Zone`.
  */
inline def withZone[A](f: Zone ?=> A): A = Zone(z => f(using z))

/** Converts a Scala string to a native C string allocated using `malloc`.
  *
  * @param s
  *   The Scala string.
  * @param charset
  *   The character set to use to encode the C string, defaults to UTF8.
  * @return
  *   A pointer to the C string allocated with `malloc`.
  */
def mallocCString(
    s: String,
    charset: Charset = StandardCharsets.UTF_8
): CString = {
  if s.isEmpty() then c""
  else {
    val bytes = s.getBytes(charset)
    val size = bytes.length.toUInt
    val cString = stdlib.malloc(size + 1.toUInt)
    string.memcpy(cString, bytes.at(0), size)
    !(cString + size) = 0.toByte
    cString
  }
}

/** Useful utilities to make it easier to use libuv from Scala.
  */
object UvUtils {

  private val ErrorCodeNameMax: CSize = 80.toUInt

  /** Gets the name of a libuv error code as a Scala string.
    */
  inline def errorName(errorCode: ErrorCode): String = {
    val cString = stackalloc[Byte](ErrorCodeNameMax)
    LibUv.uv_err_name_r(errorCode, cString, ErrorCodeNameMax)
    fromCString(cString)
  }

  private val ErrorCodeMessageMex: CSize = 200.toUInt

  /** Gets the error message for a libuv error code as a Scala string.
    */
  inline def errorMessage(errorCode: ErrorCode): String = withZone {
    val cString = alloc[Byte](ErrorCodeMessageMex)
    LibUv.uv_strerror_r(errorCode, cString, ErrorCodeMessageMex)
    fromCString(cString)
  }

  /** Gets the name and message for a libuv error code in a single Scala string.
    */
  def errorNameAndMessage(errorCode: ErrorCode): String = withZone {
    val cString = alloc[Byte](ErrorCodeMessageMex)
    LibUv.uv_err_name_r(errorCode, cString, ErrorCodeMessageMex)
    val name = fromCString(cString)
    LibUv.uv_strerror_r(errorCode, cString, ErrorCodeMessageMex)
    val message = fromCString(cString)
    s"UV error $name: $message"
  }

  inline def mapError[A](result: ErrorCode)(
      handleError: CInt => A
  ): Either[A, CInt] =
    if result < 0 then Left(handleError(result)) else Right(result)

  inline def checkErrorThrow(result: ErrorCode)(f: String => Exception): CInt =
    if result < 0 then throw f(errorNameAndMessage(result)) else result

  inline def checkErrorThrowIO(result: ErrorCode): CInt =
    checkErrorThrow(result)(new IOException(_))

  def checkErrorEofThrow(
      result: ErrorCode
  )(f: String => Exception): Option[CInt] =
    result match {
      case code if code == ErrorCodes.EOF => None
      case error if error < 0             => throw f(errorNameAndMessage(error))
      case success                        => Some(success)
    }

  /** Accumulates cleanup operations in an "attempt" block.
    *
    * @see
    *   [[scalauv.UvUtils.attempt]]
    * @see
    *   [[scalauv.UvUtils.attemptCatch]]
    */
  final class Cleanup private[UvUtils] () {

    private val onCompleteActions = Stack.empty[Option[Throwable] => Unit]

    private[UvUtils] inline def addOnErrorAction(f: Throwable => Unit): Unit =
      onCompleteActions.push(_.foreach(f))

    private[UvUtils] inline def addOnCompleteAction(
        f: Option[Throwable] => Unit
    ): Unit =
      onCompleteActions.push(f)

    private[UvUtils] inline def completed(
        maybeFailure: Option[Throwable]
    ): Unit =
      onCompleteActions.popAll().foreach(_(maybeFailure))

    private[UvUtils] inline def dropLast(): Unit = {
      onCompleteActions.pop()
      ()
    }

  }

  /** Attempts to run a block of code, allowing cleanup operations to be
    * registered. The passed function receives `Cleanup` as context, allowing it
    * to register cleanup via `onFail` and `onComplete`. If `f` throws an
    * exception, any cleanup operations registered so far will be run in reverse
    * order. This function requires catching all thrown exceptions, and is
    * designed for use where exceptions cannot be thrown any further up the call
    * stack, such as native C function pointers.
    *
    * @see
    *   [[scalauv.UvUtils.onFail]]
    * @see
    *   [[scalauv.UvUtils.onComplete]]
    *
    * @param f
    *   The operation to attempt.
    * @param handleError
    *   A function to handle any exceptions that are thrown.
    * @return
    */
  def attemptCatch[A](f: Cleanup ?=> A)(handleError: Throwable => A): A = {
    given cleanup: Cleanup = Cleanup()
    try f
    catch {
      case NonFatal(t) =>
        cleanup.completed(Some(t))
        handleError(t)
    } finally cleanup.completed(None)
  }

  /** Attempts to run a block of code, allowing cleanup operations to be
    * registered. The passed function receives `Cleanup` as context, allowing it
    * to register cleanup via `onFail` and `onComplete`. If `f` throws an
    * exception, any cleanup operations registered so far will be run in reverse
    * order. After cleanup is run, the exception is propagated up the call
    * stack.
    *
    * @see
    *   [[scalauv.UvUtils.onFail]]
    * @see
    *   [[scalauv.UvUtils.onComplete]]
    *
    * @param f
    *   The operation to attempt.
    */
  def attempt[A](f: Cleanup ?=> A): A = {
    given cleanup: Cleanup = Cleanup()
    try f
    catch {
      case NonFatal(t) =>
        cleanup.completed(Some(t))
        throw t
    } finally cleanup.completed(None)
  }

  /** Register a cleanup operation to be run if the code block fails.
    *
    * @param f
    *   A cleanup operation that receives the failure exception.
    */
  inline def onFailWith(f: Throwable => Unit)(using cleanup: Cleanup): Unit =
    cleanup.addOnErrorAction(f)

  /** Register a cleanup operation to be run if the code block fails.
    *
    * @param f
    *   A cleanup operation.
    */
  inline def onFail(f: => Unit)(using Cleanup): Unit =
    onFailWith(_ => f)

  /** Register a cleanup operation to be run when the code block completes,
    * whether successful or not.
    *
    * @param f
    *   A cleanup operation.
    */
  inline def onComplete(f: => Unit)(using cleanup: Cleanup): Unit =
    cleanup.addOnCompleteAction(_ => f)

  inline def dropLast()(using cleanup: Cleanup): Unit =
    cleanup.dropLast()
}

extension (uvResult: CInt) {

  inline def mapError[A](handleError: CInt => A): Either[A, CInt] =
    UvUtils.mapError(uvResult)(handleError)

  inline def mapErrorMessage: Either[String, CInt] =
    mapError(UvUtils.errorMessage(_))

  inline def checkErrorThrow(f: String => Exception): CInt =
    UvUtils.checkErrorThrow(uvResult)(f)

  /** Checks the result of a libuv operation, and throws an `IOException` if the
    * result indicates failure.
    *
    * @throws java.io.IOException
    *   if the result is < 0
    * @return
    *   the result if it is >= 0
    */
  inline def checkErrorThrowIO(): CInt =
    UvUtils.checkErrorThrowIO(uvResult)

  /** Performs an action only if the result indicates failure.
    *
    * @param f
    *   The operation to perform on failure.
    * @return
    *   The result.
    */
  inline def onFail(f: => Unit): CInt = {
    if uvResult < 0 then f
    uvResult
  }

  inline def onFailMessage(f: String => Unit): CInt = {
    if uvResult < 0 then f(UvUtils.errorMessage(uvResult))
    uvResult
  }

  inline def successAs[A](v: => A): Either[CInt, A] =
    if uvResult < 0 then Left(uvResult) else Right(v)

}

type Ip4Address = Int

object Ip4Address {

  inline def apply(a: Int, b: Int, c: Int, d: Int): Ip4Address = {
    val aShifted = a << 24
    val bShifted = b << 16
    val cShifted = c << 8
    val dShifted = d
    aShifted | bShifted | cShifted | dShifted
  }

  val Unspecified: Ip4Address = apply(0, 0, 0, 0)

  val loopback: Ip4Address = apply(127, 0, 0, 1)

}

type Port = Int

opaque type SocketAddress = Ptr[Byte]

opaque type SocketAddressIp4 <: SocketAddress = Ptr[Byte]

object SocketAddressIp4 {

  val size: CSize = helpers.scala_uv_sizeof_sockaddr_in()

  inline def apply(address: Ip4Address, port: Port): SocketAddressIp4 = {
    val sockaddr = stackalloc[Byte](size).asInstanceOf[SocketAddressIp4]
    helpers.scala_uv_init_sockaddr_in(address, port, sockaddr)
    sockaddr
  }

  inline def fromBytes(a: Int, b: Int, c: Int, d: Int)(
      port: Port
  ): SocketAddressIp4 =
    apply(Ip4Address(a, b, c, d), port)

  inline def fromString(
      ip: String,
      port: Port
  ): Either[ErrorCode, SocketAddressIp4] = withZone {
    val cString = toCString(ip)
    val sockaddr = stackalloc[Byte](size).asInstanceOf[SocketAddressIp4]
    LibUv
      .uv_ip4_addr(cString, port, sockaddr)
      .successAs(sockaddr)
  }

  inline def unspecifiedAddress(port: Port): SocketAddressIp4 =
    apply(Ip4Address.Unspecified, port)

  inline def loopbackAddress(port: Port): SocketAddressIp4 =
    apply(Ip4Address.loopback, port)

}

opaque type SocketAddressIp6 <: SocketAddress = Ptr[Byte]

opaque type AddrInfo = Ptr[Byte]
