package scalauv

import scala.scalanative.unsafe.*
import scala.scalanative.posix.netinet.in
import scala.scalanative.posix.sys.socket
import scala.scalanative.posix.netdb
import scala.scalanative.posix.inttypes.*

opaque type Loop = Ptr[Byte]

/** The main API of libuv.
  *
  * @groupprio event_loop 10
  * @groupprio base_handle 20
  * @groupprio base_request 30
  * @groupprio misc 40
  * @groupprio stream_handle 50
  * @groupprio tcp_handle 60
  * @groupprio fs 70
  * @groupprio async_handle 80
  * @groupprio timer_handle 90
  * @groupprio prepare_handle 100
  * @groupprio check_handle 110
  * @groupprio idle_handle 120
  * @groupprio poll_handle 130
  * @groupprio signal_handle 140
  * @groupprio process_handle 150
  * @groupprio thread_pool 160
  * @groupprio dns 170
  * @groupprio thread 180
  * @groupprio fs_event_handle 190
  * @groupprio fs_poll_handle 200
  *
  * @groupname event_loop Event loop
  * @groupname base_handle Base handle
  * @groupname base_request Base request
  * @groupname timer_handle Timer handle
  * @groupname prepare_handle Prepare handle
  * @groupname check_handle Check handle
  * @groupname idle_handle Idle handle
  * @groupname async_handle Async handle
  * @groupname poll_handle Poll handle
  * @groupname signal_handle Signal handle
  * @groupname process_handle Process handle
  * @groupname stream_handle Stream handle
  * @groupname tcp_handle TCP handle
  * @groupname pipe_handle Pipe handle
  * @groupname tty_handle TTY handle
  * @groupname udp_handle UDP handle
  * @groupname fs_event_handle Filesystem event handle
  * @groupname fs_poll_handle Filesystem poll handle
  * @groupname timer_handle Timer handle
  * @groupname fs File system operations
  * @groupname thread_pool Thread pool
  * @groupname dns DNS
  * @groupname thread Threads and synchronization
  * @groupname misc Miscellaneous
  */
@link("uv")
@extern
object LibUv {

  // =========================================================
  // Event loop

  /** Callback for `uv_walk`.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/loop.html#c.uv_walk_cb LibUv docs]]
    * @group event_loop
    */
  type WalkCallback = CFuncPtr2[Handle, Ptr[Byte], Unit]

  /** Initialize an event loop.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/loop.html#c.uv_loop_init LibUv docs]]
    * @group event_loop
    */
  def uv_loop_init(loop: Loop): ErrorCode = extern

  // TODO: figure out best way to handle varargs
//   def uv_loop_configure(loop: Loop, option: CInt, value: CInt): ErrorCode =
//     extern

  /** Close an event loop.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/loop.html#c.uv_loop_close LibUv docs]]
    * @group event_loop
    */
  def uv_loop_close(loop: Loop): ErrorCode = extern

  /** Get the default event loop.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/loop.html#c.uv_default_loop LibUv docs]]
    * @group event_loop
    */
  def uv_default_loop(): Loop = extern

  /** Run the event loop.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/loop.html#c.uv_run LibUv docs]]
    * @group event_loop
    */
  def uv_run(loop: Loop, runMode: RunMode): ErrorCode = extern

  /** Check if the event loop is still alive.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/loop.html#c.uv_loop_alive LibUv docs]]
    * @group event_loop
    */
  def uv_loop_alive(loop: Loop): CInt = extern

  /** Stop the event loop.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/loop.html#c.uv_stop LibUv docs]]
    * @group event_loop
    */
  def uv_stop(loop: Loop): Unit = extern

  /** Get the size of the event loop structure.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/loop.html#c.uv_loop_size LibUv docs]]
    * @group event_loop
    */
  def uv_loop_size(): CSize = extern

  /** Get the file descriptor of the event loop.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/loop.html#c.uv_backend_fd LibUv docs]]
    * @group event_loop
    */
  def uv_backend_fd(loop: Loop): CInt = extern

  /** Get the timeout of the event loop.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/loop.html#c.uv_backend_timeout LibUv docs]]
    * @group event_loop
    */
  def uv_backend_timeout(loop: Loop): CInt = extern

  /** Get the current time.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/loop.html#c.uv_now LibUv docs]]
    * @group event_loop
    */
  def uv_now(loop: Loop): uint64_t = extern

  /** Update event loop's current time.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/loop.html#c.uv_update_time LibUv docs]]
    * @group event_loop
    */
  def uv_update_time(loop: Loop): Unit = extern

  /** Walk the event loop's handles.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/loop.html#c.uv_walk LibUv docs]]
    * @group event_loop
    */
  def uv_walk(loop: Loop, walkCallback: WalkCallback, arg: Ptr[Byte]): Unit =
    extern

  /** Initialize the event loop after a fork.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/loop.html#c.uv_loop_fork LibUv docs]]
    * @group event_loop
    */
  def uv_loop_fork(loop: Loop): ErrorCode = extern

  /** Get the user data attached to the event loop.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/loop.html#c.uv_loop_get_data LibUv docs]]
    * @group event_loop
    */
  def uv_loop_get_data(loop: Loop): Ptr[Byte] = extern

  /** Set the user data attached to the event loop.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/loop.html#c.uv_loop_set_data LibUv docs]]
    * @group event_loop
    */
  def uv_loop_set_data(loop: Loop, data: Ptr[Byte]): Unit = extern

  // =========================================================
  // Base handle

  /** Callback for buffer allocation.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/handle.html#c.uv_alloc_cb LibUv docs]]
    * @group base_handle
    */
  type AllocCallback = CFuncPtr3[Handle, CSize, Buffer, Unit]

  /** Callback for closing handles.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/handle.html#c.uv_close_cb LibUv docs]]
    * @group base_handle
    */
  type CloseCallback = CFuncPtr1[Handle, Unit]

  /** Check if a handle is active.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/handle.html#c.uv_is_active LibUv docs]]
    * @group base_handle
    */
  def uv_is_active(handle: Handle): CInt = extern

  /** Check if a handle is closing or closed.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/handle.html#c.uv_is_closing LibUv docs]]
    * @group base_handle
    */
  def uv_is_closing(handle: Handle): CInt = extern

  /** Close a handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/handle.html#c.uv_close LibUv docs]]
    * @group base_handle
    */
  def uv_close(handle: Handle, callback: CloseCallback): Unit = extern

  /** Reference a handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/handle.html#c.uv_ref LibUv docs]]
    * @group base_handle
    */
  def uv_ref(handle: Handle): Unit = extern

  /** Unreference a handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/handle.html#c.uv_unref LibUv docs]]
    * @group base_handle
    */
  def uv_unref(handle: Handle): Unit = extern

  /** Check if a handle is referenced.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/handle.html#c.uv_has_ref LibUv docs]]
    * @group base_handle
    */
  def uv_has_ref(handle: Handle): CInt = extern

  /** Get the size of a handle structure.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/handle.html#c.uv_handle_size LibUv docs]]
    * @group base_handle
    */
  def uv_handle_size(handle_type: HandleType): CSize = extern

  /** Gets the size of the send buffer.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/handle.html#c.uv_send_buffer_size LibUv docs]]
    * @group base_handle
    */
  def uv_send_buffer_size(handle: Handle, value: Ptr[CInt]): ErrorCode = extern

  /** Sets the size of the receive buffer.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/handle.html#c.uv_recv_buffer_size LibUv docs]]
    * @group base_handle
    */
  def uv_recv_buffer_size(handle: Handle, value: Ptr[CInt]): ErrorCode = extern

