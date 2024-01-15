package scalauv

import scala.scalanative.unsafe.*
import scala.scalanative.libc.stdlib

opaque type Handle = Ptr[Byte]

object Handle {
  given Tag[Handle] = Tag.Ptr(Tag.Byte)

  extension (h: Handle) {
    inline def toPtr: Ptr[Byte] = h

    inline def free(): Unit = stdlib.free(h)
  }

  inline def unsafeFromPtr(ptr: Ptr[Byte]): Handle = ptr

  inline def zoneAllocate(handleType: HandleType)(using Zone): Handle =
    alloc[Byte](LibUv.uv_handle_size(handleType)).asInstanceOf[Handle]

  inline def stackAllocate(handleType: HandleType): Handle =
    Handle.unsafeFromPtr(stackalloc[Byte](LibUv.uv_handle_size(handleType)))

  inline def malloc(handleType: HandleType): Handle =
    stdlib.malloc(LibUv.uv_handle_size(handleType)).asInstanceOf[Handle]

}

opaque type HandleType = CInt

object HandleType {
  val UV_UNKNOWN_HANDLE: HandleType = 0
  val UV_ASYNC: HandleType = 1
  val UV_CHECK: HandleType = 2
  val UV_FS_EVENT: HandleType = 3
  val UV_FS_POLL: HandleType = 4
  val UV_HANDLE: HandleType = 5
  val UV_IDLE: HandleType = 6
  val UV_NAMED_PIPE: HandleType = 7
  val UV_POLL: HandleType = 8
  val UV_PREPARE: HandleType = 9
  val UV_PROCESS: HandleType = 10
  val UV_STREAM: HandleType = 11
  val UV_TCP: HandleType = 12
  val UV_TIMER: HandleType = 13
  val UV_TTY: HandleType = 14
  val UV_UDP: HandleType = 15
  val UV_SIGNAL: HandleType = 16
  val UV_FILE: HandleType = 17
  val UV_HANDLE_TYPE_MAX: HandleType = 18
}

opaque type AsyncHandle <: Handle = Ptr[Byte]

object AsyncHandle {
  given Tag[AsyncHandle] = Tag.Ptr(Tag.Byte)

  inline def zoneAllocate()(using Zone): AsyncHandle =
    Handle.zoneAllocate(HandleType.UV_ASYNC)

  inline def stackAllocate(): AsyncHandle =
    Handle.stackAllocate(HandleType.UV_ASYNC)

  inline def malloc(): AsyncHandle =
    Handle.malloc(HandleType.UV_ASYNC)
}

opaque type TimerHandle <: Handle = Ptr[Byte]

object TimerHandle {
  given Tag[TimerHandle] = Tag.Ptr(Tag.Byte)

  inline def zoneAllocate()(using Zone): TimerHandle =
    Handle.zoneAllocate(HandleType.UV_TIMER)

  inline def stackAllocate(): TimerHandle =
    Handle.stackAllocate(HandleType.UV_TIMER)

  inline def malloc(): TimerHandle =
    Handle.malloc(HandleType.UV_TIMER)
}

type PrepareHandle = Ptr[Byte]

object PrepareHandle {
  given Tag[PrepareHandle] = Tag.Ptr(Tag.Byte)

  inline def zoneAllocate()(using Zone): PrepareHandle =
    Handle.zoneAllocate(HandleType.UV_PREPARE)

  inline def stackAllocate(): PrepareHandle =
    Handle.stackAllocate(HandleType.UV_PREPARE)

  inline def malloc(): PrepareHandle =
    Handle.malloc(HandleType.UV_PREPARE)
}

type CheckHandle = Ptr[Byte]

object CheckHandle {
  given Tag[CheckHandle] = Tag.Ptr(Tag.Byte)

  inline def zoneAllocate()(using Zone): CheckHandle =
    Handle.zoneAllocate(HandleType.UV_CHECK)

  inline def stackAllocate(): CheckHandle =
    Handle.stackAllocate(HandleType.UV_CHECK)

  inline def malloc(): CheckHandle =
    Handle.malloc(HandleType.UV_CHECK)
}

type IdleHandle = Ptr[Byte]

object IdleHandle {
  given Tag[IdleHandle] = Tag.Ptr(Tag.Byte)

  inline def zoneAllocate()(using Zone): IdleHandle =
    Handle.zoneAllocate(HandleType.UV_IDLE)

  inline def stackAllocate(): IdleHandle =
    Handle.stackAllocate(HandleType.UV_IDLE)

