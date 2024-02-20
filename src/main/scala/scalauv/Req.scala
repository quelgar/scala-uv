package scalauv

import scalanative.unsafe.*
import scala.scalanative.libc.stdlib

/** Representation of a `uv_req_t*` native type.
  */
opaque type Req = Ptr[Byte]

/** Constructors for the [[scalauv.Req Req]] opaque type that represents a
  * pointer to the `uv_req_t` native C type. While this object can be used to
  * allocate requests by providing a request type, it is more common to use the
  * equivalent allocation methods for the exact request type you want, for
  * example `ConnectReq.malloc()`.
  */
object Req {

  given Tag[Req] = Tag.Ptr(Tag.Byte)

  /** Cast a native pointer to a request structure to the `Req` type.
    */
  inline def unsafeFromPtr(ptr: Ptr[Byte]): Req = ptr

  /** Allocate the specified type of request on the stack.
    *
    * *Note:* this is generally only safe to use for synchronous calls.
    *
    * @param requestType
    *   The type of request to allocate.
    * @return
    *   The new request.
    */
  inline def stackAllocate(requestType: RequestType): Req =
    stackalloc[Byte](LibUv.uv_req_size(requestType))

  /** Zone allocate the specified type of request.
    */
  inline def zoneAllocate(requestType: RequestType)(using Zone): Req =
    alloc[Byte](LibUv.uv_req_size(requestType))

  /** Allocate the specified type of request.
    */
  inline def malloc(requestType: RequestType): Req =
    stdlib.malloc(LibUv.uv_req_size(requestType)).asInstanceOf[Req]

  extension (r: Req) {

    inline def data: Ptr[Byte] = LibUv.uv_req_get_data(r)

    inline def data_=(data: Ptr[Byte]): Unit = LibUv.uv_req_set_data(r, data)

    inline def reqType: RequestType = LibUv.uv_req_get_type(r)

    inline def cancel(): ErrorCode = LibUv.uv_cancel(r)

    /** Casts this request to a native pointer.
      */
    inline def toPtr: Ptr[Byte] = r

    /** Frees this request structure.
      */
    inline def free(): Unit = stdlib.free(r)

  }

}

opaque type RequestType = CInt

/** Constants for all the request types supported by libuv.
  */
object RequestType {

  extension (rt: RequestType) {

    inline def size: CSize = LibUv.uv_req_size(rt)

  }

  val UNKNOWN_REQ: RequestType = 0
  val REQ: RequestType = 1
  val CONNECT: RequestType = 2
  val WRITE: RequestType = 3
  val SHUTDOWN: RequestType = 4
  val UDP_SEND: RequestType = 5
  val FS: RequestType = 6
  val WORK: RequestType = 7
  val GETADDRINFO: RequestType = 8
  val GETNAMEINFO: RequestType = 9
  val REQ_TYPE_MAX: RequestType = 10
}

opaque type ConnectReq <: Req = Ptr[Byte]

object ConnectReq {

  given Tag[ConnectReq] = Tag.Ptr(Tag.Byte)

  inline def stackAllocate(): ConnectReq =
    Req.stackAllocate(RequestType.CONNECT)

  inline def zoneAllocate()(using Zone): ConnectReq =
    Req.zoneAllocate(RequestType.CONNECT)

  inline def malloc(): ConnectReq =
    Req.malloc(RequestType.CONNECT)

  extension (r: ConnectReq) {

    inline def connectReqStreamHandle: StreamHandle =
      helpers.scala_uv_connect_stream_handle(r)

  }

}

opaque type ShutdownReq <: Req = Ptr[Byte]

object ShutdownReq {

  given Tag[ShutdownReq] = Tag.Ptr(Tag.Byte)

  inline def stackAllocate(): ShutdownReq =
    Req.stackAllocate(RequestType.SHUTDOWN)

  inline def zoneAllocate()(using Zone): ShutdownReq =
    Req.zoneAllocate(RequestType.SHUTDOWN)

  inline def malloc(): ShutdownReq =
    Req.malloc(RequestType.SHUTDOWN)

  extension (r: ShutdownReq) {

    inline def shutdownReqStreamHandle: StreamHandle =
      helpers.scala_uv_shutdown_stream_handle(r)

  }

}

opaque type WriteReq <: Req = Ptr[Byte]

object WriteReq {

