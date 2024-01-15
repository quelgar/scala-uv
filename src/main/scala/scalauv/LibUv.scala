package scalauv

import scala.scalanative.unsafe.*

@link("uv")
@extern
object LibUv {

  type ErrorCode = CInt

  // =========================================================
  // Event loop

  type Loop = Ptr[Byte]

  type RunMode = CInt

  type WalkCallback = CFuncPtr2[Handle, Ptr[Byte], Unit]

  def uv_loop_init(loop: Loop): ErrorCode = extern

  // TODO: figure out best way to handle varargs
//   def uv_loop_configure(loop: Loop, option: CInt, value: CInt): ErrorCode =
//     extern

  def uv_loop_close(loop: Loop): ErrorCode = extern

  def uv_default_loop(): Loop = extern

  def uv_run(loop: Loop, runMode: RunMode): ErrorCode = extern

  def uv_loop_alive(loop: Loop): CInt = extern

  def uv_stop(loop: Loop): Unit = extern

  def uv_loop_size(): CSize = extern

  def uv_backend_fd(loop: Loop): CInt = extern

  def uv_backend_timeout(loop: Loop): CInt = extern

  def uv_now(loop: Loop): CUnsignedLongLong = extern

  def uv_update_time(loop: Loop): Unit = extern

  def uv_walk(loop: Loop, walkCallback: WalkCallback, arg: Ptr[Byte]): Unit =
    extern

  def uv_loop_fork(loop: Loop): ErrorCode = extern

  def uv_loop_get_data(loop: Loop): Ptr[Byte] = extern

  def uv_loop_set_data(loop: Loop, data: Ptr[Byte]): Unit = extern

  // =========================================================
  // Base handle

  type Handle = Ptr[Byte]

  type HandleType = CInt

  type AllocCallback = CFuncPtr3[Handle, CSize, Buffer, Unit]

  type CloseCallback = CFuncPtr1[Handle, Unit]

  def uv_is_active(handle: Handle): CInt = extern

  def uv_is_closing(handle: Handle): CInt = extern

  def uv_close(handle: Handle, callback: CloseCallback): Unit = extern

  def uv_ref(handle: Handle): Unit = extern

  def uv_unref(handle: Handle): Unit = extern

  def uv_has_ref(handle: Handle): CInt = extern

  def uv_handle_size(handle_type: HandleType): CSize = extern

  def uv_send_buffer_size(handle: Handle, value: Ptr[CInt]): ErrorCode = extern

  def uv_recv_buffer_size(handle: Handle, value: Ptr[CInt]): ErrorCode = extern

  // TODO: the file descriptor type varies between Unix and Windows
//   def uv_fileno(handle: Handle, fd: Ptr[CInt]): ErrorCode = extern

  def uv_handle_get_loop(handle: Handle): Loop = extern

  def uv_handle_get_data(handle: Handle): Ptr[Byte] = extern

  def uv_handle_set_data(handle: Handle, data: Ptr[Byte]): Unit = extern

  def uv_handle_get_type(handle: Handle): HandleType = extern

  def uv_handle_type_name(handleType: HandleType): CString = extern

  // =========================================================
  // Base request

  type RequestType = CInt

  type Req = Ptr[Byte]

  def uv_cancel(req: Req): ErrorCode = extern

  def uv_req_size(req_type: RequestType): CSize = extern

  def uv_req_get_data(req: Req): Ptr[Byte] = extern

  def uv_req_set_data(req: Req, data: Ptr[Byte]): Unit = extern

  def uv_req_get_type(req: Req): RequestType = extern

  def uv_req_type_name(reqType: RequestType): CString = extern

  // =========================================================
  // Timer handle

  type TimerHandle = Handle

  type TimerCallback = CFuncPtr1[TimerHandle, Unit]

  def uv_timer_init(loop: Loop, handle: TimerHandle): ErrorCode = extern

  def uv_timer_start(
      handle: TimerHandle,
      callback: TimerCallback,
      timeoutMillis: CUnsignedLong,
      repeatMillis: CUnsignedLong
  ): ErrorCode = extern

  def uv_timer_stop(handle: TimerHandle): ErrorCode = extern

  def uv_timer_again(handle: TimerHandle): ErrorCode = extern

  def uv_timer_set_repeat(
      handle: TimerHandle,
      repeatMillis: CUnsignedLongLong
  ): Unit =
    extern

