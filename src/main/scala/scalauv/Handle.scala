package scalauv

import scala.scalanative.unsafe.*
import scala.scalanative.libc.stdlib

/** Representation of a `uv_handle_t*` native type.
  *
  * @see
  *   [[https://docs.libuv.org/en/v1.x/handle.html#c.uv_handle_t Libuv Docs]]
  */
opaque type Handle = Ptr[Byte]

/** Constructors for the [[scalauv.Handle Handle]] opaque type that represents a
  * pointer to the `uv_handle_t` native C type. While this object can be used to
  * allocate handles by providing a handle type, it is more common to use the
  * equivalent allocation methods for the exact handle type you want, for
  * example `TcpHandle.malloc()`.
  */
object Handle {
  given Tag[Handle] = Tag.Ptr(Tag.Byte)

  extension (h: Handle) {

    /** Extract the underlying native pointer for this handle.
      */
    inline def toPtr: Ptr[Byte] = h

    /** Frees this hnadle structure.
      */
    inline def free(): Unit = stdlib.free(h)
  }

  /** Casts a native pointer to a handle structure to the `Handle` type.
    */
  inline def unsafeFromPtr(ptr: Ptr[Byte]): Handle = ptr

  /** Stack allocate a handle structure of the specified type.
    */
  inline def stackAllocate(handleType: HandleType): Handle =
    Handle.unsafeFromPtr(stackalloc[Byte](LibUv.uv_handle_size(handleType)))

  /** Zone allocate a handle structure of the specified type.
    */
  inline def zoneAllocate(handleType: HandleType)(using Zone): Handle =
    alloc[Byte](LibUv.uv_handle_size(handleType)).asInstanceOf[Handle]

  /** Malloc allocate a handle structure of the specified type.
    */
  inline def malloc(handleType: HandleType): Handle =
    stdlib.malloc(LibUv.uv_handle_size(handleType)).asInstanceOf[Handle]

}

/** A native C enum representing the type of a handle.
  */
opaque type HandleType = CInt

/** Constants for alll the handle types supported by libuv.
  */
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

/** The `uv_async_t*` type.
  *
  * @see
  *   [[https://docs.libuv.org/en/v1.x/async.html#c.uv_async_t Libuv Docs]]
  */
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

/** The `uv_timer_t*` type.
  *
  * @see
  *   [[https://docs.libuv.org/en/v1.x/timer.html#c.uv_timer_t Libuv Docs]]
  */
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

/** The `uv_prepare_t*` type.
  *
  * @see
  *   [[https://docs.libuv.org/en/v1.x/prepare.html#c.uv_prepare_t Libuv Docs]]
  */
opaque type PrepareHandle <: Handle = Ptr[Byte]

object PrepareHandle {
  given Tag[PrepareHandle] = Tag.Ptr(Tag.Byte)

  inline def zoneAllocate()(using Zone): PrepareHandle =
    Handle.zoneAllocate(HandleType.UV_PREPARE)

  inline def stackAllocate(): PrepareHandle =
    Handle.stackAllocate(HandleType.UV_PREPARE)

  inline def malloc(): PrepareHandle =
    Handle.malloc(HandleType.UV_PREPARE)
}

/** The `uv_check_t*` type.
  *
  * @see
  *   [[https://docs.libuv.org/en/v1.x/check.html#c.uv_check_t Libuv Docs]]
  */
opaque type CheckHandle <: Handle = Ptr[Byte]

object CheckHandle {
  given Tag[CheckHandle] = Tag.Ptr(Tag.Byte)

  inline def zoneAllocate()(using Zone): CheckHandle =
    Handle.zoneAllocate(HandleType.UV_CHECK)

  inline def stackAllocate(): CheckHandle =
    Handle.stackAllocate(HandleType.UV_CHECK)

  inline def malloc(): CheckHandle =
    Handle.malloc(HandleType.UV_CHECK)
}

/** The `uv_idle_t*` type.
  *
  * @see
  *   [[https://docs.libuv.org/en/v1.x/idle.html#c.uv_idle_t Libuv Docs]]
  */
opaque type IdleHandle <: Handle = Ptr[Byte]

object IdleHandle {
  given Tag[IdleHandle] = Tag.Ptr(Tag.Byte)

  inline def zoneAllocate()(using Zone): IdleHandle =
    Handle.zoneAllocate(HandleType.UV_IDLE)

  inline def stackAllocate(): IdleHandle =
    Handle.stackAllocate(HandleType.UV_IDLE)

  inline def malloc(): IdleHandle =
    Handle.malloc(HandleType.UV_IDLE)
}

/** The `uv_poll_t*` type.
  *
  * @see
  *   [[https://docs.libuv.org/en/v1.x/poll.html#c.uv_poll_t Libuv Docs]]
  */
opaque type PollHandle <: Handle = Ptr[Byte]

object PollHandle {
  given Tag[PollHandle] = Tag.Ptr(Tag.Byte)

  inline def zoneAllocate()(using Zone): PollHandle =
    Handle.zoneAllocate(HandleType.UV_POLL)

  inline def stackAllocate(): PollHandle =
    Handle.stackAllocate(HandleType.UV_POLL)

  inline def malloc(): PollHandle =
    Handle.malloc(HandleType.UV_POLL)
}

/** The `uv_signal_t*` type.
  *
  * @see
  *   [[https://docs.libuv.org/en/v1.x/signal.html#c.uv_signal_t Libuv Docs]]
  */
opaque type SignalHandle <: Handle = Ptr[Byte]

object SignalHandle {
  given Tag[SignalHandle] = Tag.Ptr(Tag.Byte)

  inline def zoneAllocate()(using Zone): SignalHandle =
    Handle.zoneAllocate(HandleType.UV_SIGNAL)

  inline def stackAllocate(): SignalHandle =
    Handle.stackAllocate(HandleType.UV_SIGNAL)

  inline def malloc(): SignalHandle =
    Handle.malloc(HandleType.UV_SIGNAL)
}

/** The `uv_stream_t*` type.
  *
  * @see
  *   [[https://docs.libuv.org/en/v1.x/stream.html#c.uv_stream_t Libuv Docs]]
  */
opaque type StreamHandle <: Handle = Ptr[Byte]

object StreamHandle {

  given Tag[StreamHandle] = Tag.Ptr(Tag.Byte)

}

/** The `uv_tcp_t*` type.
  *
  * @see
  *   [[https://docs.libuv.org/en/v1.x/tcp.html#c.uv_tcp_t Libuv Docs]]
  */
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

/** The `uv_pipe_t*` type.
  *
  * @see
  *   [[https://docs.libuv.org/en/v1.x/pipe.html#c.uv_pipe_t Libuv Docs]]
  */
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

/** The `uv_tty_t*` type.
  *
  * @see
  *   [[https://docs.libuv.org/en/v1.x/tty.html#c.uv_tty_t Libuv Docs]]
  */
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

/** The `uv_udp_t*` type.
  *
  * @see
  *   [[https://docs.libuv.org/en/v1.x/udp.html#c.uv_udp_t Libuv Docs]]
  */
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

/** The `uv_fs_event_t*` type.
  *
  * @see
  *   [[https://docs.libuv.org/en/v1.x/fs_event.html#c.uv_fs_event_t Libuv Docs]]
  */
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

/** The `uv_fs_poll_t*` type.
  *
  * @see
  *   [[https://docs.libuv.org/en/v1.x/fs_poll.html#c.uv_fs_poll_t Libuv Docs]]
  */
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

/** The `uv_fs_t` type.
  *
  * @see
  *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_t Libuv Docs]]
  */
type FileHandle = CInt