  // TODO: the file descriptor type varies between Unix and Windows
//   def uv_fileno(handle: Handle, fd: Ptr[CInt]): ErrorCode = extern

  /** Get the event loop of a handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/handle.html#c.uv_handle_get_loop LibUv docs]]
    * @group base_handle
    */
  def uv_handle_get_loop(handle: Handle): Loop = extern

  /** Get the user data attached to a handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/handle.html#c.uv_handle_get_data LibUv docs]]
    * @group base_handle
    */
  def uv_handle_get_data(handle: Handle): Ptr[Byte] = extern

  /** Set the user data attached to a handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/handle.html#c.uv_handle_set_data LibUv docs]]
    * @group base_handle
    */
  def uv_handle_set_data(handle: Handle, data: Ptr[Byte]): Unit = extern

  /** Get the type of a handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/handle.html#c.uv_handle_get_type LibUv docs]]
    * @group base_handle
    */
  def uv_handle_get_type(handle: Handle): HandleType = extern

  /** Get the name of a handle type.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/handle.html#c.uv_handle_type_name LibUv docs]]
    * @group base_handle
    */
  def uv_handle_type_name(handleType: HandleType): CString = extern

  // =========================================================
  // Base request

  /** Cancels a request.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/request.html#c.uv_cancel LibUv docs]]
    * @group base_request
    */
  def uv_cancel(req: Req): ErrorCode = extern

  /** Get the size of a request structure.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/request.html#c.uv_req_size LibUv docs]]
    * @group base_request
    */
  def uv_req_size(req_type: RequestType): CSize = extern

  /** Gets th user data attached to a request.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/request.html#c.uv_req_get_data LibUv docs]]
    * @group base_request
    */
  def uv_req_get_data(req: Req): Ptr[Byte] = extern

  /** Sets the user data attached to a request.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/request.html#c.uv_req_set_data LibUv docs]]
    * @group base_request
    */
  def uv_req_set_data(req: Req, data: Ptr[Byte]): Unit = extern

  /** Gets the type of a request.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/request.html#c.uv_req_get_type LibUv docs]]
    * @group base_request
    */
  def uv_req_get_type(req: Req): RequestType = extern

  /** Gets the name of a request type.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/request.html#c.uv_req_type_name LibUv docs]]
    * @group base_request
    */
  def uv_req_type_name(reqType: RequestType): CString = extern

  // =========================================================
  // Timer handle

  /** Callback for timers.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/timer.html#c.uv_timer_cb LibUv docs]]
    * @group timer_handle
    */
  type TimerCallback = CFuncPtr1[TimerHandle, Unit]

  /** Initialize a timer handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/timer.html#c.uv_timer_init LibUv docs]]
    * @group timer_handle
    */
  def uv_timer_init(loop: Loop, handle: TimerHandle): ErrorCode = extern

  /** Start a timer.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/timer.html#c.uv_timer_start LibUv docs]]
    * @group timer_handle
    */
  def uv_timer_start(
      handle: TimerHandle,
      callback: TimerCallback,
      timeoutMillis: CUnsignedLong,
      repeatMillis: CUnsignedLong
  ): ErrorCode = extern

  /** Stop a timer.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/timer.html#c.uv_timer_stop LibUv docs]]
    * @group timer_handle
    */
  def uv_timer_stop(handle: TimerHandle): ErrorCode = extern

  /** Stop and repeat a timer.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/timer.html#c.uv_timer_again LibUv docs]]
    * @group timer_handle
    */
  def uv_timer_again(handle: TimerHandle): ErrorCode = extern

  /** Set the repeat interval of a timer.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/timer.html#c.uv_timer_set_repeat LibUv docs]]
    * @group timer_handle
    */
  def uv_timer_set_repeat(
      handle: TimerHandle,
      repeatMillis: uint64_t
  ): Unit =
    extern

  /** Gets the repeat interval of a timer.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/timer.html#c.uv_timer_get_repeat LibUv docs]]
    * @group timer_handle
    */
  def uv_timer_get_repeat(handle: TimerHandle): uint64_t = extern

  /** Gets the time until a timer is due.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/timer.html#c.uv_timer_get_due_in LibUv docs]]
    * @group timer_handle
    */
  def uv_timer_get_due_in(handle: TimerHandle): uint64_t = extern

  // =========================================================
  // Prepare handle

  /** Callback for prepare handles.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/prepare.html#c.uv_prepare_cb LibUv docs]]
    * @group prepare_handle
    */
  type PrepareCallback = CFuncPtr1[PrepareHandle, Unit]

  /** Initialize a prepare handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/prepare.html#c.uv_prepare_init LibUv docs]]
    * @group prepare_handle
    */
  def uv_prepare_init(loop: Loop, handle: PrepareHandle): ErrorCode = extern

  /** Start a prepare handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/prepare.html#c.uv_prepare_start LibUv docs]]
    * @group prepare_handle
    */
  def uv_prepare_start(
      handle: PrepareHandle,
      callback: PrepareCallback
  ): ErrorCode = extern

  /** Stop a prepare handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/prepare.html#c.uv_prepare_stop LibUv docs]]
    * @group prepare_handle
    */
  def uv_prepare_stop(handle: PrepareHandle): ErrorCode = extern

  // =========================================================
  // Check handle

  /** Callback for check handles.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/check.html#c.uv_check_cb LibUv docs]]
    * @group check_handle
    */
  type CheckCallback = CFuncPtr1[CheckHandle, Unit]

  /** Initialize a check handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/check.html#c.uv_check_init LibUv docs]]
    * @group check_handle
    */
  def uv_check_init(loop: Loop, handle: CheckHandle): ErrorCode = extern

  /** Start a check handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/check.html#c.uv_check_start LibUv docs]]
    * @group check_handle
    */
  def uv_check_start(handle: CheckHandle, callback: CheckCallback): ErrorCode =
    extern

    /** Stop a check handle.
      *
      * @see
      *   [[https://docs.libuv.org/en/v1.x/check.html#c.uv_check_stop LibUv docs]]
      * @group check_handle
      */
  def uv_check_stop(handle: CheckHandle): ErrorCode = extern

  // =========================================================
  // Idle handle

  /** Callback for idle handles.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/idle.html#c.uv_idle_cb LibUv docs]]
    * @group idle_handle
    */
  type IdleCallback = CFuncPtr1[IdleHandle, Unit]

  /** Initialize an idle handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/idle.html#c.uv_idle_init LibUv docs]]
    * @group idle_handle
    */
  def uv_idle_init(loop: Loop, handle: IdleHandle): ErrorCode = extern

  /** Start an idle handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/idle.html#c.uv_idle_start LibUv docs]]
    * @group idle_handle
    */
  def uv_idle_start(handle: IdleHandle, callback: IdleCallback): ErrorCode =
    extern

    /** Stop an idle handle.
      *
      * @see
      *   [[https://docs.libuv.org/en/v1.x/idle.html#c.uv_idle_stop LibUv docs]]
      * @group idle_handle
      */
  def uv_idle_stop(handle: IdleHandle): ErrorCode = extern

  // =========================================================
  // Async handle

  /** Callback for async handles.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/async.html#c.uv_async_cb LibUv docs]]
    * @group async_handle
    */
  type AsyncCallback = CFuncPtr1[AsyncHandle, Unit]

  /** Initialize an async handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/async.html#c.uv_async_init LibUv docs]]
    * @group async_handle
    */
  def uv_async_init(
      loop: Loop,
      handle: AsyncHandle,
      callback: AsyncCallback
  ): ErrorCode = extern