  def uv_timer_get_repeat(handle: TimerHandle): CUnsignedLongLong = extern

  def uv_timer_get_due_in(handle: TimerHandle): CUnsignedLongLong = extern

  // =========================================================
  // Prepare handle

  type PrepareHandle = Handle

  type PrepareCallback = CFuncPtr1[PrepareHandle, Unit]

  def uv_prepare_init(loop: Loop, handle: PrepareHandle): ErrorCode = extern

  def uv_prepare_start(
      handle: PrepareHandle,
      callback: PrepareCallback
  ): ErrorCode = extern

  def uv_prepare_stop(handle: PrepareHandle): ErrorCode = extern

  // =========================================================
  // Check handle

  type CheckHandle = Handle

  type CheckCallback = CFuncPtr1[CheckHandle, Unit]

  def uv_check_init(loop: Loop, handle: CheckHandle): ErrorCode = extern

  def uv_check_start(handle: CheckHandle, callback: CheckCallback): ErrorCode =
    extern

  def uv_check_stop(handle: CheckHandle): ErrorCode = extern

  // =========================================================
  // Idle handle

  type IdleHandle = Handle

  type IdleCallback = CFuncPtr1[IdleHandle, Unit]

  def uv_idle_init(loop: Loop, handle: IdleHandle): ErrorCode = extern

  def uv_idle_start(handle: IdleHandle, callback: IdleCallback): ErrorCode =
    extern

  def uv_idle_stop(handle: IdleHandle): ErrorCode = extern

  // =========================================================
  // Async handle

  type AsyncHandle = Handle

  type AsyncCallback = CFuncPtr1[AsyncHandle, Unit]

  def uv_async_init(
      loop: Loop,
      handle: AsyncHandle,
      callback: AsyncCallback
  ): ErrorCode = extern

  def uv_async_send(handle: AsyncHandle): ErrorCode = extern

  // =========================================================
  // Poll handle

  type PollHandle = Handle

  type PollCallback = CFuncPtr3[PollHandle, ErrorCode, CInt, Unit]

  type PollEvent = CInt

  def uv_poll_init(loop: Loop, handle: PollHandle, fd: CInt): ErrorCode =
    extern

    // TODO: what type to use for OsSocketHandle?
//   def uv_poll_init_socket(
//       loop: Loop,
//       handle: PollHandle,
//       socket: OsSocketHandle
//   ): ErrorCode = extern

  def uv_poll_start(
      handle: PollHandle,
      events: CInt,
      callback: PollCallback
  ): ErrorCode = extern

  def uv_poll_stop(handle: PollHandle): ErrorCode = extern

  // =========================================================
  // Signal handle

  type SignalHandle = Handle

  type SignalCallback = CFuncPtr2[SignalHandle, CInt, Unit]

  def uv_signal_init(loop: Loop, handle: SignalHandle): ErrorCode = extern

  def uv_signal_start(
      handle: SignalHandle,
      callback: SignalCallback,
      signum: CInt
  ): ErrorCode = extern

  def uv_signal_start_one_shot(
      handle: SignalHandle,
      callback: SignalCallback,
      signum: CInt
  ): ErrorCode = extern

  def uv_signal_stop(handle: SignalHandle): ErrorCode = extern

  // =========================================================
  // Process handle

//   type ProcessHandle = Handle

//   type ProcessOptions = Ptr[CStruct10[ExitCallback, CString, Ptr[CString], Ptr[
//     CString
//   ], CString, CUnsignedInt, CInt, StdioContainer, Uid, Guid]]

//   type ExitCallback = CFuncPtr3[ProcessHandle, CLongLong, CInt, Unit]

//   type ProcessFlags = CInt

//   type StdioContainer = Ptr[CStruct2[StdioFlags, StreamHandle]]

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

  type StreamHandle = Ptr[Byte]

  type ConnectReq = Req

  type ShutdownReq = Req

  type WriteReq = Req

  type StreamReadCallback = CFuncPtr3[StreamHandle, CSSize, Buffer, Unit]

  type StreamWriteCallback = CFuncPtr2[WriteReq, CInt, Unit]

  type ConnectCallback = CFuncPtr2[ConnectReq, CInt, Unit]

  type ShutdownCallback = CFuncPtr2[Req, CInt, Unit]

  type ConnectionCallback = CFuncPtr2[StreamHandle, ErrorCode, Unit]

  def uv_shutdown(
      req: ShutdownReq,
      handle: StreamHandle,
      cb: ShutdownCallback
  ): ErrorCode = extern

