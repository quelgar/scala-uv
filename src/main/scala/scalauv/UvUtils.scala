package scalauv

import LibUv.*
import scala.scalanative.unsafe.*
import scala.scalanative.libc.*
import scala.scalanative.unsigned.*
import java.io.IOException
import scala.util.boundary
import java.nio.charset.StandardCharsets
import java.nio.charset.Charset

inline def withZone[A](f: Zone ?=> A): A = Zone(z => f(using z))

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

/** Utility to help deal with errors returned by the libuv API. Constructor
  * methods like `attempt` can be used the check the libuv return value,
  * constructing a `Failure` case if it is < 0 and `Success` otherwise. As with
  * `Either`, the `flatMap` does not continue if there is a failure. It is also
  * possible to register callbacks to be run if there is a failure, using
  * methods like `onFail`.
  */
enum Uv[+A] {

  case Success(onFailActions: Vector[Uv.Error => Unit], result: A) extends Uv[A]
  case Failure(error: Uv.Error) extends Uv[Nothing]

  def errorMessage: Option[String] = this match {
    case Success(_, _) =>
      None
    case Failure(error) =>
      Some(error.message)
  }

  def map[B](f: A => B): Uv[B] = this match {
    case Success(onFailActions, result) =>
      Success(onFailActions, f(result))
    case Failure(error) =>
      Failure(error)
  }

  def flatMap[B](f: A => Uv[B]): Uv[B] = this match {
    case Success(onFailActions, result) =>
      f(result) match {
        case Success(onFailActions2, result2) =>
          Success(onFailActions2 ++ onFailActions, result2)
        case Failure(error) =>
          onFailActions.foreach(_(error))
          Failure(error)
      }
    case Failure(error) =>
      Failure(error)
  }

  def foreach(f: A => Unit): Unit = this match {
    case Success(_, result) =>
      f(result)
    case Failure(_) =>
      ()
  }

  def as[B](b: B): Uv[B] = this match {
    case Success(onFailActions, _) =>
      Success(onFailActions, b)
    case Failure(error) =>
      Failure(error)
  }

  def mapErrorMessage(f: String => String): Uv[A] = this match {
    case s @ Success(_, _) =>
      s
    case Failure(error) =>
      Failure(error.copy(message = f(error.message)))
  }

  def onFailError(f: Uv.Error => Unit): Uv[A] = this match {
    case Success(onFailActions, result) =>
      Success(f +: onFailActions, result)
    case Failure(error) =>
      f(error)
      this
  }

  def onFail(f: => Unit): Uv[A] = onFailError(_ => f)

  def toEither: Either[Uv.Error, A] = this match {
    case Success(_, result) =>
      Right(result)
    case Failure(error) =>
      Left(error)
  }

  def eitherMessage: Either[String, A] = this match {
    case Success(_, result) =>
      Right(result)
    case Failure(error) =>
      Left(error.message)
  }

  def fold[B](onFail: Uv.Error => B, onSuccess: A => B): B = this match {
    case Success(_, result) =>
      onSuccess(result)
    case Failure(error) =>
      onFail(error)
  }

  def foreachFailure(f: Uv.Error => Unit): Unit = this match {
    case Success(_, _) =>
      ()
    case Failure(error) =>
      f(error)
  }

  def isSuccess: Boolean = this match {
    case Success(_, _) =>
      true
    case Failure(_) =>
      false
  }

  def isFailure: Boolean = !isSuccess

  def toOption: Option[A] = this match {
    case Success(_, result) =>
      Some(result)
    case Failure(_) =>
      None
  }

}

object Uv {

  final case class Error(errorCode: ErrorCode, message: String) {

    def errorName: String = UvUtils.errorName(errorCode)

  }

  object Error {

    def forCode(errorCode: ErrorCode): Error =
      Error(errorCode, UvUtils.errorMessage(errorCode))

  }

  def succeed[A](a: A): Uv[A] = Success(Vector.empty, a)

  def unit: Uv[Unit] = succeed(())

  def fail(errorCode: ErrorCode): Uv[Nothing] = Failure(
    Error.forCode(errorCode)
  )

  def checkError[A](errorCode: ErrorCode, a: A): Uv[A] =
    if errorCode < 0 then Failure(Error.forCode(errorCode))
    else succeed(a)

