package scalauv

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*
import java.time.Instant
import scala.scalanative.posix.inttypes.*

/** Time structure.
  *
  * @see
  *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_timespec_t LibUv docs]]
  */
opaque type TimeSpec = CStruct2[CLong, CLong]

object TimeSpec {

  extension (ts: TimeSpec) {

    inline def seconds: CLong = ts._1
    inline def seconds_=(s: CLong): Unit = ts._1 = s

    inline def nanoSeconds: CLong = ts._2
    inline def nanoSeconds_=(ns: CLong): Unit = ts._2 = ns

    inline def toInstant: Instant =
      Instant.ofEpochSecond(seconds.toLong, nanoSeconds.toLong)
  }

}

/** Pointer to file `stat` strcture.
  *
  * @see
  *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_stat_t LibUv docs]]
  * @group fs
  */
opaque type Stat = CStruct16[
  uint64_t,
  uint64_t,
  uint64_t,
  uint64_t,
  uint64_t,
  uint64_t,
  uint64_t,
  uint64_t,
  uint64_t,
  uint64_t,
  uint64_t,
  uint64_t,
  TimeSpec,
  TimeSpec,
  TimeSpec,
  TimeSpec
]

object Stat {

  extension (s: Stat) {

    inline def device: uint64_t = s._1
    inline def device_=(d: uint64_t): Unit = s._1 = d
    inline def mode: uint64_t = s._2
    inline def mode_=(m: uint64_t): Unit = s._2 = m
    inline def nlink: uint64_t = s._3
    inline def nlink_=(n: uint64_t): Unit = s._3 = n
    inline def uid: uint64_t = s._4
    inline def uid_=(u: uint64_t): Unit = s._4 = u
    inline def gid: uint64_t = s._5
    inline def gid_=(g: uint64_t): Unit = s._5 = g
    inline def rdev: uint64_t = s._6
    inline def rdev_=(r: uint64_t): Unit = s._6 = r
    inline def ino: uint64_t = s._7
    inline def ino_=(i: uint64_t): Unit = s._7 = i
    inline def size: uint64_t = s._8
    inline def size_=(size: uint64_t): Unit = s._8 = size
    inline def blksize: uint64_t = s._9
    inline def blksize_=(b: uint64_t): Unit = s._9 = b
    inline def blocks: uint64_t = s._10
    inline def blocks_=(b: uint64_t): Unit = s._10 = b
    inline def flags: uint64_t = s._11
    inline def flags_=(f: uint64_t): Unit = s._11 = f
    inline def gen: uint64_t = s._12
    inline def gen_=(g: uint64_t): Unit = s._12 = g
    inline def accessTime: TimeSpec = s._13
    inline def accessTime_=(t: TimeSpec): Unit = s._13 = t
    inline def modificationTime: TimeSpec = s._14
    inline def modificationTime_=(t: TimeSpec): Unit = s._14 = t
    inline def inodeChangeTime: TimeSpec = s._15
    inline def inodeChangeTime_=(t: TimeSpec): Unit = s._15 = t
    inline def birthTime: TimeSpec = s._16
    inline def birthTime_=(t: TimeSpec): Unit = s._16 = t

  }
}

/** Pointer to `struct statfs`.
  *
  * @see
  *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_statfs_t LibUv docs]]
  * @group fs
  */
type StatFs = CStruct8[
  uint64_t,
  uint64_t,
  uint64_t,
  uint64_t,
  uint64_t,
  uint64_t,
  uint64_t,
  CArray[uint64_t, Nat._4]
]

/** Pointer to directory entry structure.
  *
  * @see
  *   [[https://docs.libuv.org/en/v1.x/fs.html#c.uv_dirent_t LibUv docs]]
  * @group fs
  */
opaque type DirEnt = CStruct2[CString, DirEntType]

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
opaque type Dir = CStruct2[DirEnt, CSize]

object Dir {

  extension (d: Dir) {

    inline def entries: DirEnt = d._1
    inline def entries_=(entries: DirEnt): Unit = d._1 = entries

    inline def numberOfEntries: CSize = d._2
    inline def numberOfEntries_=(n: CSize): Unit = d._2 = n

  }

  def zoneAllocate(size: Int)(using Zone): Dir = {
    val entries = alloc[DirEnt](size.toUInt)
    val dir = alloc[Dir](1)
    dir.entries = entries
    dir.numberOfEntries = size.toUInt
    dir
  }

}