  def uv_listen(
      stream: StreamHandle,
      backlog: CInt,
      cb: ConnectionCallback
  ): ErrorCode = extern

  def uv_accept(
      server: StreamHandle,
      client: StreamHandle
  ): ErrorCode = extern

  def uv_read_start(
      stream: StreamHandle,
      alloc_cb: AllocCallback,
      read_cb: StreamReadCallback
  ): ErrorCode = extern

  def uv_read_stop(stream: StreamHandle): ErrorCode = extern

  def uv_write(
      req: WriteReq,
      handle: StreamHandle,
      bufs: Buffer,
      numberOfBufs: CUnsignedInt,
      cb: StreamWriteCallback
  ): ErrorCode = extern

  def uv_write2(
      req: WriteReq,
      handle: StreamHandle,
      bufs: Buffer,
      numberOfBufs: CUnsignedInt,
      sendHandle: StreamHandle,
      cb: StreamWriteCallback
  ): ErrorCode = extern

  def uv_try_write(
      handle: StreamHandle,
      bufs: Buffer,
      numberOfBufs: CUnsignedInt
  ): CInt = extern

  def uv_try_write2(
      handle: StreamHandle,
      bufs: Buffer,
      numberOfBufs: CUnsignedInt,
      sendHandle: StreamHandle
  ): CInt = extern

  def uv_is_readable(handle: StreamHandle): CInt = extern

  def uv_is_writable(handle: StreamHandle): CInt = extern

  def uv_stream_set_blocking(handle: StreamHandle, blocking: CInt): CInt =
    extern

  def uv_stream_get_write_queue_size(handle: StreamHandle): CSize = extern

  // =========================================================
  // TCP handle

  type TcpHandle = Handle

  def uv_tcp_init(loop: Loop, handle: TcpHandle): ErrorCode = extern

  def uv_tcp_init_ex(
      loop: Loop,
      handle: TcpHandle,
      flags: CUnsignedInt
  ): ErrorCode = extern

  def uv_tcp_open(handle: TcpHandle, sock: CInt): ErrorCode = extern

  def uv_tcp_no_delay(handle: TcpHandle, enable: CInt): ErrorCode = extern

  def uv_tcp_keepalive(
      handle: TcpHandle,
      enable: CInt,
      delay: CUnsignedInt
  ): ErrorCode = extern

  def uv_tcp_simultaneous_accepts(handle: TcpHandle, enable: CInt): ErrorCode =
    extern

  def uv_tcp_bind(
      handle: TcpHandle,
      addr: SocketAddress,
      flags: CUnsignedInt
  ): ErrorCode = extern

  def uv_tcp_getsockname(
      handle: TcpHandle,
      name: SocketAddress,
      namelen: Ptr[CInt]
  ): ErrorCode = extern

  def uv_tcp_getpeername(
      handle: TcpHandle,
      name: SocketAddress,
      namelen: Ptr[CInt]
  ): ErrorCode = extern

  def uv_tcp_connect(
      req: Req,
      handle: TcpHandle,
      addr: SocketAddress,
      cb: ConnectCallback
  ): ErrorCode = extern

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

  type PipeHandle = Handle

  def uv_pipe_init(
      loop: Loop,
      handle: PipeHandle,
      ipc: CInt
  ): ErrorCode = extern

  def uv_pipe_open(
      handle: PipeHandle,
      file: FileHandle
  ): ErrorCode = extern

  def uv_pipe_bind(
      handle: PipeHandle,
      name: CString
  ): ErrorCode = extern

  def uv_pipe_bind2(
      handle: PipeHandle,
      name: CString,
      nameLength: CSize,
      flags: CUnsignedInt
  ): ErrorCode = extern

  def uv_pipe_connect(
      req: ConnectReq,
      handle: PipeHandle,
      name: CString,
      cb: ConnectCallback
  ): Unit = extern

  def uv_pipe_connect2(
      req: ConnectReq,
      handle: PipeHandle,
      name: CString,
      nameLength: CSize,
      flags: CUnsignedInt,
      cb: ConnectCallback
  ): Unit = extern

  def uv_pipe_getsockname(
      handle: PipeHandle,
      buffer: CString,
      nameLength: Ptr[CSize]
  ): ErrorCode = extern

  def uv_pipe_getpeername(
      handle: PipeHandle,
      buffer: CString,
      nameLength: Ptr[CSize]
  ): ErrorCode = extern

