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

object PollEvent {

  val UV_READABLE = 1
  val UV_WRITABLE = 2
  val UV_DISCONNECT = 4
  val UV_PRIORITIZED = 8

}

object ProcessFlags {

  val UV_PROCESS_SETUID = 1 << 0
  val UV_PROCESS_SETGID = 1 << 1
  val UV_PROCESS_WINDOWS_VERBATIM_ARGUMENTS = 1 << 2
  val UV_PROCESS_DETACHED = 1 << 3
  val UV_PROCESS_WINDOWS_HIDE = 1 << 4
  val UV_PROCESS_WINDOWS_HIDE_CONSOLE = 1 << 5
  val UV_PROCESS_WINDOWS_HIDE_GUI = 1 << 6

}

object StdioFlags {

  val UV_IGNORE = 0x00
  val UV_CREATE_PIPE = 0x01
  val UV_INHERIT_FD = 0x02
  val UV_INHERIT_STREAM = 0x04
  val UV_READABLE_PIPE = 0x10
  val UV_WRITABLE_PIPE = 0x20
  val UV_NON_BLOCK_PIPE = 0x40

}

object TtyMode {

  val UV_TTY_MODE_NORMAL = 0
  val UV_TTY_MODE_RAW = 1
  val UV_TTY_MODE_IO = 2

}

object TtyVtermState {

  val UV_TTY_SUPPORTED = 0
  val UV_TTY_UNSUPPORTED = 1

}

object UdpFlags {

  val UV_UDP_IPV6ONLY = 1
  val UV_UDP_PARTIAL = 2
  val UV_UDP_REUSEADDR = 4
  val UV_UDP_MMSG_CHUNK = 8
  val UV_UDP_MMSG_FREE = 16
  val UV_UDP_LINUX_RECVERR = 32
  val UV_UDP_RECVMMSG = 256

}

object Membership {

  val UV_LEAVE_GROUP = 0
  val UV_JOIN_GROUP = 1

}

object FsEvent {

  val UV_RENAME = 1
  val UV_CHANGE = 2

}

object FsEventFlags {

  val UV_FS_EVENT_WATCH_ENTRY = 1
  val UV_FS_EVENT_STAT = 2
  val UV_FS_EVENT_RECURSIVE = 4

}

object FsType {

  val UV_FS_UNKNOWN = -1
  val UV_FS_CUSTOM = 0
  val UV_FS_OPEN = 1
  val UV_FS_CLOSE = 2
  val UV_FS_READ = 3
  val UV_FS_WRITE = 4
  val UV_FS_SENDFILE = 5
  val UV_FS_STAT = 6
  val UV_FS_LSTAT = 7
  val UV_FS_FSTAT = 8
  val UV_FS_FTRUNCATE = 9
  val UV_FS_UTIME = 10
  val UV_FS_FUTIME = 11
  val UV_FS_ACCESS = 12
  val UV_FS_CHMOD = 13
  val UV_FS_FCHMOD = 14
  val UV_FS_FSYNC = 15
  val UV_FS_FDATASYNC = 16
  val UV_FS_UNLINK = 17
  val UV_FS_RMDIR = 18
  val UV_FS_MKDIR = 19
  val UV_FS_MKDTEMP = 20
  val UV_FS_RENAME = 21
  val UV_FS_SCANDIR = 22
  val UV_FS_LINK = 23
  val UV_FS_SYMLINK = 24
  val UV_FS_READLINK = 25
  val UV_FS_CHOWN = 26
  val UV_FS_FCHOWN = 27
  val UV_FS_REALPATH = 28
  val UV_FS_COPYFILE = 29
  val UV_FS_LCHOWN = 30
  val UV_FS_OPENDIR = 31
  val UV_FS_READDIR = 32
  val UV_FS_CLOSEDIR = 33
  val UV_FS_MKSTEMP = 34
  val UV_FS_LUTIME = 35

}

object DirEntType {

  val UV_DIRENT_UNKNOWN = 0
  val UV_DIRENT_FILE = 1
  val UV_DIRENT_DIR = 2
  val UV_DIRENT_LINK = 3
  val UV_DIRENT_FIFO = 4
  val UV_DIRENT_SOCKET = 5
  val UV_DIRENT_CHAR = 6
  val UV_DIRENT_BLOCK = 7

}

object Clock {

  val UV_CLOCK_MONOTONIC = 0
  val UV_CLOCK_REALTIME = 1

}
