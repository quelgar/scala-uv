package scalauv

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*
import java.time.Instant

/** Time structure.
  *
  * @see
  *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_timespec_t LibUv docs]]
  */
trait TimeSpec {
  def seconds: CLong
  def nanoSeconds: CLong

  final inline def toInstant: Instant =
    Instant.ofEpochSecond(seconds, nanoSeconds)
}

/** Pointer to file `stat` strcture.
  *
  * @see
  *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_stat_t LibUv docs]]
  * @group fs
  */
opaque type Stat = Ptr[CStruct20[
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
  CLongLong,
  CLongLong,
  CLongLong,
  CLongLong,
  CLongLong,
  CLongLong,
  CLongLong,
  CLongLong
]]

object Stat {

  extension (s: Stat) {

    inline def device: CUnsignedLongLong = s._1
    inline def mode: CUnsignedLongLong = s._2
    inline def nlink: CUnsignedLongLong = s._3
    inline def uid: CUnsignedLongLong = s._4
    inline def gid: CUnsignedLongLong = s._5
    inline def rdev: CUnsignedLongLong = s._6
    inline def ino: CUnsignedLongLong = s._7
    inline def size: CUnsignedLongLong = s._8
    inline def blksize: CUnsignedLongLong = s._9
    inline def blocks: CUnsignedLongLong = s._10
    inline def flags: CUnsignedLongLong = s._11
    inline def gen: CUnsignedLongLong = s._12

    inline def accessTimeSpec: TimeSpec = new TimeSpec {
      override def seconds: CLong = s._13
      override def nanoSeconds: CLong = s._14
    }
    inline def accessTime: Instant = Instant.ofEpochSecond(s._13, s._14)

    inline def modificationTimeSpec: TimeSpec = new TimeSpec {
      override def seconds: CLong = s._15
      override def nanoSeconds: CLong = s._16
    }
    inline def modificationTime: Instant = Instant.ofEpochSecond(s._15, s._16)

    inline def inodeChangeTimeSpec: TimeSpec = new TimeSpec {
      override def seconds: CLong = s._17
      override def nanoSeconds: CLong = s._18
    }
    inline def inodeChangeTime: Instant = Instant.ofEpochSecond(s._17, s._18)

    inline def birthTimeSpec: TimeSpec = new TimeSpec {
      override def seconds: CLong = s._19
      override def nanoSeconds: CLong = s._20
    }
    inline def birthTime: Instant = Instant.ofEpochSecond(s._19, s._20)

  }
}

/** Pointer to `struct statfs`.
  *
  * @see
  *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_statfs_t LibUv docs]]
  * @group fs
  */
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

/** Pointer to directory entry structure.
  *
  * @see
  *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_dirent_t LibUv docs]]
  * @group fs
  */
opaque type DirEnt = Ptr[CStruct2[CString, DirEntType]]

object DirEnt {

  extension (d: DirEnt) {

    inline def name: CString = d._1
    inline def entryType: DirEntType = d._2

  }
}

/** Pointer to directory structure.
  *
  * @see
  *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_dir_t LibUv docs]]
  * @group fs
  */
opaque type Dir = Ptr[CStruct2[DirEnt, CSize]]

object Dir {

  extension (d: Dir) {

    inline def entries: DirEnt = d._1
    inline def entries_=(entries: DirEnt): Unit = d._1 = entries

    inline def numberOfEntries: CSize = d._2
    inline def numberOfEntries_=(n: CSize): Unit = d._2 = n

  }

  def zoneAllocate(size: Int)(using Zone): Dir = {
    val entries = alloc[CStruct2[CString, DirEntType]](size.toUInt)
    val dir = alloc[CStruct2[DirEnt, CSize]](1)
    dir.entries = entries
    dir.numberOfEntries = size.toUInt
    dir
  }

}
