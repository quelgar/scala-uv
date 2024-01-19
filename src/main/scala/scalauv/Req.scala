package scalauv

import scalanative.unsafe.*
import scala.scalanative.libc.stdlib

opaque type Req = Ptr[Byte]

object Req {

  given Tag[Req] = Tag.Ptr(Tag.Byte)

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

  inline def zoneAllocate(requestType: RequestType)(using Zone): Req =
    alloc[Byte](LibUv.uv_req_size(requestType))

  inline def malloc(requestType: RequestType): Req =
    stdlib.malloc(LibUv.uv_req_size(requestType)).asInstanceOf[Req]

  extension (r: Req) {

    inline def toPtr: Ptr[Byte] = r

    inline def free(): Unit = stdlib.free(r)

  }

}

opaque type RequestType = CInt

object RequestType {
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

opaque type WriteReq <: Req = Ptr[Byte]

object WriteReq {

  given Tag[WriteReq] = Tag.Ptr(Tag.Byte)

  inline def stackAllocate(): WriteReq =
    Req.stackAllocate(RequestType.WRITE)

  inline def zoneAllocate()(using Zone): WriteReq =
    Req.zoneAllocate(RequestType.WRITE)

  inline def malloc(): WriteReq =
    Req.malloc(RequestType.WRITE)

}

opaque type UdpSendReq <: Req = Ptr[Byte]

opaque type FileReq <: Req = Ptr[Byte]

object FileReq {

  inline def stackAllocate(): FileReq =
    Req.stackAllocate(RequestType.FS)

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
