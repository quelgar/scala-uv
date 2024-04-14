package scalauv

import scala.scalanative.unsafe.*

@extern
private[scalauv] object helpers {

  def scala_uv_fs_req_get_loop(req: FileReq): Loop = extern

  def scala_uv_buf_init(
      base: Ptr[CChar],
      len: CUnsignedInt,
      buffer: Ptr[Byte]
  ): Unit = extern

  def scala_uv_buf_base(buffer: Ptr[Byte]): Ptr[Byte] = extern

  def scala_uv_buf_base_set(buffer: Ptr[Byte], base: Ptr[Byte]): Unit = extern

  def scala_uv_buf_len(buffer: Ptr[Byte]): CSize = extern

  def scala_uv_buf_len_set(buffer: Ptr[Byte], len: CSize): Unit = extern

  def scala_uv_buf_struct_size(): CSize = extern

  def scala_uv_mutex_t_size(): CSize = extern

  def scala_uv_connect_stream_handle(req: ConnectReq): StreamHandle = extern

  def scala_uv_shutdown_stream_handle(req: ShutdownReq): StreamHandle =
    extern

  def scala_uv_write_stream_handle(req: WriteReq): StreamHandle = extern

  def scala_uv_send_stream_handle(req: WriteReq): StreamHandle = extern

  def scala_uv_value_o_append(): CInt = extern

  def scala_uv_value_o_creat(): CInt = extern

  def scala_uv_value_o_direct(): CInt = extern

  def scala_uv_value_o_directory(): CInt = extern

  def scala_uv_value_o_dsync(): CInt = extern

  def scala_uv_value_o_excl(): CInt = extern

  def scala_uv_value_o_exlock(): CInt = extern

  def scala_uv_value_o_filemap(): CInt = extern

  def scala_uv_value_o_noatime(): CInt = extern

  def scala_uv_value_o_noctty(): CInt = extern

  def scala_uv_value_o_nofollow(): CInt = extern

  def scala_uv_value_o_nonblock(): CInt = extern

  def scala_uv_value_o_random(): CInt = extern

  def scala_uv_value_o_rdonly(): CInt = extern

  def scala_uv_value_o_rdwr(): CInt = extern

  def scala_uv_value_o_sequential(): CInt = extern

  def scala_uv_value_o_short_lived(): CInt = extern

  def scala_uv_value_o_symlink(): CInt = extern

  def scala_uv_value_o_sync(): CInt = extern

  def scala_uv_value_o_temporary(): CInt = extern

  def scala_uv_value_o_trunc(): CInt = extern

  def scala_uv_value_o_wronly(): CInt = extern

}
