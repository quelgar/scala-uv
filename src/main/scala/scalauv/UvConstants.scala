package scalauv

import LibUv.*
import scala.scalanative.unsafe.*

type UvBufferSize = Nat.Digit2[Nat._1, Nat._6]

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

object RunMode {
  val DEFAULT: RunMode = 0
  val ONCE: RunMode = 1
  val NOWAIT: RunMode = 2
}

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

object FileOpenFlags {
  val O_RDONLY = 0
  val O_WRONLY = 1
  val O_RDWR = 2

  val O_CREAT = 0x200
  val O_EXCL = 0x800
  val O_TRUNC = 0x400

  val O_APPEND = 0x08
  val O_DSYNC = 0x400000
  val O_SYNC = 0x80
}

object CreateMode {

  val S_IRUSR = 0x100
  val S_IWUSR = 0x80

  val None = 0

}

object AccessCheckMode {

  val F_OK = 0
  val R_OK = 4
  val W_OK = 2
  val X_OK = 1

}