  def uv_pipe_pending_instances(
      handle: PipeHandle,
      count: CInt
  ): Unit = extern

  def uv_pipe_pending_count(handle: PipeHandle): CSize = extern

  def uv_pipe_pending_type(handle: PipeHandle): HandleType = extern

  def uv_pipe_chmod(handle: PipeHandle, flags: CInt): ErrorCode = extern

  def uv_pipe(
      fileHandles: Ptr[FileHandle],
      readFlags: CInt,
      writeFlags: CInt
  ): ErrorCode = extern

  // =========================================================
  // TTY handle

  type TtyHandle = Handle

  type TtyMode = CInt

  type TtyVtermState = CInt

  def uv_tty_init(
      loop: Loop,
      handle: TtyHandle,
      fd: FileHandle,
      unused: CInt
  ): ErrorCode = extern

  def uv_tty_set_mode(handle: TtyHandle, mode: TtyMode): ErrorCode = extern

  def uv_tty_reset_mode(): ErrorCode = extern

  def uv_tty_get_winsize(
      handle: TtyHandle,
      width: Ptr[CInt],
      height: Ptr[CInt]
  ): ErrorCode = extern

  def uv_tty_set_vterm_state(state: TtyVtermState): ErrorCode = extern

  def uv_tty_get_vterm_state(state: Ptr[TtyVtermState]): ErrorCode = extern

  // =========================================================
  // UDP handle

  type UdpHandle = Handle

  type UdpSendReq = Req

  type UdpFlags = CInt

  type UdpSendCallback = CFuncPtr2[UdpSendReq, ErrorCode, Unit]

  type UdpRecvCallback =
    CFuncPtr5[UdpHandle, CSSize, Buffer, SocketAddress, CUnsignedInt, Unit]

  type Membership = CInt

  def uv_udp_init(loop: Loop, handle: UdpHandle): ErrorCode = extern

  def uv_udp_init_ex(
      loop: Loop,
      handle: UdpHandle,
      flags: CUnsignedInt
  ): ErrorCode =
    extern

    // TODO: what type to use for OsSocketHandle?
//   def uv_udp_open(handle: UdpHandle, sock: OsSocketHandle): ErrorCode = extern

  def uv_udp_bind(
      handle: UdpHandle,
      addr: SocketAddress,
      flags: CUnsignedInt
  ): ErrorCode = extern

  def uv_udp_connect(
      handle: UdpHandle,
      addr: SocketAddress
  ): ErrorCode = extern

  def uv_udp_getpeername(
      handle: UdpHandle,
      name: SocketAddress,
      namelen: Ptr[CInt]
  ): ErrorCode = extern

  def uv_udp_getsockname(
      handle: UdpHandle,
      name: SocketAddress,
      namelen: Ptr[CInt]
  ): ErrorCode = extern

  def uv_udp_set_membership(
      handle: UdpHandle,
      multicastAddress: CString,
      interfaceAddress: CString,
      membership: Membership
  ): ErrorCode = extern

  def uv_udp_set_source_membership(
      handle: UdpHandle,
      multicastAddress: CString,
      interfaceAddress: CString,
      sourceAddress: CString,
      membership: Membership
  ): ErrorCode = extern

  def uv_udp_set_multicast_loop(handle: UdpHandle, on: CInt): ErrorCode =
    extern

  def uv_udp_set_multicast_ttl(handle: UdpHandle, ttl: CInt): ErrorCode =
    extern

  def uv_udp_set_multicast_interface(
      handle: UdpHandle,
      interfaceAddress: CString
  ): ErrorCode = extern

  def uv_udp_set_broadcast(handle: UdpHandle, on: CInt): ErrorCode = extern

  def uv_udp_set_ttl(handle: UdpHandle, ttl: CInt): ErrorCode = extern

  def uv_udp_send(
      req: UdpSendReq,
      handle: UdpHandle,
      bufs: Buffer,
      numberOfBufs: CUnsignedInt,
      addr: SocketAddress,
      sendCb: UdpSendCallback
  ): ErrorCode = extern

  def uv_udp_try_send(
      handle: UdpHandle,
      bufs: Buffer,
      numberOfBufs: CUnsignedInt,
      addr: SocketAddress
  ): CInt = extern

  def uv_udp_recv_start(
      handle: UdpHandle,
      alloc_cb: AllocCallback,
      recv_cb: UdpRecvCallback
  ): ErrorCode = extern