  def attempt(errorCode: ErrorCode): Uv[ErrorCode] =
    if errorCode < 0 then fail(errorCode)
    else succeed(errorCode)

  def onFailError(f: Error => Unit): Uv[Unit] = Success(Vector(f), ())

  def onFail(f: => Unit): Uv[Unit] = onFailError(_ => f)

}

object UvUtils {

  /** Allocate the specified type of request on the stack.
    *
    * *Note:* this is generally only safe to use for synchronous calls.
    *
    * @param requestType
    *   The type of request to allocate.
    * @return
    *   The new request.
    */
  inline def stackAllocateRequest(requestType: RequestType): Req =
    stackalloc[Byte](uv_req_size(requestType))

  inline def zoneAllocateRequest(requestType: RequestType)(using Zone): Req =
    alloc[Byte](uv_req_size(requestType))

  inline def mallocRequest(requestType: RequestType): Req =
    stdlib.malloc(uv_req_size(requestType)).asInstanceOf[Req]

  object FsReq {

    /** Allocate a new FS request, provide it to the specified function, and
      * clean it up after the function returns. Note this is only safe to use
      * for **blocking** FS operations.
      *
      * @param f
      *   A function that performs **blocking** FS operations.
      */
    inline def use[A](inline f: Req => A): A = {
      val req = stackAllocateRequest(RequestType.FS)
      try f(req)
      finally uv_fs_req_cleanup(req)
    }

  }

  inline def stackAllocateHandle(handleType: HandleType): Handle =
    stackalloc[Byte](uv_handle_size(handleType))

  inline def zoneAllocateHandle(handleType: HandleType)(using Zone): Handle =
    alloc[Byte](uv_handle_size(handleType))

  inline def mallocHandle(handleType: HandleType): Handle =
    stdlib.malloc(uv_handle_size(handleType)).asInstanceOf[Handle]

  private val ErrorCodeNameMax: CSize = 80.toUInt

  def errorName(errorCode: CInt): String = {
    val cString = stackalloc[Byte](ErrorCodeNameMax)
    LibUv.uv_err_name_r(errorCode, cString, ErrorCodeNameMax)
    fromCString(cString)
  }

  private val ErrorCodeMessageMex: CSize = 200.toUInt

  def errorMessage(errorCode: ErrorCode): String = withZone {
    val cString = alloc[Byte](ErrorCodeMessageMex)
    LibUv.uv_strerror_r(errorCode, cString, ErrorCodeMessageMex)
    fromCString(cString)
  }

  def errorNameAndMessage(errorCode: LibUv.ErrorCode): String = withZone {
    val cString = alloc[Byte](ErrorCodeMessageMex)
    LibUv.uv_err_name_r(errorCode, cString, ErrorCodeMessageMex)
    val name = fromCString(cString)
    LibUv.uv_strerror_r(errorCode, cString, ErrorCodeMessageMex)
    val message = fromCString(cString)
    s"UV error $name: $message"
  }

  def checkError[A](result: ErrorCode)(
      handleError: CInt => A
  ): Either[A, CInt] =
    if result < 0 then Left(handleError(result)) else Right(result)

  def checkErrorThrow(result: ErrorCode)(f: String => Exception): CInt =
    if result < 0 then throw f(errorNameAndMessage(result)) else result

  def checkErrorThrowIO(result: ErrorCode): CInt =
    checkErrorThrow(result)(new IOException(_))

  def checkErrorEofThrow(
      result: ErrorCode
  )(f: String => Exception): Option[CInt] =
    result match {
      case code if code == ErrorCodes.EOF => None
      case error if error < 0             => throw f(errorNameAndMessage(error))
      case success                        => Some(success)
    }

  def withMutex[A](mutex: Mutex)(f: => A): A = {
    uv_mutex_lock(mutex)
    try f
    finally uv_mutex_unlock(mutex)
  }

}

extension (uvResult: CInt) {

  def checkError[A](handleError: CInt => A): Either[A, CInt] =
    UvUtils.checkError(uvResult)(handleError)

  def checkErrorThrow(f: String => Exception): CInt =
    UvUtils.checkErrorThrow(uvResult)(f)

  def checkErrorThrowIO(): CInt =
    UvUtils.checkErrorThrowIO(uvResult)

