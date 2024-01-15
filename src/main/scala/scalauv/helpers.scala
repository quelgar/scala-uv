package scalauv

import scala.scalanative.unsafe.*

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

  def uv_scala_connect_stream_handle(req: ConnectReq): StreamHandle = extern

  def uv_scala_shutdown_stream_handle(req: Req): StreamHandle =
    extern

  def uv_scala_write_stream_handle(req: Req): StreamHandle = extern

  def uv_scala_send_stream_handle(req: Req): StreamHandle = extern

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
