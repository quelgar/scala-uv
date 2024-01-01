package scalauv

import scala.scalanative.unsafe.*

@link("uv")
@extern
object LibUv {

  type Loop = Ptr[Byte]

  type Handle = Ptr[Byte]

  type StreamHandle = Ptr[Byte]

  type TcpHandle = Handle

  type FileHandle = CInt

  type AsyncHandle = Handle

  type TimerHandle = Handle

  type PipeHandle = Handle

  type CloseCallback = CFuncPtr1[Handle, Unit]

  type AsyncCallback = CFuncPtr1[AsyncHandle, Unit]

  type TimerCallback = CFuncPtr1[TimerHandle, Unit]

  type FsCallback = CFuncPtr1[Req, Unit]

  type AllocCallback = CFuncPtr3[Handle, CSize, Buffer, Unit]

  type StreamReadCallback = CFuncPtr3[StreamHandle, CSSize, Buffer, Unit]

  type StreamWriteCallback = CFuncPtr2[WriteReq, CInt, Unit]

  type ConnectCallback = CFuncPtr2[ConnectReq, CInt, Unit]

  type ShutdownCallback = CFuncPtr2[Req, CInt, Unit]

  type ConnectionCallback = CFuncPtr2[StreamHandle, ErrorCode, Unit]

  type Req = Ptr[Byte]

  type ConnectReq = Req

  type ShutdownReq = Req

  type WriteReq = Req

  type FileReq = Req

  type ErrorCode = CInt

  type RequestType = CInt

  type HandleType = CInt

  type RunMode = CInt

  type Thread = Ptr[Byte]

  type ThreadCallback = CFuncPtr1[Thread, Unit]

  type ThreadLocalKey = Ptr[Byte]

  type OnceOnly = Ptr[Byte]

  type Mutex = Ptr[Byte]

  type ReadWriteLock = Ptr[Byte]

  type Semaphore = Ptr[Byte]

  type Condition = Ptr[Byte]

  type Barrier = Ptr[Byte]

  type SocketHandle = CInt

  // =========================================================
  // Basics

  def uv_strerror_r(err: CInt, buf: CString, buflen: CSize): CString = extern

  def uv_err_name_r(err: CInt, buf: CString, buflen: CSize): CString = extern

  def uv_translate_sys_error(sys_errno: CInt): CInt = extern

  def uv_default_loop(): Loop = extern

  def uv_run(loop: Loop, runMode: RunMode): ErrorCode = extern

  def uv_loop_close(loop: Loop): ErrorCode = extern

  def uv_loop_size(): CSize = extern

  def uv_loop_init(loop: Loop): ErrorCode = extern

//   def uv_loop_configure(loop: Loop, option: CInt, value: CInt): ErrorCode =
//     extern

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

  // =========================================================
  // Handles

  def uv_handle_size(handle_type: HandleType): CSize = extern

  def uv_handle_get_loop(handle: Handle): Loop = extern

  def uv_handle_get_data(handle: Handle): Ptr[Byte] = extern

  def uv_handle_set_data(handle: Handle, data: Ptr[Byte]): Unit = extern

  def uv_handle_get_type(handle: Handle): HandleType = extern

  def uv_handle_type_name(handleType: HandleType): CString = extern

  def uv_is_active(handle: Handle): CInt = extern

  def uv_is_closing(handle: Handle): CInt = extern

  def uv_close(handle: Handle, callback: CloseCallback): Unit = extern

  def uv_ref(handle: Handle): Unit = extern

  def uv_unref(handle: Handle): Unit = extern

  def uv_has_ref(handle: Handle): CInt = extern

  // =========================================================
  // Requests

  def uv_req_size(req_type: RequestType): CSize = extern

  def uv_req_get_data(req: Req): Ptr[Byte] = extern

  def uv_req_set_data(req: Req, data: Ptr[Byte]): Unit = extern

  def uv_req_get_type(req: Req): RequestType = extern

  def uv_req_type_name(reqType: RequestType): CString = extern

  // =========================================================
  // Async & Timers

  def uv_async_init(
      loop: Loop,
      handle: AsyncHandle,
      callback: AsyncCallback
  ): ErrorCode = extern

  def uv_async_send(handle: AsyncHandle): ErrorCode = extern

  def uv_timer_init(loop: Loop, handle: TimerHandle): ErrorCode = extern

  def uv_timer_start(
      handle: TimerHandle,
      callback: TimerCallback,
      timeoutMillis: CUnsignedLong,
      repeatMillis: CUnsignedLong
  ): ErrorCode = extern

  // =========================================================
  // Files

  def uv_fs_open(
      loop: Loop,
      req: FileReq,
      path: CString,
      flags: CInt,
      mode: CInt,
      callback: FsCallback
  ): FileHandle = extern

  def uv_fs_close(
      loop: Loop,
      req: FileReq,
      file: FileHandle,
      callback: FsCallback
  ): ErrorCode = extern

  def uv_fs_req_cleanup(req: FileReq): Unit = extern

  def uv_fs_read(
      loop: Loop,
      req: FileReq,
      file: FileHandle,
      bufs: Buffer,
      numberOfBufs: CUnsignedInt,
      offset: Long,
      cb: FsCallback
  ): CInt = extern

  def uv_fs_write(
      loop: Loop,
      req: FileReq,
      file: FileHandle,
      bufs: Buffer,
      numberOfBufs: CUnsignedInt,
      offset: Long,
      cb: FsCallback
  ): CInt = extern

  def uv_fs_access(
      loop: Loop,
      req: FileReq,
      path: CString,
      mode: CInt,
      cb: FsCallback
  ): CInt = extern

  // =========================================================
  // Streams

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
  // TCP

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
      addr: SocketAddressIp4,
      flags: CUnsignedInt
  ): ErrorCode = extern

  def uv_tcp_getsockname(
      handle: TcpHandle,
      name: SocketAddressIp4,
      namelen: Ptr[CInt]
  ): ErrorCode = extern

  def uv_tcp_getpeername(
      handle: TcpHandle,
      name: SocketAddressIp4,
      namelen: Ptr[CInt]
  ): ErrorCode = extern

  def uv_tcp_connect(
      req: Req,
      handle: TcpHandle,
      addr: SocketAddressIp4,
      cb: ConnectCallback
  ): ErrorCode = extern

  def uv_tcp_close_reset(handle: TcpHandle, cb: CloseCallback): ErrorCode =
    extern

  def uv_socketpair(
      socketType: CInt,
      protocol: CInt,
      socketVector: Ptr[CArray[SocketHandle, Nat._2]],
      flags0: CInt,
      flags1: CInt
  ): ErrorCode = extern

  // =========================================================
  // Pipes

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
  // Mutexes

  def uv_mutex_init(mutex: Mutex): ErrorCode = extern

  def uv_mutex_init_recursive(mutex: Mutex): ErrorCode = extern

  def uv_mutex_destroy(mutex: Mutex): Unit = extern

  def uv_mutex_lock(mutex: Mutex): Unit = extern

  def uv_mutex_trylock(mutex: Mutex): ErrorCode = extern

  def uv_mutex_unlock(mutex: Mutex): Unit = extern

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