  /** Send a signal to an async handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/async.html#c.uv_async_send LibUv docs]]
    * @group async_handle
    */
  def uv_async_send(handle: AsyncHandle): ErrorCode = extern

  // =========================================================
  // Poll handle

  /** Callback for poll handles.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/poll.html#c.uv_poll_cb LibUv docs]]
    * @group poll_handle
    */
  type PollCallback = CFuncPtr3[PollHandle, ErrorCode, CInt, Unit]

  /** Initialize a poll handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/poll.html#c.uv_poll_init LibUv docs]]
    * @group poll_handle
    */
  def uv_poll_init(loop: Loop, handle: PollHandle, fd: CInt): ErrorCode =
    extern

    // TODO: what type to use for OsSocketHandle?
//   def uv_poll_init_socket(
//       loop: Loop,
//       handle: PollHandle,
//       socket: OsSocketHandle
//   ): ErrorCode = extern

  /** Start a poll handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/poll.html#c.uv_poll_start LibUv docs]]
    * @group poll_handle
    */
  def uv_poll_start(
      handle: PollHandle,
      events: CInt,
      callback: PollCallback
  ): ErrorCode = extern

  /** Stop a poll handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/poll.html#c.uv_poll_stop LibUv docs]]
    * @group poll_handle
    */
  def uv_poll_stop(handle: PollHandle): ErrorCode = extern

  // =========================================================
  // Signal handle

  /** Callback for signal handles.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/signal.html#c.uv_signal_cb LibUv docs]]
    * @group signal_handle
    */
  type SignalCallback = CFuncPtr2[SignalHandle, CInt, Unit]

  /** Initialize a signal handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/signal.html#c.uv_signal_init LibUv docs]]
    * @group signal_handle
    */
  def uv_signal_init(loop: Loop, handle: SignalHandle): ErrorCode = extern

  /** Start a signal handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/signal.html#c.uv_signal_start LibUv docs]]
    * @group signal_handle
    */
  def uv_signal_start(
      handle: SignalHandle,
      callback: SignalCallback,
      signum: CInt
  ): ErrorCode = extern

  /** Start a one-shot signal handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/signal.html#c.uv_signal_start_one_shot LibUv docs]]
    * @group signal_handle
    */
  def uv_signal_start_one_shot(
      handle: SignalHandle,
      callback: SignalCallback,
      signum: CInt
  ): ErrorCode = extern

  /** Stop a signal handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/signal.html#c.uv_signal_stop LibUv docs]]
    * @group signal_handle
    */
  def uv_signal_stop(handle: SignalHandle): ErrorCode = extern

  // =========================================================
  // Process handle

//   type ProcessHandle = Handle

//   type ProcessOptions = CStruct10[ExitCallback, CString, Ptr[CString], Ptr[
//     CString
//   ], CString, CUnsignedInt, CInt, StdioContainer, Uid, Guid]

//   type ExitCallback = CFuncPtr3[ProcessHandle, CLongLong, CInt, Unit]

//   type ProcessFlags = CInt

//   type StdioContainer = CStruct2[StdioFlags, StreamHandle]

//   type StdioFlags = CInt

//   def uv_disable_stdio_inheritance(): Unit = extern

//   def uv_spawn(
//       loop: Loop,
//       handle: ProcessHandle,
//       options: ProcessOptions
//   ): ErrorCode = extern

//   def uv_process_kill(handle: ProcessHandle, signum: CInt): ErrorCode = extern

//   def uv_kill(pid: CInt, signum: CInt): ErrorCode = extern

  // TODO: what type to use for uv_pid_t?
//   def uv_process_get_pid(handle: ProcessHandle): CInt = extern

  // =========================================================
  // Stream handle

  /** Callback for reading from a stream.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/stream.html#c.uv_read_cb LibUv docs]]
    * @group stream_handle
    */
  type StreamReadCallback = CFuncPtr3[StreamHandle, CSSize, Buffer, Unit]

  /** Callback for writing to a stream.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/stream.html#c.uv_write_cb LibUv docs]]
    * @group stream_handle
    */
  type StreamWriteCallback = CFuncPtr2[WriteReq, CInt, Unit]

  /** Callback for connecting a stream.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/stream.html#c.uv_connect_cb LibUv docs]]
    * @group stream_handle
    */
  type ConnectCallback = CFuncPtr2[ConnectReq, CInt, Unit]

  /** Callback for shutting down a stream.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/stream.html#c.uv_shutdown_cb LibUv docs]]
    * @group stream_handle
    */
  type ShutdownCallback = CFuncPtr2[Req, CInt, Unit]

  /** Callback for accepting an incoming connection.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/stream.html#c.uv_connection_cb LibUv docs]]
    * @group stream_handle
    */
  type ConnectionCallback = CFuncPtr2[StreamHandle, ErrorCode, Unit]

  /** Shudown a stream.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/stream.html#c.uv_shutdown LibUv docs]]
    * @group stream_handle
    */
  def uv_shutdown(
      req: ShutdownReq,
      handle: StreamHandle,
      cb: ShutdownCallback
  ): ErrorCode = extern

  /** Listen for incoming connections.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/stream.html#c.uv_listen LibUv docs]]
    * @group stream_handle
    */
  def uv_listen(
      stream: StreamHandle,
      backlog: CInt,
      cb: ConnectionCallback
  ): ErrorCode = extern

  /** Accept an incoming connection.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/stream.html#c.uv_accept LibUv docs]]
    * @group stream_handle
    */
  def uv_accept(
      server: StreamHandle,
      client: StreamHandle
  ): ErrorCode = extern

  /** Start reading from a stream.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/stream.html#c.uv_read_start LibUv docs]]
    * @group stream_handle
    */
  def uv_read_start(
      stream: StreamHandle,
      alloc_cb: AllocCallback,
      read_cb: StreamReadCallback
  ): ErrorCode = extern

  /** Stop reading from a stream.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/stream.html#c.uv_read_stop LibUv docs]]
    * @group stream_handle
    */
  def uv_read_stop(stream: StreamHandle): ErrorCode = extern

  /** Write to a stream.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/stream.html#c.uv_write LibUv docs]]
    * @group stream_handle
    */
  def uv_write(
      req: WriteReq,
      handle: StreamHandle,
      bufs: Buffer,
      numberOfBufs: CUnsignedInt,
      cb: StreamWriteCallback
  ): ErrorCode = extern

  /** Write to a stream with a handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/stream.html#c.uv_write2 LibUv docs]]
    * @group stream_handle
    */
  def uv_write2(
      req: WriteReq,
      handle: StreamHandle,
      bufs: Buffer,
      numberOfBufs: CUnsignedInt,
      sendHandle: StreamHandle,
      cb: StreamWriteCallback
  ): ErrorCode = extern

  /** Try writing to a stream.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/stream.html#c.uv_try_write LibUv docs]]
    * @group stream_handle
    */
  def uv_try_write(
      handle: StreamHandle,
      bufs: Buffer,
      numberOfBufs: CUnsignedInt
  ): CInt = extern

  /** Try writing to a stream with a handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/stream.html#c.uv_try_write2 LibUv docs]]
    * @group stream_handle
    */
  def uv_try_write2(
      handle: StreamHandle,
      bufs: Buffer,
      numberOfBufs: CUnsignedInt,
      sendHandle: StreamHandle
  ): CInt = extern

  /** Check if a stream is readable.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/stream.html#c.uv_is_readable LibUv docs]]
    * @group stream_handle
    */
  def uv_is_readable(handle: StreamHandle): CInt = extern

