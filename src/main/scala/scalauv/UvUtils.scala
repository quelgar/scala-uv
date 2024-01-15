package scalauv

import LibUv.*
import scala.scalanative.unsafe.*
import scala.scalanative.libc.*
import scala.scalanative.unsigned.*
import java.io.IOException
import scala.util.boundary
import java.nio.charset.StandardCharsets
import java.nio.charset.Charset
import scala.util.control.NonFatal
import scala.collection.mutable.Stack

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

  inline def errorName(errorCode: CInt): String = {
    val cString = stackalloc[Byte](ErrorCodeNameMax)
    LibUv.uv_err_name_r(errorCode, cString, ErrorCodeNameMax)
    fromCString(cString)
  }

  private val ErrorCodeMessageMex: CSize = 200.toUInt

  inline def errorMessage(errorCode: ErrorCode): String = withZone {
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

  inline def withMutex[A](mutex: Mutex)(f: => A): A = {
    uv_mutex_lock(mutex)
    try f
    finally uv_mutex_unlock(mutex)
  }

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

  }

  def attemptCatch[A](f: Cleanup ?=> A)(handleError: Throwable => A): A = {
    given cleanup: Cleanup = Cleanup()
    try f
    catch {
      case NonFatal(t) =>
        cleanup.completed(Some(t))
        handleError(t)
    } finally cleanup.completed(None)
  }

  def attempt[A](f: Cleanup ?=> A): A = {
    given cleanup: Cleanup = Cleanup()
    try f
    catch {
      case NonFatal(t) =>
        cleanup.completed(Some(t))
        throw t
    } finally cleanup.completed(None)
  }

  inline def onFailWith(f: Throwable => Unit)(using cleanup: Cleanup): Unit =
    cleanup.addOnErrorAction(f)

  inline def onFail(f: => Unit)(using Cleanup): Unit =
    onFailWith(_ => f)

  inline def onComplete(f: => Unit)(using cleanup: Cleanup): Unit =
    cleanup.addOnCompleteAction(_ => f)

}

extension (uvResult: CInt) {

  inline def mapError[A](handleError: CInt => A): Either[A, CInt] =
    UvUtils.mapError(uvResult)(handleError)

  inline def mapErrorMessage: Either[String, CInt] =
    mapError(UvUtils.errorMessage(_))

  inline def checkErrorThrow(f: String => Exception): CInt =
    UvUtils.checkErrorThrow(uvResult)(f)

  inline def checkErrorThrowIO(): CInt =
    UvUtils.checkErrorThrowIO(uvResult)

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

opaque type SocketAddress = Ptr[Byte]

opaque type SocketAddressIp4 <: SocketAddress = Ptr[Byte]

object SocketAddressIp4 {

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

opaque type SocketAddressIp6 = Ptr[Byte]

extension (r: LibUv.ConnectReq) {

  inline def connectReqStreamHandle: LibUv.StreamHandle =
    helpers.uv_scala_connect_stream_handle(r)

}

opaque type AddrInfo = Ptr[Byte]
