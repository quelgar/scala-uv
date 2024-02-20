package scalauv

import scala.scalanative.unsafe.*

type UvBufferSize = Nat.Digit2[Nat._1, Nat._6]

opaque type RunMode = CInt

object RunMode {
  val DEFAULT: RunMode = 0
  val ONCE: RunMode = 1
  val NOWAIT: RunMode = 2
}

object FileOpenFlags {
  val O_APPEND = helpers.scala_uv_value_o_append()
  val O_CREAT = helpers.scala_uv_value_o_creat()
  val O_DIRECT = helpers.scala_uv_value_o_direct()
  val O_DIRECTORY = helpers.scala_uv_value_o_directory()
  val O_DSYNC = helpers.scala_uv_value_o_dsync()
  val O_EXCL = helpers.scala_uv_value_o_excl()
  val O_EXLOCK = helpers.scala_uv_value_o_exlock()
  val O_FILEMAP = helpers.scala_uv_value_o_filemap()
  val O_NOATIME = helpers.scala_uv_value_o_noatime()
  val O_NOCTTY = helpers.scala_uv_value_o_noctty()
  val O_NOFOLLOW = helpers.scala_uv_value_o_nofollow()
  val O_NONBLOCK = helpers.scala_uv_value_o_nonblock()
  val O_RANDOM = helpers.scala_uv_value_o_random()
  val O_RDONLY = helpers.scala_uv_value_o_rdonly()
  val O_RDWR = helpers.scala_uv_value_o_rdwr()
  val O_SEQUENTIAL = helpers.scala_uv_value_o_sequential()
  val O_SHORT_LIVED = helpers.scala_uv_value_o_short_lived()
  val O_SYMLINK = helpers.scala_uv_value_o_symlink()
  val O_SYNC = helpers.scala_uv_value_o_sync()
  val O_TEMPORARY = helpers.scala_uv_value_o_temporary()
  val O_TRUNC = helpers.scala_uv_value_o_trunc()
  val O_WRONLY = helpers.scala_uv_value_o_wronly()
}

object CreateMode {

  val S_IRWXU = 0x1c0
  val S_IRUSR = 0x100
  val S_IWUSR = 0x80
  val S_IXUSR = 0x40

  val S_IRWXG = 0x38
  val S_IRGRP = 0x20
  val S_IWGRP = 0x10
  val S_IXGRP = 0x8

  val S_IRWXO = 0x7
  val S_IROTH = 0x4
  val S_IWOTH = 0x2
  val S_IXOTH = 0x1

  val S_ISUID = 0x800
  val S_ISGID = 0x400
  val S_ISVTX = 0x200

  val None = 0

}

object AccessCheckMode {

  val F_OK = 0
  val R_OK = 4
  val W_OK = 2
  val X_OK = 1

}

opaque type PollEvent = CInt

object PollEvent {

  val UV_READABLE: PollEvent = 1
  val UV_WRITABLE: PollEvent = 2
  val UV_DISCONNECT: PollEvent = 4
  val UV_PRIORITIZED: PollEvent = 8

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

opaque type TtyMode = CInt

object TtyMode {

  val UV_TTY_MODE_NORMAL: TtyMode = 0
  val UV_TTY_MODE_RAW: TtyMode = 1
  val UV_TTY_MODE_IO: TtyMode = 2

}

opaque type TtyVtermState = CInt

object TtyVtermState {

  val UV_TTY_SUPPORTED: TtyVtermState = 0
  val UV_TTY_UNSUPPORTED: TtyVtermState = 1

}

opaque type UdpFlags = CInt

object UdpFlags {

  val UV_UDP_IPV6ONLY: UdpFlags = 1
  val UV_UDP_PARTIAL: UdpFlags = 2
  val UV_UDP_REUSEADDR: UdpFlags = 4
  val UV_UDP_MMSG_CHUNK: UdpFlags = 8
  val UV_UDP_MMSG_FREE: UdpFlags = 16
  val UV_UDP_LINUX_RECVERR: UdpFlags = 32
  val UV_UDP_RECVMMSG: UdpFlags = 256

}

opaque type Membership = CInt

object Membership {

  val UV_LEAVE_GROUP: Membership = 0
  val UV_JOIN_GROUP: Membership = 1

}

opaque type FsEvent = CInt

object FsEvent {

  val UV_RENAME: FsEvent = 1
  val UV_CHANGE: FsEvent = 2

}

opaque type FsEventFlags = CInt

object FsEventFlags {

  val UV_FS_EVENT_WATCH_ENTRY: FsEvent = 1
  val UV_FS_EVENT_STAT: FsEvent = 2
  val UV_FS_EVENT_RECURSIVE: FsEvent = 4

}

opaque type FsType = CInt

object FsType {

  val UV_FS_UNKNOWN: FsType = -1
  val UV_FS_CUSTOM: FsType = 0
  val UV_FS_OPEN: FsType = 1
  val UV_FS_CLOSE: FsType = 2
  val UV_FS_READ: FsType = 3
  val UV_FS_WRITE: FsType = 4
  val UV_FS_SENDFILE: FsType = 5
  val UV_FS_STAT: FsType = 6
  val UV_FS_LSTAT: FsType = 7
  val UV_FS_FSTAT: FsType = 8
  val UV_FS_FTRUNCATE: FsType = 9
  val UV_FS_UTIME: FsType = 10
  val UV_FS_FUTIME: FsType = 11
  val UV_FS_ACCESS: FsType = 12
  val UV_FS_CHMOD: FsType = 13
  val UV_FS_FCHMOD: FsType = 14
  val UV_FS_FSYNC: FsType = 15
  val UV_FS_FDATASYNC: FsType = 16
  val UV_FS_UNLINK: FsType = 17
  val UV_FS_RMDIR: FsType = 18
  val UV_FS_MKDIR: FsType = 19
  val UV_FS_MKDTEMP: FsType = 20
  val UV_FS_RENAME: FsType = 21
  val UV_FS_SCANDIR: FsType = 22
  val UV_FS_LINK: FsType = 23
  val UV_FS_SYMLINK: FsType = 24
  val UV_FS_READLINK: FsType = 25
  val UV_FS_CHOWN: FsType = 26
  val UV_FS_FCHOWN: FsType = 27
  val UV_FS_REALPATH: FsType = 28
  val UV_FS_COPYFILE: FsType = 29
  val UV_FS_LCHOWN: FsType = 30
  val UV_FS_OPENDIR: FsType = 31
  val UV_FS_READDIR: FsType = 32
  val UV_FS_CLOSEDIR: FsType = 33
  val UV_FS_MKSTEMP: FsType = 34
  val UV_FS_LUTIME: FsType = 35

}

opaque type DirEntType = CInt

object DirEntType {

  given Tag[DirEntType] = Tag.Int

  val UV_DIRENT_UNKNOWN: DirEntType = 0
  val UV_DIRENT_FILE: DirEntType = 1
  val UV_DIRENT_DIR: DirEntType = 2
  val UV_DIRENT_LINK: DirEntType = 3
  val UV_DIRENT_FIFO: DirEntType = 4
  val UV_DIRENT_SOCKET: DirEntType = 5
  val UV_DIRENT_CHAR: DirEntType = 6
  val UV_DIRENT_BLOCK: DirEntType = 7

}

opaque type ClockType = CInt

object ClockType {

  val UV_CLOCK_MONOTONIC: ClockType = 0
  val UV_CLOCK_REALTIME: ClockType = 1

}
