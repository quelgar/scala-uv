package scalauv

import scala.scalanative.unsafe.*

/** Type alias for native C integer, which is returned by all libuv functions
  * that can fail to indicate the error. If the result is < 0, then it is an
  * error code.
  */
type ErrorCode = CInt

/** All possible error codes that libuv functions can return.
  */
object ErrorCodes {

  import errors.*

  val E2BIG: ErrorCode = uv_scala_errorcode_E2BIG()
  val EACCES: ErrorCode = uv_scala_errorcode_EACCES()
  val EADDRINUSE: ErrorCode = uv_scala_errorcode_EADDRINUSE()
  val EADDRNOTAVAIL: ErrorCode = uv_scala_errorcode_EADDRNOTAVAIL()
  val EAFNOSUPPORT: ErrorCode = uv_scala_errorcode_EAFNOSUPPORT()
  val EAGAIN: ErrorCode = uv_scala_errorcode_EAGAIN()
  val EAI_ADDRFAMILY: ErrorCode = uv_scala_errorcode_EAI_ADDRFAMILY()
  val EAI_AGAIN: ErrorCode = uv_scala_errorcode_EAI_AGAIN()
  val EAI_BADFLAGS: ErrorCode = uv_scala_errorcode_EAI_BADFLAGS()
  val EAI_BADHINTS: ErrorCode = uv_scala_errorcode_EAI_BADHINTS()
  val EAI_CANCELED: ErrorCode = uv_scala_errorcode_EAI_CANCELED()
  val EAI_FAIL: ErrorCode = uv_scala_errorcode_EAI_FAIL()
  val EAI_FAMILY: ErrorCode = uv_scala_errorcode_EAI_FAMILY()
  val EAI_MEMORY: ErrorCode = uv_scala_errorcode_EAI_MEMORY()
  val EAI_NODATA: ErrorCode = uv_scala_errorcode_EAI_NODATA()
  val EAI_NONAME: ErrorCode = uv_scala_errorcode_EAI_NONAME()
  val EAI_OVERFLOW: ErrorCode = uv_scala_errorcode_EAI_OVERFLOW()
  val EAI_PROTOCOL: ErrorCode = uv_scala_errorcode_EAI_PROTOCOL()
  val EAI_SERVICE: ErrorCode = uv_scala_errorcode_EAI_SERVICE()
  val EAI_SOCKTYPE: ErrorCode = uv_scala_errorcode_EAI_SOCKTYPE()
  val EALREADY: ErrorCode = uv_scala_errorcode_EALREADY()
  val EBADF: ErrorCode = uv_scala_errorcode_EBADF()
  val EBUSY: ErrorCode = uv_scala_errorcode_EBUSY()
  val ECANCELED: ErrorCode = uv_scala_errorcode_ECANCELED()
  val ECHARSET: ErrorCode = uv_scala_errorcode_ECHARSET()
  val ECONNABORTED: ErrorCode = uv_scala_errorcode_ECONNABORTED()
  val ECONNREFUSED: ErrorCode = uv_scala_errorcode_ECONNREFUSED()
  val ECONNRESET: ErrorCode = uv_scala_errorcode_ECONNRESET()
  val EDESTADDRREQ: ErrorCode = uv_scala_errorcode_EDESTADDRREQ()
  val EEXIST: ErrorCode = uv_scala_errorcode_EEXIST()
  val EFAULT: ErrorCode = uv_scala_errorcode_EFAULT()
  val EFBIG: ErrorCode = uv_scala_errorcode_EFBIG()
  val EHOSTUNREACH: ErrorCode = uv_scala_errorcode_EHOSTUNREACH()
  val EINTR: ErrorCode = uv_scala_errorcode_EINTR()
  val EINVAL: ErrorCode = uv_scala_errorcode_EINVAL()
  val EIO: ErrorCode = uv_scala_errorcode_EIO()
  val EISCONN: ErrorCode = uv_scala_errorcode_EISCONN()
  val EISDIR: ErrorCode = uv_scala_errorcode_EISDIR()
  val ELOOP: ErrorCode = uv_scala_errorcode_ELOOP()
  val EMFILE: ErrorCode = uv_scala_errorcode_EMFILE()
  val EMSGSIZE: ErrorCode = uv_scala_errorcode_EMSGSIZE()
  val ENAMETOOLONG: ErrorCode = uv_scala_errorcode_ENAMETOOLONG()
  val ENETDOWN: ErrorCode = uv_scala_errorcode_ENETDOWN()
  val ENETUNREACH: ErrorCode = uv_scala_errorcode_ENETUNREACH()
  val ENFILE: ErrorCode = uv_scala_errorcode_ENFILE()
  val ENOBUFS: ErrorCode = uv_scala_errorcode_ENOBUFS()
  val ENODEV: ErrorCode = uv_scala_errorcode_ENODEV()
  val ENOENT: ErrorCode = uv_scala_errorcode_ENOENT()
  val ENOMEM: ErrorCode = uv_scala_errorcode_ENOMEM()
  val ENONET: ErrorCode = uv_scala_errorcode_ENONET()
  val ENOPROTOOPT: ErrorCode = uv_scala_errorcode_ENOPROTOOPT()
  val ENOSPC: ErrorCode = uv_scala_errorcode_ENOSPC()
  val ENOSYS: ErrorCode = uv_scala_errorcode_ENOSYS()
  val ENOTCONN: ErrorCode = uv_scala_errorcode_ENOTCONN()
  val ENOTDIR: ErrorCode = uv_scala_errorcode_ENOTDIR()
  val ENOTEMPTY: ErrorCode = uv_scala_errorcode_ENOTEMPTY()
  val ENOTSOCK: ErrorCode = uv_scala_errorcode_ENOTSOCK()
  val ENOTSUP: ErrorCode = uv_scala_errorcode_ENOTSUP()
  val EOVERFLOW: ErrorCode = uv_scala_errorcode_EOVERFLOW()
  val EPERM: ErrorCode = uv_scala_errorcode_EPERM()
  val EPIPE: ErrorCode = uv_scala_errorcode_EPIPE()
  val EPROTO: ErrorCode = uv_scala_errorcode_EPROTO()
  val EPROTONOSUPPORT: ErrorCode = uv_scala_errorcode_EPROTONOSUPPORT()
  val EPROTOTYPE: ErrorCode = uv_scala_errorcode_EPROTOTYPE()
  val ERANGE: ErrorCode = uv_scala_errorcode_ERANGE()
  val EROFS: ErrorCode = uv_scala_errorcode_EROFS()
  val ESHUTDOWN: ErrorCode = uv_scala_errorcode_ESHUTDOWN()
  val ESPIPE: ErrorCode = uv_scala_errorcode_ESPIPE()
  val ESRCH: ErrorCode = uv_scala_errorcode_ESRCH()
  val ETIMEDOUT: ErrorCode = uv_scala_errorcode_ETIMEDOUT()
  val ETXTBSY: ErrorCode = uv_scala_errorcode_ETXTBSY()
  val EXDEV: ErrorCode = uv_scala_errorcode_EXDEV()
  val UNKNOWN: ErrorCode = uv_scala_errorcode_UNKNOWN()
  val EOF: ErrorCode = uv_scala_errorcode_EOF()
  val ENXIO: ErrorCode = uv_scala_errorcode_ENXIO()
  val EMLINK: ErrorCode = uv_scala_errorcode_EMLINK()
  val EHOSTDOWN: ErrorCode = uv_scala_errorcode_EHOSTDOWN()
  val EREMOTEIO: ErrorCode = uv_scala_errorcode_EREMOTEIO()
  val ENOTTY: ErrorCode = uv_scala_errorcode_ENOTTY()
  val EFTYPE: ErrorCode = uv_scala_errorcode_EFTYPE()
  val EILSEQ: ErrorCode = uv_scala_errorcode_EILSEQ()
  val ESOCKTNOSUPPORT: ErrorCode = uv_scala_errorcode_ESOCKTNOSUPPORT()