  def uv_udp_using_recvmmsg(handle: UdpHandle): CInt = extern

  def uv_udp_recv_stop(handle: UdpHandle): ErrorCode = extern

  def uv_udp_get_send_queue_size(handle: UdpHandle): CSize = extern

  def uv_udp_get_send_queue_count(handle: UdpHandle): CSize = extern

  // =========================================================
  // FS Event handle

  type FsEventHandle = Handle

  type FsEventCallback = CFuncPtr4[
    FsEventHandle,
    CString,
    CInt,
    CInt,
    Unit
  ]

  type FsEvent = CInt

  type FsEventFlags = CInt

  def uv_fs_event_init(
      loop: Loop,
      handle: FsEventHandle
  ): ErrorCode = extern

  def uv_fs_event_start(
      handle: FsEventHandle,
      cb: FsEventCallback,
      path: CString,
      flags: CUnsignedInt
  ): ErrorCode = extern

  def uv_fs_event_stop(handle: FsEventHandle): ErrorCode = extern

  def uv_fs_event_getpath(
      handle: FsEventHandle,
      buffer: CString,
      size: Ptr[CSize]
  ): ErrorCode = extern

  // =========================================================
  // FS Poll handle

  type FsPollHandle = Handle

  type FsPollCallback = CFuncPtr4[FsPollHandle, ErrorCode, Stat, Stat, Unit]

  def uv_fs_poll_init(
      loop: Loop,
      handle: FsPollHandle
  ): ErrorCode = extern

  def uv_fs_poll_start(
      handle: FsPollHandle,
      cb: FsPollCallback,
      path: CString,
      interval: CUnsignedInt
  ): ErrorCode = extern

  def uv_fs_poll_stop(handle: FsPollHandle): ErrorCode = extern

  def uv_fs_poll_getpath(
      handle: FsPollHandle,
      buffer: CString,
      size: Ptr[CSize]
  ): ErrorCode = extern

  // =========================================================
  // File system operations

  type FileReq = Req

  type TimeSpec = Ptr[CStruct2[CLong, CLong]]

  type Stat = Ptr[CStruct16[
    CUnsignedLongLong,
    CUnsignedLongLong,
    CUnsignedLongLong,
    CUnsignedLongLong,
    CUnsignedLongLong,
    CUnsignedLongLong,
    CUnsignedLongLong,
    CUnsignedLongLong,
    CUnsignedLongLong,
    CUnsignedLongLong,
    CUnsignedLongLong,
    CUnsignedLongLong,
    TimeSpec,
    TimeSpec,
    TimeSpec,
    TimeSpec
  ]]

  type FsType = CInt

  type StatFs = Ptr[CStruct11[
    CUnsignedLongLong,
    CUnsignedLongLong,
    CUnsignedLongLong,
    CUnsignedLongLong,
    CUnsignedLongLong,
    CUnsignedLongLong,
    CUnsignedLongLong,
    CUnsignedLongLong,
    CUnsignedLongLong,
    CUnsignedLongLong,
    CUnsignedLongLong
  ]]

  type DirEntType = CInt

  type DirEnt = Ptr[CStruct2[CString, DirEntType]]

  type Dir = Ptr[CStruct2[DirEnt, CSize]]

  type FsCallback = CFuncPtr1[Req, Unit]

  def uv_fs_req_cleanup(req: FileReq): Unit = extern

  def uv_fs_close(
      loop: Loop,
      req: FileReq,
      file: FileHandle,
      callback: FsCallback
  ): ErrorCode = extern

  def uv_fs_open(
      loop: Loop,
      req: FileReq,
      path: CString,
      flags: CInt,
      mode: CInt,
      callback: FsCallback
  ): FileHandle = extern