  /** Check if a stream is writable.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/stream.html#c.uv_is_writable LibUv docs]]
    * @group stream_handle
    */
  def uv_is_writable(handle: StreamHandle): CInt = extern

  /** Set the blocking mode of a stream.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/stream.html#c.uv_stream_set_blocking LibUv docs]]
    * @group stream_handle
    */
  def uv_stream_set_blocking(handle: StreamHandle, blocking: CInt): CInt =
    extern

  /** Get the write queue size of a stream.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/stream.html#c.uv_stream_get_write_queue_size LibUv docs]]
    * @group stream_handle
    */
  def uv_stream_get_write_queue_size(handle: StreamHandle): CSize = extern

  // =========================================================
  // TCP handle

  /** Initializes a TCP handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/tcp.html#c.uv_tcp_init LibUv docs]]
    * @group tcp_handle
    */
  def uv_tcp_init(loop: Loop, handle: TcpHandle): ErrorCode = extern

  /** Initializes a TCP handle with additional flags.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/tcp.html#c.uv_tcp_init_ex LibUv docs]]
    * @group tcp_handle
    */
  def uv_tcp_init_ex(
      loop: Loop,
      handle: TcpHandle,
      flags: CUnsignedInt
  ): ErrorCode = extern

  /** Opens a TCP handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/tcp.html#c.uv_tcp_open LibUv docs]]
    * @group tcp_handle
    */
  def uv_tcp_open(handle: TcpHandle, sock: CInt): ErrorCode = extern

  /** Sets the no-delay status of a TCP handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/tcp.html#c.uv_tcp_nodelay LibUv docs]]
    * @group tcp_handle
    */
  def uv_tcp_no_delay(handle: TcpHandle, enable: CInt): ErrorCode = extern

  /** Sets the keep-alive status of a TCP handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/tcp.html#c.uv_tcp_keepalive LibUv docs]]
    * @group tcp_handle
    */
  def uv_tcp_keepalive(
      handle: TcpHandle,
      enable: CInt,
      delay: CUnsignedInt
  ): ErrorCode = extern

  /** Sets the number of simultaneous accepts for a TCP handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/tcp.html#c.uv_tcp_simultaneous_accepts LibUv docs]]
    * @group tcp_handle
    */
  def uv_tcp_simultaneous_accepts(handle: TcpHandle, enable: CInt): ErrorCode =
    extern

  /** Binds a TCP handle to an address.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/tcp.html#c.uv_tcp_bind LibUv docs]]
    * @group tcp_handle
    */
  def uv_tcp_bind(
      handle: TcpHandle,
      addr: Ptr[socket.sockaddr],
      flags: CUnsignedInt
  ): ErrorCode = extern

  /** Gets the socket address bound for a handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/tcp.html#c.uv_tcp_getsockname LibUv docs]]
    * @group tcp_handle
    */
  def uv_tcp_getsockname(
      handle: TcpHandle,
      name: Ptr[socket.sockaddr],
      namelen: Ptr[CInt]
  ): ErrorCode = extern

  /** Gets the socket address of the peer for a handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/tcp.html#c.uv_tcp_getpeername LibUv docs]]
    * @group tcp_handle
    */
  def uv_tcp_getpeername(
      handle: TcpHandle,
      name: Ptr[socket.sockaddr],
      namelen: Ptr[CInt]
  ): ErrorCode = extern

  /** Connects a TCP handle to an address.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/tcp.html#c.uv_tcp_connect LibUv docs]]
    * @group tcp_handle
    */
  def uv_tcp_connect(
      req: Req,
      handle: TcpHandle,
      addr: Ptr[socket.sockaddr],
      cb: ConnectCallback
  ): ErrorCode = extern

  /** Closes and resets a TCP handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/tcp.html#c.uv_tcp_close_reset LibUv docs]]
    * @group tcp_handle
    */
  def uv_tcp_close_reset(handle: TcpHandle, cb: CloseCallback): ErrorCode =
    extern

    // TODO: what type to use for OsSocketHandle?
//   def uv_socketpair(
//       socketType: CInt,
//       protocol: CInt,
//       socketVector: Ptr[CArray[OsSocketHandle, Nat._2]],
//       flags0: CInt,
//       flags1: CInt
//   ): ErrorCode = extern

  // =========================================================
  // Pipe handle

  /** Initializes a pipe handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/pipe.html#c.uv_pipe_init LibUv docs]]
    * @group pipe_handle
    */
  def uv_pipe_init(
      loop: Loop,
      handle: PipeHandle,
      ipc: CInt
  ): ErrorCode = extern

  /** Opens a pipe handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/pipe.html#c.uv_pipe_open LibUv docs]]
    * @group pipe_handle
    */
  def uv_pipe_open(
      handle: PipeHandle,
      file: FileHandle
  ): ErrorCode = extern

  /** Binds a pipe handle to a name.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/pipe.html#c.uv_pipe_bind LibUv docs]]
    * @group pipe_handle
    */
  def uv_pipe_bind(
      handle: PipeHandle,
      name: CString
  ): ErrorCode = extern

  /** Binds a pipe handle to a name with additional flags.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/pipe.html#c.uv_pipe_bind2 LibUv docs]]
    * @group pipe_handle
    */
  def uv_pipe_bind2(
      handle: PipeHandle,
      name: CString,
      nameLength: CSize,
      flags: CUnsignedInt
  ): ErrorCode = extern

  /** Connects a pipe handle to a name.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/pipe.html#c.uv_pipe_connect LibUv docs]]
    * @group pipe_handle
    */
  def uv_pipe_connect(
      req: ConnectReq,
      handle: PipeHandle,
      name: CString,
      cb: ConnectCallback
  ): Unit = extern

  /** Connects a pipe handle to a name with additional flags.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/pipe.html#c.uv_pipe_connect2 LibUv docs]]
    * @group pipe_handle
    */
  def uv_pipe_connect2(
      req: ConnectReq,
      handle: PipeHandle,
      name: CString,
      nameLength: CSize,
      flags: CUnsignedInt,
      cb: ConnectCallback
  ): Unit = extern

  /** Gets the name of a pipe handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/pipe.html#c.uv_pipe_getsockname LibUv docs]]
    * @group pipe_handle
    */
  def uv_pipe_getsockname(
      handle: PipeHandle,
      buffer: CString,
      nameLength: Ptr[CSize]
  ): ErrorCode = extern

  /** Gets the name of the peer of a pipe handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/pipe.html#c.uv_pipe_getpeername LibUv docs]]
    * @group pipe_handle
    */
  def uv_pipe_getpeername(
      handle: PipeHandle,
      buffer: CString,
      nameLength: Ptr[CSize]
  ): ErrorCode = extern

  /** Sets the pending instance count of a pipe handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/pipe.html#c.uv_pipe_pending_instances LibUv docs]]
    * @group pipe_handle
    */
  def uv_pipe_pending_instances(
      handle: PipeHandle,
      count: CInt
  ): Unit = extern

  /** Gets the pending count of a pipe handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/pipe.html#c.uv_pipe_pending_count LibUv docs]]
    * @group pipe_handle
    */
  def uv_pipe_pending_count(handle: PipeHandle): CSize = extern

  /** Gets the pending type of a pipe handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/pipe.html#c.uv_pipe_pending_type LibUv docs]]
    * @group pipe_handle
    */
  def uv_pipe_pending_type(handle: PipeHandle): HandleType = extern

