package scalauv

import scala.scalanative.unsafe.*

@extern
private[scalauv] object helpers {

  def scala_uv_buf_init(
      base: Ptr[CChar],
      len: CUnsignedInt,
      buffer: Ptr[Byte]
  ): Unit = extern

  def scala_uv_buf_base(buffer: Ptr[Byte]): Ptr[Byte] = extern

  def scala_uv_buf_len(buffer: Ptr[Byte]): CSize = extern

  def scala_uv_buf_struct_size(): CSize = extern

  def scala_uv_mutex_t_size(): CSize = extern

  def scala_uv_connect_stream_handle(req: ConnectReq): StreamHandle = extern

  def scala_uv_shutdown_stream_handle(req: Req): StreamHandle =
    extern

  def scala_uv_write_stream_handle(req: Req): StreamHandle = extern

  def scala_uv_send_stream_handle(req: Req): StreamHandle = extern

  def scala_uv_sizeof_sockaddr_in(): CSize = extern

  def scala_uv_init_sockaddr_in(
      address: CInt,
      port: CInt,
      socketAddress: SocketAddressIp4
  ): Unit =
    extern

  def scala_uv_sizeof_sockaddr_in6(): CSize = extern

  def scala_uv_init_sockaddr_in6(
      address: Ptr[Byte],
      port: CInt,
      flowInfo: CUnsignedInt,
      scopeId: CUnsignedInt,
      socketAddress: SocketAddressIp6
  ): Unit =
    extern

  def scala_uv_value_o_rdonly(): CInt = extern

  def scala_uv_value_o_wronly(): CInt = extern

  def scala_uv_value_o_rdwr(): CInt = extern

  def scala_uv_value_o_creat(): CInt = extern

  def scala_uv_value_o_excl(): CInt = extern

  def scala_uv_value_o_trunc(): CInt = extern

  def scala_uv_value_o_append(): CInt = extern

  def scala_uv_value_o_dsync(): CInt = extern

  def scala_uv_value_o_sync(): CInt = extern
}