  def checkErrorMessage: Either[String, CInt] =
    checkError(UvUtils.errorMessage)

  def checkCustomErrorMessage(f: String => String): Either[String, CInt] =
    checkError(code => f(UvUtils.errorMessage(code)))

  def attempt: Uv[ErrorCode] = Uv.attempt(uvResult)

  def ifSuccess[A](a: => A): Uv[A] =
    if uvResult < 0 then Uv.fail(uvResult) else Uv.succeed(a)

  def onFail(f: => Unit): CInt = {
    if uvResult < 0 then f
    uvResult
  }

  def onFailMessage(f: String => Unit): CInt = {
    if uvResult < 0 then f(UvUtils.errorMessage(uvResult))
    uvResult
  }

}

final class IOVector(val nativeBuffers: Buffer, numberOfBuffers: Int) {

  inline def apply(index: Int): Buffer = {
    nativeBuffers + index
  }

  inline def foreachBuffer(f: Buffer => Unit): Unit =
    for index <- 0 until numberOfBuffers do {
      f(apply(index))
    }

  def foreachBufferMax(max: Int)(f: Buffer => Unit): Unit = {
    var remaining = max
    boundary {
      for index <- 0 until numberOfBuffers do {
        val buf = apply(index)
        val length = scala.math.min(buf.length, remaining)
        f(buf)
        remaining -= length
        assert(remaining >= 0)
        if remaining == 0 then boundary.break()
      }
    }
  }

  inline def nativeNumBuffers: CUnsignedInt = numberOfBuffers.toUInt

}

object IOVector {

  inline def stackAllocateForBuffer(
      ptr: Ptr[Byte],
      size: CUnsignedInt
  ): IOVector = {
    val buffer = Buffer.stackAllocate(ptr, size)
    IOVector(buffer, 1)
  }

  inline def stackAllocateForBuffers(
      buffers: Seq[(Ptr[Byte], CUnsignedInt)]
  ): IOVector = {
    val uvBufs =
      stackalloc[Byte](buffers.size.toUInt * Buffer.structureSize)
        .asInstanceOf[Buffer]
    buffers.zipWithIndex.foreach { case ((ptr, size), index) =>
      (uvBufs + index).init(ptr, size)
    }
    IOVector(uvBufs, buffers.size)
  }

  def zoneAllocate(bufferSizes: Int*)(using Zone): IOVector = {
    val uvBufs =
      alloc[Byte](bufferSizes.size.toUInt * Buffer.structureSize)
        .asInstanceOf[Buffer]
    bufferSizes.zipWithIndex.foreach { case (size, index) =>
      val base = alloc[Byte](size)
      (uvBufs + index).init(base, size.toUInt)
    }
    IOVector(uvBufs, bufferSizes.size)
  }

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

opaque type SocketAddressIp4 = Ptr[Byte]

object SocketAddress4 {

  val size: CSize = helpers.uv_scala_sizeof_sockaddr_in()

  inline def apply(address: Ip4Address, port: Port): SocketAddressIp4 = {
    val sockaddr = stackalloc[Byte](size).asInstanceOf[SocketAddressIp4]
    helpers.uv_scala_init_sockaddr_in(address, port, sockaddr)
    sockaddr
  }

  inline def fromBytes(a: Int, b: Int, c: Int, d: Int)(
      port: Port
  ): SocketAddressIp4 =
    apply(Ip4Address(a, b, c, d), port)

  inline def fromString(
      ip: String,
      port: Port
  ): Uv[SocketAddressIp4] = withZone {
    val cString = toCString(ip)
    val sockaddr = stackalloc[Byte](size).asInstanceOf[SocketAddressIp4]
    LibUv
      .uv_ip4_addr(cString, port, sockaddr)
      .ifSuccess(sockaddr)
  }

  inline def unspecifiedAddress(port: Port): SocketAddressIp4 =
    apply(Ip4Address.Unspecified, port)

  inline def loopbackAddress(port: Port): SocketAddressIp4 =
    apply(Ip4Address.loopback, port)

}

opaque type SocketAddressIp6 = Ptr[Byte]

extension (r: LibUv.ConnectReq) {

  inline def connectReqStreamHandle: LibUv.StreamHandle =
    helpers.uv_scala_connect_stream_handle(r)

}