  /** Chmod a pipe handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/pipe.html#c.uv_pipe_chmod LibUv docs]]
    * @group pipe_handle
    */
  def uv_pipe_chmod(handle: PipeHandle, flags: CInt): ErrorCode = extern

  /** Creates a pair of connected pipe handles.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/pipe.html#c.uv_pipe LibUv docs]]
    * @group pipe_handle
    */
  def uv_pipe(
      fileHandles: Ptr[FileHandle],
      readFlags: CInt,
      writeFlags: CInt
  ): ErrorCode = extern

  // =========================================================
  // TTY handle

  /** Initializes a TTY handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/tty.html#c.uv_tty_init LibUv docs]]
    * @group tty_handle
    */
  def uv_tty_init(
      loop: Loop,
      handle: TtyHandle,
      fd: FileHandle,
      unused: CInt
  ): ErrorCode = extern

  /** Resets a TTY handle mode.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/tty.html#c.uv_tty_reset_mode LibUv docs]]
    * @group tty_handle
    */
  def uv_tty_set_mode(handle: TtyHandle, mode: TtyMode): ErrorCode = extern

  /** Resets a TTY handle mode.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/tty.html#c.uv_tty_reset_mode LibUv docs]]
    * @group tty_handle
    */
  def uv_tty_reset_mode(): ErrorCode = extern

  /** Gets the window size of a TTY handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/tty.html#c.uv_tty_get_winsize LibUv docs]]
    * @group tty_handle
    */
  def uv_tty_get_winsize(
      handle: TtyHandle,
      width: Ptr[CInt],
      height: Ptr[CInt]
  ): ErrorCode = extern

  /** Sets the virtual terminal state of a TTY handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/tty.html#c.uv_tty_set_vterm_state LibUv docs]]
    * @group tty_handle
    */
  def uv_tty_set_vterm_state(state: TtyVtermState): ErrorCode = extern

  /** Gets the virtual terminal state of a TTY handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/tty.html#c.uv_tty_get_vterm_state LibUv docs]]
    * @group tty_handle
    */
  def uv_tty_get_vterm_state(state: Ptr[TtyVtermState]): ErrorCode = extern

  // =========================================================
  // UDP handle

  /** Callback for sending a UDP message.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/udp.html#c.uv_udp_send_cb LibUv docs]]
    * @group udp_handle
    */
  type UdpSendCallback = CFuncPtr2[UdpSendReq, ErrorCode, Unit]

  /** Callback for receiving a UDP message.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/udp.html#c.uv_udp_recv_cb LibUv docs]]
    * @group udp_handle
    */
  type UdpRecvCallback =
    CFuncPtr5[UdpHandle, CSSize, Buffer, socket.sockaddr, CUnsignedInt, Unit]

  /** Initializes a UDP handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/udp.html#c.uv_udp_init LibUv docs]]
    * @group udp_handle
    */
  def uv_udp_init(loop: Loop, handle: UdpHandle): ErrorCode = extern

  /** Initializes a UDP handle with additional flags.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/udp.html#c.uv_udp_init_ex LibUv docs]]
    * @group udp_handle
    */
  def uv_udp_init_ex(
      loop: Loop,
      handle: UdpHandle,
      flags: CUnsignedInt
  ): ErrorCode =
    extern

    // TODO: what type to use for OsSocketHandle?
//   def uv_udp_open(handle: UdpHandle, sock: OsSocketHandle): ErrorCode = extern

  /** Binds a UDP handle to an address.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/udp.html#c.uv_udp_bind LibUv docs]]
    * @group udp_handle
    */
  def uv_udp_bind(
      handle: UdpHandle,
      addr: socket.sockaddr,
      flags: CUnsignedInt
  ): ErrorCode = extern

  /** Connects a UDP handle to an address.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/udp.html#c.uv_udp_connect LibUv docs]]
    * @group udp_handle
    */
  def uv_udp_connect(
      handle: UdpHandle,
      addr: socket.sockaddr
  ): ErrorCode = extern

  /** Gets the socket address of the peer for a UDP handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/udp.html#c.uv_udp_getpeername LibUv docs]]
    * @group udp_handle
    */
  def uv_udp_getpeername(
      handle: UdpHandle,
      name: socket.sockaddr,
      namelen: Ptr[CInt]
  ): ErrorCode = extern

  /** Gets the socket address bound for a UDP handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/udp.html#c.uv_udp_getsockname LibUv docs]]
    * @group udp_handle
    */
  def uv_udp_getsockname(
      handle: UdpHandle,
      name: socket.sockaddr,
      namelen: Ptr[CInt]
  ): ErrorCode = extern

  /** Sets the membership for a multicast address on a UDP handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/udp.html#c.uv_udp_set_membership LibUv docs]]
    * @group udp_handle
    */
  def uv_udp_set_membership(
      handle: UdpHandle,
      multicastAddress: CString,
      interfaceAddress: CString,
      membership: Membership
  ): ErrorCode = extern

  /** Sets the source membership for a multicast address on a UDP handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/udp.html#c.uv_udp_set_source_membership LibUv docs]]
    * @group udp_handle
    */
  def uv_udp_set_source_membership(
      handle: UdpHandle,
      multicastAddress: CString,
      interfaceAddress: CString,
      sourceAddress: CString,
      membership: Membership
  ): ErrorCode = extern

  /** Sets the multicast loopback status for a UDP handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/udp.html#c.uv_udp_set_multicast_loop LibUv docs]]
    * @group udp_handle
    */
  def uv_udp_set_multicast_loop(handle: UdpHandle, on: CInt): ErrorCode =
    extern

  /** Sets the multicast TTL for a UDP handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/udp.html#c.uv_udp_set_multicast_ttl LibUv docs]]
    * @group udp_handle
    */
  def uv_udp_set_multicast_ttl(handle: UdpHandle, ttl: CInt): ErrorCode =
    extern

  /** Sets the multicast interface for a UDP handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/udp.html#c.uv_udp_set_multicast_interface LibUv docs]]
    * @group udp_handle
    */
  def uv_udp_set_multicast_interface(
      handle: UdpHandle,
      interfaceAddress: CString
  ): ErrorCode = extern

  /** Sets the broadcast status for a UDP handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/udp.html#c.uv_udp_set_broadcast LibUv docs]]
    * @group udp_handle
    */
  def uv_udp_set_broadcast(handle: UdpHandle, on: CInt): ErrorCode = extern

  /** Sets the TTL for a UDP handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/udp.html#c.uv_udp_set_ttl LibUv docs]]
    * @group udp_handle
    */
  def uv_udp_set_ttl(handle: UdpHandle, ttl: CInt): ErrorCode = extern

  /** Sends a UDP message.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/udp.html#c.uv_udp_send LibUv docs]]
    * @group udp_handle
    */
  def uv_udp_send(
      req: UdpSendReq,
      handle: UdpHandle,
      bufs: Buffer,
      numberOfBufs: CUnsignedInt,
      addr: socket.sockaddr,
      sendCb: UdpSendCallback
  ): ErrorCode = extern

  /** Tries to send a UDP message.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/udp.html#c.uv_udp_try_send LibUv docs]]
    * @group udp_handle
    */
  def uv_udp_try_send(
      handle: UdpHandle,
      bufs: Buffer,
      numberOfBufs: CUnsignedInt,
      addr: socket.sockaddr
  ): CInt = extern

  /** Starts reading from a UDP handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/udp.html#c.uv_udp_recv_start LibUv docs]]
    * @group udp_handle
    */
  def uv_udp_recv_start(
      handle: UdpHandle,
      alloc_cb: AllocCallback,
      recv_cb: UdpRecvCallback
  ): ErrorCode = extern