  inline def malloc(): IdleHandle =
    Handle.malloc(HandleType.UV_IDLE)
}

type PollHandle = Ptr[Byte]

object PollHandle {
  given Tag[PollHandle] = Tag.Ptr(Tag.Byte)

  inline def zoneAllocate()(using Zone): PollHandle =
    Handle.zoneAllocate(HandleType.UV_POLL)

  inline def stackAllocate(): PollHandle =
    Handle.stackAllocate(HandleType.UV_POLL)

  inline def malloc(): PollHandle =
    Handle.malloc(HandleType.UV_POLL)
}

type SignalHandle = Ptr[Byte]

object SignalHandle {
  given Tag[SignalHandle] = Tag.Ptr(Tag.Byte)

  inline def zoneAllocate()(using Zone): SignalHandle =
    Handle.zoneAllocate(HandleType.UV_SIGNAL)

  inline def stackAllocate(): SignalHandle =
    Handle.stackAllocate(HandleType.UV_SIGNAL)

  inline def malloc(): SignalHandle =
    Handle.malloc(HandleType.UV_SIGNAL)
}

opaque type StreamHandle <: Handle = Ptr[Byte]

object StreamHandle {

  given Tag[StreamHandle] = Tag.Ptr(Tag.Byte)

}

opaque type TcpHandle <: StreamHandle = Ptr[Byte]

object TcpHandle {
  given Tag[TcpHandle] = Tag.Ptr(Tag.Byte)

  inline def zoneAllocate()(using Zone): TcpHandle =
    Handle.zoneAllocate(HandleType.UV_TCP)

  inline def stackAllocate(): TcpHandle =
    Handle.stackAllocate(HandleType.UV_TCP)

  inline def malloc(): TcpHandle =
    Handle.malloc(HandleType.UV_TCP)
}

opaque type PipeHandle <: StreamHandle = Ptr[Byte]

object PipeHandle {
  given Tag[PipeHandle] = Tag.Ptr(Tag.Byte)

  inline def zoneAllocate()(using Zone): PipeHandle =
    Handle.zoneAllocate(HandleType.UV_NAMED_PIPE)

  inline def stackAllocate(): PipeHandle =
    Handle.stackAllocate(HandleType.UV_NAMED_PIPE)

  inline def malloc(): PipeHandle =
    Handle.malloc(HandleType.UV_NAMED_PIPE)
}

opaque type TtyHandle <: StreamHandle = Ptr[Byte]

object TtyHandle {
  given Tag[TtyHandle] = Tag.Ptr(Tag.Byte)

  inline def zoneAllocate()(using Zone): TtyHandle =
    Handle.zoneAllocate(HandleType.UV_TTY)

  inline def stackAllocate(): TtyHandle =
    Handle.stackAllocate(HandleType.UV_TTY)

  inline def malloc(): TtyHandle =
    Handle.malloc(HandleType.UV_TTY)
}

opaque type UdpHandle = Ptr[Byte]

object UdpHandle {
  given Tag[UdpHandle] = Tag.Ptr(Tag.Byte)

  inline def zoneAllocate()(using Zone): UdpHandle =
    Handle.zoneAllocate(HandleType.UV_UDP)

  inline def stackAllocate(): UdpHandle =
    Handle.stackAllocate(HandleType.UV_UDP)

  inline def malloc(): UdpHandle =
    Handle.malloc(HandleType.UV_UDP)
}

opaque type FsEventHandle = Ptr[Byte]

object FsEventHandle {
  given Tag[FsEventHandle] = Tag.Ptr(Tag.Byte)

  inline def zoneAllocate()(using Zone): FsEventHandle =
    Handle.zoneAllocate(HandleType.UV_FS_EVENT)

  inline def stackAllocate(): FsEventHandle =
    Handle.stackAllocate(HandleType.UV_FS_EVENT)

  inline def malloc(): FsEventHandle =
    Handle.malloc(HandleType.UV_FS_EVENT)
}

opaque type FsPollHandle = Ptr[Byte]

object FsPollHandle {
  given Tag[FsPollHandle] = Tag.Ptr(Tag.Byte)

  inline def zoneAllocate()(using Zone): FsPollHandle =
    Handle.zoneAllocate(HandleType.UV_FS_POLL)

  inline def stackAllocate(): FsPollHandle =
    Handle.stackAllocate(HandleType.UV_FS_POLL)

  inline def malloc(): FsPollHandle =
    Handle.malloc(HandleType.UV_FS_POLL)
}

type FileHandle = CInt