  def uv_fs_read(
      loop: Loop,
      req: FileReq,
      file: FileHandle,
      bufs: Buffer,
      numberOfBufs: CUnsignedInt,
      offset: Long,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_unlink(
      loop: Loop,
      req: FileReq,
      path: CString,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_write(
      loop: Loop,
      req: FileReq,
      file: FileHandle,
      bufs: Buffer,
      numberOfBufs: CUnsignedInt,
      offset: Long,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_mkdir(
      loop: Loop,
      req: FileReq,
      path: CString,
      mode: CInt,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_mkdtemp(
      loop: Loop,
      req: FileReq,
      tpl: CString,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_mkstemp(
      loop: Loop,
      req: FileReq,
      tpl: CString,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_rmdir(
      loop: Loop,
      req: FileReq,
      path: CString,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_opendir(
      loop: Loop,
      req: FileReq,
      path: CString,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_closedir(
      loop: Loop,
      req: FileReq,
      dir: Dir,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_readdir(
      loop: Loop,
      req: FileReq,
      dir: Dir,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_scandir(
      loop: Loop,
      req: FileReq,
      path: CString,
      flags: CInt,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_scandir_next(
      req: FileReq,
      ent: Ptr[DirEnt]
  ): ErrorCode = extern

  def uv_fs_stat(
      loop: Loop,
      req: FileReq,
      path: CString,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_fstat(
      loop: Loop,
      req: FileReq,
      file: FileHandle,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_lstat(
      loop: Loop,
      req: FileReq,
      path: CString,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_statfs(
      loop: Loop,
      req: FileReq,
      path: CString,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_rename(
      loop: Loop,
      req: FileReq,
      path: CString,
      newPath: CString,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_fsync(
      loop: Loop,
      req: FileReq,
      file: FileHandle,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_fdatasync(
      loop: Loop,
      req: FileReq,
      file: FileHandle,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_ftruncate(
      loop: Loop,
      req: FileReq,
      file: FileHandle,
      offset: Long,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_copyfile(
      loop: Loop,
      req: FileReq,
      path: CString,
      newPath: CString,
      flags: CInt,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_sendfile(
      loop: Loop,
      req: FileReq,
      outFd: FileHandle,
      inFd: FileHandle,
      inOffset: Long,
      length: CSize,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_access(
      loop: Loop,
      req: FileReq,
      path: CString,
      mode: CInt,
      cb: FsCallback
  ): CInt = extern

  def uv_fs_chmod(
      loop: Loop,
      req: FileReq,
      path: CString,
      mode: CInt,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_fchmod(
      loop: Loop,
      req: FileReq,
      file: FileHandle,
      mode: CInt,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_utime(
      loop: Loop,
      req: FileReq,
      path: CString,
      atime: CDouble,
      mtime: CDouble,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_futime(
      loop: Loop,
      req: FileReq,
      file: FileHandle,
      atime: CDouble,
      mtime: CDouble,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_lutime(
      loop: Loop,
      req: FileReq,
      path: CString,
      atime: CDouble,
      mtime: CDouble,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_link(
      loop: Loop,
      req: FileReq,
      path: CString,
      newPath: CString,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_symlink(
      loop: Loop,
      req: FileReq,
      path: CString,
      newPath: CString,
      flags: CInt,
      cb: FsCallback
  ): ErrorCode = extern

  def uv_fs_readlink(
      loop: Loop,
      req: FileReq,
      path: CString,
      cb: FsCallback
  ): ErrorCode = extern

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

  def uv_fs_get_type(req: FileReq): FsType = extern

  def uv_fs_get_result(req: FileReq): CSSize = extern

  def uv_fs_get_system_error(req: FileReq): ErrorCode = extern

  def uv_fs_get_ptr(req: FileReq): Ptr[Byte] = extern

  def uv_fs_get_path(req: FileReq): CString = extern

  def uv_fs_get_statbuf(req: FileReq): Stat = extern

  // TODO: what type to use for OsFileHandle?
//   def uv_fs_getosfhandle(fd: CInt): OsFileHandle = extern

//   def uv_open_osfhandle(osfhandle: OsFileHandle): CInt = extern

  // =========================================================
  // Thread pool work scheduling

  type Work = Req

  type WorkCallback = CFuncPtr1[Work, Unit]

  type AfterWorkCallback = CFuncPtr2[Work, ErrorCode, Unit]

  def uv_queue_work(
      loop: Loop,
      req: Work,
      work_cb: WorkCallback,
      after_work_cb: AfterWorkCallback
  ): ErrorCode = extern

  // =========================================================
  // DNS utility functions

  type GetAddrInfo = Ptr[Byte]

  type GetAddrInfoCallback = CFuncPtr3[GetAddrInfo, ErrorCode, AddrInfo, Unit]

  type GetNameInfo = Ptr[Byte]

  type GetNameInfoCallback = CFuncPtr4[
    GetNameInfo,
    ErrorCode,
    CString,
    CString,
    Unit
  ]

  def uv_getaddrinfo(
      loop: Loop,
      req: GetAddrInfo,
      getaddrinfo_cb: GetAddrInfoCallback,
      node: CString,
      service: CString,
      hints: Ptr[AddrInfo]
  ): ErrorCode = extern

  def uv_freeaddrinfo(ai: AddrInfo): Unit = extern

  def uv_getnameinfo(
      loop: Loop,
      req: GetNameInfo,
      getnameinfo_cb: GetNameInfoCallback,
      addr: SocketAddress,
      flags: CInt
  ): ErrorCode = extern

  // =========================================================
  // Threading and synchronization utilities

  type Thread = Ptr[Byte]

  type ThreadCallback = CFuncPtr1[Thread, Unit]

  type ThreadLocalKey = Ptr[Byte]

  type OnceOnly = Ptr[Byte]

  type Mutex = Ptr[Byte]

  type ReadWriteLock = Ptr[Byte]

  type Semaphore = Ptr[Byte]

  type Condition = Ptr[Byte]

  type Barrier = Ptr[Byte]

  def uv_mutex_init(mutex: Mutex): ErrorCode = extern

  def uv_mutex_init_recursive(mutex: Mutex): ErrorCode = extern

  def uv_mutex_destroy(mutex: Mutex): Unit = extern

  def uv_mutex_lock(mutex: Mutex): Unit = extern

  def uv_mutex_trylock(mutex: Mutex): ErrorCode = extern

  def uv_mutex_unlock(mutex: Mutex): Unit = extern

  // =========================================================
  // Miscellaneous utlities

  type FileHandle = CInt

  // TODO: 32bit on Unix, 64bit on Windows
//   type OsSocketHandle = Nothing

  // TODO: 32bit on Unix, 64bit on Windows
//   type OsFileHandle = Nothing

  type Pid = CInt

  type TimeVal = Ptr[CStruct2[CLong, CLong]]

  type TimeVal64 = Ptr[CStruct2[Long, Int]]

  type TimeSpec64 = Ptr[CStruct2[Long, Int]]

  type Clock = CInt

  def uv_strerror_r(err: CInt, buf: CString, buflen: CSize): CString = extern

  def uv_err_name_r(err: CInt, buf: CString, buflen: CSize): CString = extern

  def uv_translate_sys_error(sys_errno: CInt): CInt = extern

  def uv_ip4_addr(
      ip: CString,
      port: CInt,
      addr: SocketAddressIp4
  ): ErrorCode =
    extern

  def uv_ip6_addr(
      ip: CString,
      port: CInt,
      addr: SocketAddressIp6
  ): ErrorCode =
    extern

  def uv_ip4_name(
      src: Ptr[SocketAddressIp4],
      dst: CString,
      size: CSize
  ): ErrorCode = extern

  def uv_ip6_name(
      src: Ptr[SocketAddressIp6],
      dst: CString,
      size: CSize
  ): ErrorCode = extern

}

@extern
private[scalauv] object helpers {

  def uv_scala_buf_init(
      base: Ptr[CChar],
      len: CUnsignedInt,
      buffer: Ptr[Byte]
  ): Unit = extern

  def uv_scala_buf_base(buffer: Ptr[Byte]): Ptr[Byte] = extern

  def uv_scala_buf_len(buffer: Ptr[Byte]): CSize = extern

  def uv_scala_buf_struct_size(): CSize = extern

  def uv_scala_mutex_t_size(): CSize = extern

  def uv_scala_connect_stream_handle(
      req: LibUv.ConnectReq
  ): LibUv.StreamHandle =
    extern

  def uv_scala_shutdown_stream_handle(req: LibUv.Req): LibUv.StreamHandle =
    extern

  def uv_scala_write_stream_handle(req: LibUv.Req): LibUv.StreamHandle = extern

  def uv_scala_send_stream_handle(req: LibUv.Req): LibUv.StreamHandle = extern

  def uv_scala_sizeof_sockaddr_in(): CSize = extern

  def uv_scala_init_sockaddr_in(
      address: CInt,
      port: CInt,
      socketAddress: SocketAddressIp4
  ): Unit =
    extern

  def uv_scala_sizeof_sockaddr_in6(): CSize = extern

  def uv_scala_init_sockaddr_in6(
      address: Ptr[Byte],
      port: CInt,
      flowInfo: CUnsignedInt,
      scopeId: CUnsignedInt,
      socketAddress: SocketAddressIp6
  ): Unit =
    extern

}