  /** Indicates if the handle is using `UV_UDP_RECVMMSG`.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/udp.html#c.uv_udp_using_recvmmsg LibUv docs]]
    * @group udp_handle
    */
  def uv_udp_using_recvmmsg(handle: UdpHandle): CInt = extern

  /** Stops reading from a UDP handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/udp.html#c.uv_udp_recv_stop LibUv docs]]
    * @group udp_handle
    */
  def uv_udp_recv_stop(handle: UdpHandle): ErrorCode = extern

  /** Gets the send queue size of a UDP handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/udp.html#c.uv_udp_get_send_queue_size LibUv docs]]
    * @group udp_handle
    */
  def uv_udp_get_send_queue_size(handle: UdpHandle): CSize = extern

  /** Gets the send queue count of a UDP handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/udp.html#c.uv_udp_get_send_queue_count LibUv docs]]
    * @group udp_handle
    */
  def uv_udp_get_send_queue_count(handle: UdpHandle): CSize = extern

  // =========================================================
  // FS Event handle

  /** Callback for file system events.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_event_cb LibUv docs]]
    * @group fs_event_handle
    */
  type FsEventCallback = CFuncPtr4[
    FsEventHandle,
    CString,
    CInt,
    CInt,
    Unit
  ]

  /** Initializes a file system event handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_event_init LibUv docs]]`
    * @group fs_event_handle
    */
  def uv_fs_event_init(
      loop: Loop,
      handle: FsEventHandle
  ): ErrorCode = extern

  /** Starts a file system event handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_event_start LibUv docs]]
    * @group fs_event_handle
    */
  def uv_fs_event_start(
      handle: FsEventHandle,
      cb: FsEventCallback,
      path: CString,
      flags: CUnsignedInt
  ): ErrorCode = extern

  /** Stops a file system event handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_event_stop LibUv docs]]
    * @group fs_event_handle
    */
  def uv_fs_event_stop(handle: FsEventHandle): ErrorCode = extern

  /** Gets the path of a file system event handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_event_getpath LibUv docs]]
    * @group fs_event_handle
    */
  def uv_fs_event_getpath(
      handle: FsEventHandle,
      buffer: CString,
      size: Ptr[CSize]
  ): ErrorCode = extern

  // =========================================================
  // FS Poll handle

  /** Callback for file system polls.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_poll_cb LibUv docs]]
    * @group fs_poll_handle
    */
  type FsPollCallback =
    CFuncPtr4[FsPollHandle, ErrorCode, Ptr[Stat], Ptr[Stat], Unit]

  /** Initializes a file system poll handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_poll_init LibUv docs]]
    * @group fs_poll_handle
    */
  def uv_fs_poll_init(
      loop: Loop,
      handle: FsPollHandle
  ): ErrorCode = extern

  /** Starts a file system poll handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_poll_start LibUv docs]]
    * @group fs_poll_handle
    */
  def uv_fs_poll_start(
      handle: FsPollHandle,
      cb: FsPollCallback,
      path: CString,
      interval: CUnsignedInt
  ): ErrorCode = extern

  /** Stops a file system poll handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_poll_stop LibUv docs]]
    * @group fs_poll_handle
    */
  def uv_fs_poll_stop(handle: FsPollHandle): ErrorCode = extern

  /** Gets the path of a file system poll handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_poll_getpath LibUv docs]]
    * @group fs_poll_handle
    */
  def uv_fs_poll_getpath(
      handle: FsPollHandle,
      buffer: CString,
      size: Ptr[CSize]
  ): ErrorCode = extern

  // =========================================================
  // File system operations

  /** Callback for file system requests.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_cb LibUv docs]]
    * @group fs
    */
  type FsCallback = CFuncPtr1[FileReq, Unit]

  /** Cleans up a file system request.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_req_cleanup LibUv docs]]
    * @group fs
    */
  def uv_fs_req_cleanup(req: FileReq): Unit = extern

  /** Closes a file.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_close LibUv docs]]
    * @group fs
    *
    * @param loop
    *   The event loop
    * @param req
    *   The file system request
    * @param file
    *   The file to be closed.
    * @param callback
    *   The callback to be called after the file is closed.
    */
  def uv_fs_close(
      loop: Loop,
      req: FileReq,
      file: FileHandle,
      callback: FsCallback
  ): ErrorCode = extern

  /** Opens a file.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_open LibUv docs]]
    * @group fs
    *
    * @param loop
    *   The event loop
    * @param req
    *   The file system request
    * @param path
    *   The path to the file to be opened
    * @param flags
    *   The flags to be used when opening the file
    * @param mode
    *   The mode to apply when creating a file.
    * @param callback
    *   The callback to be called after the file is opened
    * @return
    *   The file handle, < 0 if an error occurred.
    */
  def uv_fs_open(
      loop: Loop,
      req: FileReq,
      path: CString,
      flags: CInt,
      mode: CInt,
      callback: FsCallback
  ): FileHandle = extern

  /** Reads from a file.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_read LibUv docs]]
    * @group fs
    *
    * @param loop
    *   The event loop
    * @param req
    *   The file system request
    * @param file
    *   The file to be read from
    * @param bufs
    *   The buffer to read into
    * @param numberOfBufs
    *   The number of buffers
    * @param offset
    *   The offset to read from
    * @param callback
    *   The callback to be called after the file is read
    * @return
    *   0 if successful, < 0 if an error occurred.
    */
  def uv_fs_read(
      loop: Loop,
      req: FileReq,
      file: FileHandle,
      bufs: Buffer,
      numberOfBufs: CUnsignedInt,
      offset: Long,
      cb: FsCallback
  ): ErrorCode = extern

  /** Unlinks a file.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_unlink LibUv docs]]
    * @group fs
    */
  def uv_fs_unlink(
      loop: Loop,
      req: FileReq,
      path: CString,
      cb: FsCallback
  ): ErrorCode = extern

  /** Writes to a file.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_write LibUv docs]]
    * @group fs
    *
    * @param loop
    *   The event loop
    * @param req
    *   The file system request
    * @param file
    *   The file to be written to
    * @param bufs
    *   The buffer to write from
    * @param numberOfBufs
    *   The number of buffers
    * @param offset
    *   The offset to write to
    * @param callback
    *   The callback to be called after the file is written
    * @return
    *   0 if successful, < 0 if an error occurred.
    */
  def uv_fs_write(
      loop: Loop,
      req: FileReq,
      file: FileHandle,
      bufs: Buffer,
      numberOfBufs: CUnsignedInt,
      offset: Long,
      cb: FsCallback
  ): ErrorCode = extern

  /** Makes a directory.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_mkdir LibUv docs]]
    * @group fs
    */
  def uv_fs_mkdir(
      loop: Loop,
      req: FileReq,
      path: CString,
      mode: CInt,
      cb: FsCallback
  ): ErrorCode = extern

  /** Makes a temporary directory.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_mkdtemp LibUv docs]]
    * @group fs
    */
  def uv_fs_mkdtemp(
      loop: Loop,
      req: FileReq,
      tpl: CString,
      cb: FsCallback
  ): ErrorCode = extern

  /** Makes a temporary file.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_mkstemp LibUv docs]]
    * @group fs
    */
  def uv_fs_mkstemp(
      loop: Loop,
      req: FileReq,
      tpl: CString,
      cb: FsCallback
  ): ErrorCode = extern

  /** Removes a directory.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_rmdir LibUv docs]]
    * @group fs
    */
  def uv_fs_rmdir(
      loop: Loop,
      req: FileReq,
      path: CString,
      cb: FsCallback
  ): ErrorCode = extern