  // these require libuv >= 1.46 but Ubuntu 22 LTS only has 1.43
  // val ENODATA: ErrorCode = uv_scala_errorcode_ENODATA()
  // val EUNATCH: ErrorCode = uv_scala_errorcode_EUNATCH()

}

@extern
private[scalauv] object errors {

  def uv_scala_errorcode_E2BIG(): ErrorCode = extern
  def uv_scala_errorcode_EACCES(): ErrorCode = extern
  def uv_scala_errorcode_EADDRINUSE(): ErrorCode = extern
  def uv_scala_errorcode_EADDRNOTAVAIL(): ErrorCode = extern
  def uv_scala_errorcode_EAFNOSUPPORT(): ErrorCode = extern
  def uv_scala_errorcode_EAGAIN(): ErrorCode = extern
  def uv_scala_errorcode_EAI_ADDRFAMILY(): ErrorCode = extern
  def uv_scala_errorcode_EAI_AGAIN(): ErrorCode = extern
  def uv_scala_errorcode_EAI_BADFLAGS(): ErrorCode = extern
  def uv_scala_errorcode_EAI_BADHINTS(): ErrorCode = extern
  def uv_scala_errorcode_EAI_CANCELED(): ErrorCode = extern
  def uv_scala_errorcode_EAI_FAIL(): ErrorCode = extern
  def uv_scala_errorcode_EAI_FAMILY(): ErrorCode = extern
  def uv_scala_errorcode_EAI_MEMORY(): ErrorCode = extern
  def uv_scala_errorcode_EAI_NODATA(): ErrorCode = extern
  def uv_scala_errorcode_EAI_NONAME(): ErrorCode = extern
  def uv_scala_errorcode_EAI_OVERFLOW(): ErrorCode = extern
  def uv_scala_errorcode_EAI_PROTOCOL(): ErrorCode = extern
  def uv_scala_errorcode_EAI_SERVICE(): ErrorCode = extern
  def uv_scala_errorcode_EAI_SOCKTYPE(): ErrorCode = extern
  def uv_scala_errorcode_EALREADY(): ErrorCode = extern
  def uv_scala_errorcode_EBADF(): ErrorCode = extern
  def uv_scala_errorcode_EBUSY(): ErrorCode = extern
  def uv_scala_errorcode_ECANCELED(): ErrorCode = extern
  def uv_scala_errorcode_ECHARSET(): ErrorCode = extern
  def uv_scala_errorcode_ECONNABORTED(): ErrorCode = extern
  def uv_scala_errorcode_ECONNREFUSED(): ErrorCode = extern
  def uv_scala_errorcode_ECONNRESET(): ErrorCode = extern
  def uv_scala_errorcode_EDESTADDRREQ(): ErrorCode = extern
  def uv_scala_errorcode_EEXIST(): ErrorCode = extern
  def uv_scala_errorcode_EFAULT(): ErrorCode = extern
  def uv_scala_errorcode_EFBIG(): ErrorCode = extern
  def uv_scala_errorcode_EHOSTUNREACH(): ErrorCode = extern
  def uv_scala_errorcode_EINTR(): ErrorCode = extern
  def uv_scala_errorcode_EINVAL(): ErrorCode = extern
  def uv_scala_errorcode_EIO(): ErrorCode = extern
  def uv_scala_errorcode_EISCONN(): ErrorCode = extern
  def uv_scala_errorcode_EISDIR(): ErrorCode = extern
  def uv_scala_errorcode_ELOOP(): ErrorCode = extern
  def uv_scala_errorcode_EMFILE(): ErrorCode = extern
  def uv_scala_errorcode_EMSGSIZE(): ErrorCode = extern
  def uv_scala_errorcode_ENAMETOOLONG(): ErrorCode = extern
  def uv_scala_errorcode_ENETDOWN(): ErrorCode = extern
  def uv_scala_errorcode_ENETUNREACH(): ErrorCode = extern
  def uv_scala_errorcode_ENFILE(): ErrorCode = extern
  def uv_scala_errorcode_ENOBUFS(): ErrorCode = extern
  def uv_scala_errorcode_ENODEV(): ErrorCode = extern
  def uv_scala_errorcode_ENOENT(): ErrorCode = extern
  def uv_scala_errorcode_ENOMEM(): ErrorCode = extern
  def uv_scala_errorcode_ENONET(): ErrorCode = extern
  def uv_scala_errorcode_ENOPROTOOPT(): ErrorCode = extern
  def uv_scala_errorcode_ENOSPC(): ErrorCode = extern
  def uv_scala_errorcode_ENOSYS(): ErrorCode = extern
  def uv_scala_errorcode_ENOTCONN(): ErrorCode = extern
  def uv_scala_errorcode_ENOTDIR(): ErrorCode = extern
  def uv_scala_errorcode_ENOTEMPTY(): ErrorCode = extern
  def uv_scala_errorcode_ENOTSOCK(): ErrorCode = extern
  def uv_scala_errorcode_ENOTSUP(): ErrorCode = extern
  def uv_scala_errorcode_EOVERFLOW(): ErrorCode = extern
  def uv_scala_errorcode_EPERM(): ErrorCode = extern
  def uv_scala_errorcode_EPIPE(): ErrorCode = extern
  def uv_scala_errorcode_EPROTO(): ErrorCode = extern
  def uv_scala_errorcode_EPROTONOSUPPORT(): ErrorCode = extern
  def uv_scala_errorcode_EPROTOTYPE(): ErrorCode = extern
  def uv_scala_errorcode_ERANGE(): ErrorCode = extern
  def uv_scala_errorcode_EROFS(): ErrorCode = extern
  def uv_scala_errorcode_ESHUTDOWN(): ErrorCode = extern
  def uv_scala_errorcode_ESPIPE(): ErrorCode = extern
  def uv_scala_errorcode_ESRCH(): ErrorCode = extern
  def uv_scala_errorcode_ETIMEDOUT(): ErrorCode = extern
  def uv_scala_errorcode_ETXTBSY(): ErrorCode = extern
  def uv_scala_errorcode_EXDEV(): ErrorCode = extern
  def uv_scala_errorcode_UNKNOWN(): ErrorCode = extern
  def uv_scala_errorcode_EOF(): ErrorCode = extern
  def uv_scala_errorcode_ENXIO(): ErrorCode = extern
  def uv_scala_errorcode_EMLINK(): ErrorCode = extern
  def uv_scala_errorcode_EHOSTDOWN(): ErrorCode = extern
  def uv_scala_errorcode_EREMOTEIO(): ErrorCode = extern
  def uv_scala_errorcode_ENOTTY(): ErrorCode = extern
  def uv_scala_errorcode_EFTYPE(): ErrorCode = extern
  def uv_scala_errorcode_EILSEQ(): ErrorCode = extern
  def uv_scala_errorcode_ESOCKTNOSUPPORT(): ErrorCode = extern
  // def uv_scala_errorcode_ENODATA(): ErrorCode = extern
  // def uv_scala_errorcode_EUNATCH(): ErrorCode = extern

}