  given Tag[WriteReq] = Tag.Ptr(Tag.Byte)

  inline def stackAllocate(): WriteReq =
    Req.stackAllocate(RequestType.WRITE)

  inline def zoneAllocate()(using Zone): WriteReq =
    Req.zoneAllocate(RequestType.WRITE)

  inline def malloc(): WriteReq =
    Req.malloc(RequestType.WRITE)

  extension (r: WriteReq) {

    inline def writeReqStreamHandle: StreamHandle =
      helpers.scala_uv_write_stream_handle(r)

    inline def writeReqSendStreamHandle: StreamHandle =
      helpers.scala_uv_send_stream_handle(r)

  }

}

opaque type UdpSendReq <: Req = Ptr[Byte]

object UdpSendReq {

  given Tag[UdpSendReq] = Tag.Ptr(Tag.Byte)

  inline def stackAllocate(): UdpSendReq =
    Req.stackAllocate(RequestType.UDP_SEND)

  inline def zoneAllocate()(using Zone): UdpSendReq =
    Req.zoneAllocate(RequestType.UDP_SEND)

  inline def malloc(): UdpSendReq =
    Req.malloc(RequestType.UDP_SEND)

}

opaque type FileReq <: Req = Ptr[Byte]

object FileReq {

  given Tag[FileReq] = Tag.Ptr(Tag.Byte)

  extension (req: FileReq) {

    inline def loop: Loop = helpers.scala_uv_fs_req_get_loop(req)

    inline def fsType: FsType = LibUv.uv_fs_get_type(req)

    inline def result: CSSize = LibUv.uv_fs_get_result(req)

    inline def systemError: CInt = LibUv.uv_fs_get_system_error(req)

    inline def ptr: Ptr[Byte] = LibUv.uv_fs_get_ptr(req)

    inline def path: CString = LibUv.uv_fs_get_path(req)

    inline def statBuf: Stat = LibUv.uv_fs_get_statbuf(req)

    inline def cleanup(): Unit = LibUv.uv_fs_req_cleanup(req)

  }

  inline def stackAllocate(): FileReq =
    Req.stackAllocate(RequestType.FS)

  inline def zoneAllocate()(using Zone): FileReq =
    Req.zoneAllocate(RequestType.FS)

  inline def malloc(): FileReq =
    Req.malloc(RequestType.FS)

  /** Allocate a new file request on the stack, provide it to the specified
    * function, and clean it up after the function returns. Note this is only
    * safe to use for **blocking** FS operations.
    *
    * @param f
    *   A function that performs **blocking** FS operations.
    */
  inline def use[A](f: FileReq => A): A = {
    val req = stackAllocate()
    try f(req)
    finally LibUv.uv_fs_req_cleanup(req)
  }

}

opaque type WorkReq <: Req = Ptr[Byte]

object WorkReq {

  given Tag[WorkReq] = Tag.Ptr(Tag.Byte)

  inline def stackAllocate(): WorkReq =
    Req.stackAllocate(RequestType.WORK)

  inline def zoneAllocate()(using Zone): WorkReq =
    Req.zoneAllocate(RequestType.WORK)

  inline def malloc(): WorkReq =
    Req.malloc(RequestType.WORK)

}

opaque type GetAddrInfoReq <: Req = Ptr[Byte]

object GetAddrInfoReq {

  given Tag[GetAddrInfoReq] = Tag.Ptr(Tag.Byte)

  inline def stackAllocate(): GetAddrInfoReq =
    Req.stackAllocate(RequestType.GETADDRINFO)

  inline def zoneAllocate()(using Zone): GetAddrInfoReq =
    Req.zoneAllocate(RequestType.GETADDRINFO)

  inline def malloc(): GetAddrInfoReq =
    Req.malloc(RequestType.GETADDRINFO)

}

opaque type GetNameInfoReq <: Req = Ptr[Byte]

object GetNameInfoReq {

  given Tag[GetNameInfoReq] = Tag.Ptr(Tag.Byte)

  inline def stackAllocate(): GetNameInfoReq =
    Req.stackAllocate(RequestType.GETNAMEINFO)

  inline def zoneAllocate()(using Zone): GetNameInfoReq =
    Req.zoneAllocate(RequestType.GETNAMEINFO)

  inline def malloc(): GetNameInfoReq =
    Req.malloc(RequestType.GETNAMEINFO)

}