  /** Opens a directory.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_opendir LibUv docs]]
    * @group fs
    */
  def uv_fs_opendir(
      loop: Loop,
      req: FileReq,
      path: CString,
      cb: FsCallback
  ): ErrorCode = extern

  /** Closes a directory.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_closedir LibUv docs]]
    * @group fs
    */
  def uv_fs_closedir(
      loop: Loop,
      req: FileReq,
      dir: Ptr[Dir],
      cb: FsCallback
  ): ErrorCode = extern

  /** Reads a directory.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_readdir LibUv docs]]
    * @group fs
    */
  def uv_fs_readdir(
      loop: Loop,
      req: FileReq,
      dir: Ptr[Dir],
      cb: FsCallback
  ): ErrorCode = extern

  /** Scans a directory.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_scandir LibUv docs]]
    * @group fs
    */
  def uv_fs_scandir(
      loop: Loop,
      req: FileReq,
      path: CString,
      flags: CInt,
      cb: FsCallback
  ): ErrorCode = extern

  /** Moves to the next item in a directory scan.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_scandir_next LibUv docs]]
    * @group fs
    */
  def uv_fs_scandir_next(
      req: FileReq,
      ent: Ptr[DirEnt]
  ): ErrorCode = extern

  /** Gets the stats of a file.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_stat LibUv docs]]
    * @group fs
    */
  def uv_fs_stat(
      loop: Loop,
      req: FileReq,
      path: CString,
      cb: FsCallback
  ): ErrorCode = extern

  /** Gets the stats of a file.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_fstat LibUv docs]]
    * @group fs
    */
  def uv_fs_fstat(
      loop: Loop,
      req: FileReq,
      file: FileHandle,
      cb: FsCallback
  ): ErrorCode = extern

  /** Gets the stats of a file.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_lstat LibUv docs]]
    * @group fs
    */
  def uv_fs_lstat(
      loop: Loop,
      req: FileReq,
      path: CString,
      cb: FsCallback
  ): ErrorCode = extern

  /** Gets the stats of a file system.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_statfs LibUv docs]]
    * @group fs
    */
  def uv_fs_statfs(
      loop: Loop,
      req: FileReq,
      path: CString,
      cb: FsCallback
  ): ErrorCode = extern

  /** Renames a file.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_rename LibUv docs]]
    * @group fs
    */
  def uv_fs_rename(
      loop: Loop,
      req: FileReq,
      path: CString,
      newPath: CString,
      cb: FsCallback
  ): ErrorCode = extern

  /** Syncs a file to disk.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_fsync LibUv docs]]
    * @group fs
    */
  def uv_fs_fsync(
      loop: Loop,
      req: FileReq,
      file: FileHandle,
      cb: FsCallback
  ): ErrorCode = extern

  /** Syncs a file to disk.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_fdatasync LibUv docs]]
    * @group fs
    */
  def uv_fs_fdatasync(
      loop: Loop,
      req: FileReq,
      file: FileHandle,
      cb: FsCallback
  ): ErrorCode = extern

  /** Truncates a file.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_ftruncate LibUv docs]]
    * @group fs
    */
  def uv_fs_ftruncate(
      loop: Loop,
      req: FileReq,
      file: FileHandle,
      offset: Long,
      cb: FsCallback
  ): ErrorCode = extern

  /** Copies a file.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_copyfile LibUv docs]]
    * @group fs
    */
  def uv_fs_copyfile(
      loop: Loop,
      req: FileReq,
      path: CString,
      newPath: CString,
      flags: CInt,
      cb: FsCallback
  ): ErrorCode = extern

  /** Sends a file to another file handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_sendfile LibUv docs]]
    * @group fs
    */
  def uv_fs_sendfile(
      loop: Loop,
      req: FileReq,
      outFd: FileHandle,
      inFd: FileHandle,
      inOffset: Long,
      length: CSize,
      cb: FsCallback
  ): ErrorCode = extern

  /** Checks access to a file.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_access LibUv docs]]
    * @group fs
    */
  def uv_fs_access(
      loop: Loop,
      req: FileReq,
      path: CString,
      mode: CInt,
      cb: FsCallback
  ): CInt = extern

  /** Changes the permissions of a file.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_chmod LibUv docs]]
    * @group fs
    */
  def uv_fs_chmod(
      loop: Loop,
      req: FileReq,
      path: CString,
      mode: CInt,
      cb: FsCallback
  ): ErrorCode = extern

  /** Changes the permissions of a file.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_fchmod LibUv docs]]
    * @group fs
    */
  def uv_fs_fchmod(
      loop: Loop,
      req: FileReq,
      file: FileHandle,
      mode: CInt,
      cb: FsCallback
  ): ErrorCode = extern

  /** Sets the access and modification times of a file.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_utime LibUv docs]]
    * @group fs
    */
  def uv_fs_utime(
      loop: Loop,
      req: FileReq,
      path: CString,
      atime: CDouble,
      mtime: CDouble,
      cb: FsCallback
  ): ErrorCode = extern

  /** Sets the access and modification times of a file.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_futime LibUv docs]]
    * @group fs
    */
  def uv_fs_futime(
      loop: Loop,
      req: FileReq,
      file: FileHandle,
      atime: CDouble,
      mtime: CDouble,
      cb: FsCallback
  ): ErrorCode = extern

  /** Sets the access and modification times of a file.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_lutime LibUv docs]]
    * @group fs
    */
  def uv_fs_lutime(
      loop: Loop,
      req: FileReq,
      path: CString,
      atime: CDouble,
      mtime: CDouble,
      cb: FsCallback
  ): ErrorCode = extern

  /** Creates a hard link to a file.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_link LibUv docs]]
    * @group fs
    */
  def uv_fs_link(
      loop: Loop,
      req: FileReq,
      path: CString,
      newPath: CString,
      cb: FsCallback
  ): ErrorCode = extern

  /** Creates a symbolic link to a file.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_symlink LibUv docs]]
    * @group fs
    */
  def uv_fs_symlink(
      loop: Loop,
      req: FileReq,
      path: CString,
      newPath: CString,
      flags: CInt,
      cb: FsCallback
  ): ErrorCode = extern

  /** Reads the value of a symbolic link.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_readlink LibUv docs]]
    * @group fs
    */
  def uv_fs_readlink(
      loop: Loop,
      req: FileReq,
      path: CString,
      cb: FsCallback
  ): ErrorCode = extern

  /** Resolves the real path of a file.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_realpath LibUv docs]]
    * @group fs
    */
  def uv_fs_realpath(
      loop: Loop,
      req: FileReq,
      path: CString,
      cb: FsCallback
  ): ErrorCode = extern

//   def uv_fs_chown(
//       loop: Loop,
//       req: FileReq,
//       path: CString,
//       uid: Uid,
//       gid: Guid,
//       cb: FsCallback
//   ): ErrorCode = extern

//   def uv_fs_fchown(
//       loop: Loop,
//       req: FileReq,
//       file: FileHandle,
//       uid: Uid,
//       gid: Guid,
//       cb: FsCallback
//   ): ErrorCode = extern

//   def uv_fs_lchown(
//       loop: Loop,
//       req: FileReq,
//       path: CString,
//       uid: Uid,
//       gid: Guid,
//       cb: FsCallback
//   ): ErrorCode = extern

  /** Gets the type of a file request.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_get_type LibUv docs]]
    * @group fs
    */
  def uv_fs_get_type(req: FileReq): FsType = extern

  /** Gets the result of a file request.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_get_result LibUv docs]]
    * @group fs
    */
  def uv_fs_get_result(req: FileReq): CSSize = extern

  /** Gets the platform specific error code of a file request.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_get_system_error LibUv docs]]
    * @group fs
    */
  def uv_fs_get_system_error(req: FileReq): ErrorCode = extern

  /** Gets the request pointer. What it points at depends on the type of
    * request.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_get_ptr LibUv docs]]
    * @group fs
    */
  def uv_fs_get_ptr(req: FileReq): Ptr[Byte] = extern

  /** Gets the path of a file request.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_get_path LibUv docs]]
    * @group fs
    */
  def uv_fs_get_path(req: FileReq): CString = extern

  /** Gets the stat buffer of a file request.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_fs_get_statbuf LibUv docs]]
    * @group fs
    */
  def uv_fs_get_statbuf(req: FileReq): Ptr[Stat] = extern

  // TODO: what type to use for OsFileHandle?
//   def uv_fs_getosfhandle(fd: CInt): OsFileHandle = extern

//   def uv_open_osfhandle(osfhandle: OsFileHandle): CInt = extern

  // =========================================================
  // Thread pool work scheduling

  /** Callback to perform work on the thread pool.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/threadpool.html#c.uv_work_cb LibUv docs]]
    * @group thread_pool
    */
  type WorkCallback = CFuncPtr1[WorkReq, Unit]

  /** Callback to perform work on the thread pool after the work is done.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/threadpool.html#c.uv_after_work_cb LibUv docs]]
    * @group thread_pool
    */
  type AfterWorkCallback = CFuncPtr2[WorkReq, ErrorCode, Unit]

  /** Enqueues work to be performed by the thread pool.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/threadpool.html#c.uv_queue_work LibUv docs]]
    * @group thread_pool
    */
  def uv_queue_work(
      loop: Loop,
      req: WorkReq,
      work_cb: WorkCallback,
      after_work_cb: AfterWorkCallback
  ): ErrorCode = extern

  // =========================================================
  // DNS utility functions

  /** Callback for getting address info from DNS.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/dns.html#c.uv_getaddrinfo_cb LibUv docs]]
    * @group dns
    */
  type GetAddrInfoCallback =
    CFuncPtr3[GetAddrInfoReq, ErrorCode, netdb.addrinfo, Unit]

  /** Callback for getting name info from DNS.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/dns.html#c.uv_getnameinfo_cb LibUv docs]]
    * @group dns
    */
  type GetNameInfoCallback = CFuncPtr4[
    GetNameInfoReq,
    ErrorCode,
    CString,
    CString,
    Unit
  ]

  /** Get address info from DNS.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/dns.html#c.uv_getaddrinfo LibUv docs]]
    * @group dns
    */
  def uv_getaddrinfo(
      loop: Loop,
      req: GetAddrInfoReq,
      getaddrinfo_cb: GetAddrInfoCallback,
      node: CString,
      service: CString,
      hints: Ptr[netdb.addrinfo]
  ): ErrorCode = extern

  /** Free address info.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/dns.html#c.uv_freeaddrinfo LibUv docs]]
    * @group dns
    */
  def uv_freeaddrinfo(ai: netdb.addrinfo): Unit = extern

  /** Get name info from DNS.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/dns.html#c.uv_getnameinfo LibUv docs]]
    * @group dns
    */
  def uv_getnameinfo(
      loop: Loop,
      req: GetNameInfoReq,
      getnameinfo_cb: GetNameInfoCallback,
      addr: socket.sockaddr,
      flags: CInt
  ): ErrorCode = extern

  // =========================================================
  // Threading and synchronization utilities

  /** Callback for thread operations.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/thread.html#c.uv_thread_cb LibUv docs]]
    * @group thread
    */
  type ThreadCallback = CFuncPtr1[Thread, Unit]

  /** Initialise a mutex handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/thread.html#c.uv_mutex_init LibUv docs]]
    * @group thread
    */
  def uv_mutex_init(mutex: Mutex): ErrorCode = extern

  /** Initialise a recursive mutex handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/thread.html#c.uv_mutex_init_recursive LibUv docs]]
    * @group thread
    */
  def uv_mutex_init_recursive(mutex: Mutex): ErrorCode = extern

  /** Destroy a mutex handle.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/thread.html#c.uv_mutex_destroy LibUv docs]]
    * @group thread
    */
  def uv_mutex_destroy(mutex: Mutex): Unit = extern

  /** Lock a mutex.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/thread.html#c.uv_mutex_lock LibUv docs]]
    * @group thread
    */
  def uv_mutex_lock(mutex: Mutex): Unit = extern

  /** Try to lock a mutex.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/thread.html#c.uv_mutex_trylock LibUv docs]]
    * @group thread
    */
  def uv_mutex_trylock(mutex: Mutex): ErrorCode = extern

  /** Unlock a mutex.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/thread.html#c.uv_mutex_unlock LibUv docs]]
    * @group thread
    */
  def uv_mutex_unlock(mutex: Mutex): Unit = extern

  // =========================================================
  // Miscellaneous utlities

  // TODO: 32bit on Unix, 64bit on Windows
//   type OsSocketHandle = Nothing

  // TODO: 32bit on Unix, 64bit on Windows
//   type OsFileHandle = Nothing

  /** PID type.
    *
    * @group misc
    */
  type Pid = CInt

  /** Time type.
    *
    * @group misc
    */
  type TimeVal = CStruct2[CLong, CLong]

  /** 64-bit time type.
    *
    * @group misc
    */
  type TimeVal64 = CStruct2[Long, Int]

  /** 64-bit time type.
    *
    * @group misc
    */
  type TimeSpec64 = CStruct2[Long, Int]

  /** Gets the error message for the given error code.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/misc.html#c.uv_strerror_r LibUv docs]]
    * @group misc
    */
  def uv_strerror_r(err: CInt, buf: CString, buflen: CSize): CString = extern

  /** Gets the error name for the given error code.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/misc.html#c.uv_err_name_r LibUv docs]]
    * @group misc
    */
  def uv_err_name_r(err: CInt, buf: CString, buflen: CSize): CString = extern

  /** Gets the libuv error code equivalent for a platform error.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/misc.html#c.uv_translate_sys_error LibUv docs]]
    * @group misc
    */
  def uv_translate_sys_error(sys_errno: CInt): CInt = extern

  /** Converts an IPv4 address string to an address structure.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/misc.html#c.uv_ip4_addr LibUv docs]]
    * @group misc
    */
  def uv_ip4_addr(
      ip: CString,
      port: CInt,
      addr: in.sockaddr_in
  ): ErrorCode =
    extern

  /** Converts an IPv6 address string to an address structure.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/misc.html#c.uv_ip6_addr LibUv docs]]
    * @group misc
    */
  def uv_ip6_addr(
      ip: CString,
      port: CInt,
      addr: in.sockaddr_in6
  ): ErrorCode =
    extern

  /** Converts an IPv4 address structure to a string.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/misc.html#c.uv_ip4_name LibUv docs]]
    * @group misc
    */
  def uv_ip4_name(
      src: Ptr[in.sockaddr_in],
      dst: CString,
      size: CSize
  ): ErrorCode = extern

  /** Converts an IPv6 address structure to a string.
    *
    * @see
    *   [[https://docs.libuv.org/en/v1.x/misc.html#c.uv_ip6_name LibUv docs]]
    * @group misc
    */
  def uv_ip6_name(
      src: Ptr[in.sockaddr_in6],
      dst: CString,
      size: CSize
  ): ErrorCode = extern

}
