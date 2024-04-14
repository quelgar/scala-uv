package scalauv

import scalanative.unsafe.*
import scalanative.unsigned.*
import java.nio
import java.nio.charset.StandardCharsets
import scala.scalanative.libc.stdlib

/** The `uv_buf_t*` type. The fields of a `Buffer` are `base` and `length`.
  * `base` points to the actual content, while `length` typicaly indicates how
  * many bytes are valid.
  *
  * @see
  *   [[scalauv.Buffer$]]
  */
opaque type Buffer = Ptr[Byte]

/** Constructors and utilties for the [[scalauv.Buffer Buffer]] opaque type that
  * represents a pointer to the `uv_buf_t` native C type.
  *
  * @see
  *   [[scalauv.Buffer]]
  */
object Buffer {

  given Tag[Buffer] = Tag.Ptr(Tag.Byte)

  // to avoid compiler bug where same name is used in inline methods in different files
  private val h = scalauv.helpers

  extension (buffer: Buffer) {

    /** Pointer to the actual content.
      */
    inline def base: Ptr[Byte] = h.scala_uv_buf_base(buffer)

    inline def base_=(ptr: Ptr[Byte]): Unit =
      h.scala_uv_buf_base_set(buffer, ptr)

    /** The number of bytes (starting from `base`) that should be considered
      * part of this buffer. When reading, you probably want to set this to the
      * number of bytes read.
      */
    inline def length: Int = h.scala_uv_buf_len(buffer).toInt

    inline def length_=(len: Int): Unit =
      h.scala_uv_buf_len_set(buffer, len.toUInt)

    /** Gets the byte at the specified index.
      *
      * @param index
      *   the index of the byte to get, should be 0 <= `index` < `length`, but
      *   this is not checked.
      */
    def apply(index: Int): Byte = base(index)

    // TODO in Scala Native 0.5
    // def asNio: nio.ByteBuffer = ???

    /** Convert this buffer to a `String` using the UTF-8 charset. When reading,
      * you probably want to set `length` to the number of bytes read before
      * calling this.
      */
    def asUtf8String: String =
      new String(asArray, StandardCharsets.UTF_8)

    /** Copies this buffer content into a new Scala array. Copies `length` bytes
      * from `base` into a new array. When reading, you probably want to set
      * `length` to the number of bytes read before calling this.
      */
    def asArray: Array[Byte] = {
      val len = length
      val a = Array.ofDim[Byte](len)
      for i <- 0 until len do {
        a(i) = base(i)
      }
      a
    }

    /** Performs an operation using each byte in this buffer.
      */
    def foreach(f: Byte => Unit): Unit = {
      for i <- 0 until length do {
        f(base(i))
      }
    }

    def +(index: Int): Buffer =
      buffer + (index.toLong * Buffer.structureSize.toLong)

    /** Gets the native pointer to this buffer structure.
      */
    inline def toPtr: Ptr[Byte] = buffer

    /** Initializes this buffer structure with the specified `base` and
      * `length`.
      */
    inline def init(base: Ptr[Byte], length: CSize): Unit =
      h.scala_uv_buf_init(base, length.toUInt, buffer)

    /** Allocates a new native byte array of `size` bytes, and uses it to
      * initialize this buffer structure. `base` is set to the newly allocated
      * array, and `length` is set to `size`.
      */
    inline def mallocInit(size: CSize): Unit =
      h.scala_uv_buf_init(stdlib.malloc(size), size.toUInt, buffer)

    /** Frees this boffer structure. **Note:** does not free the `base` pointer.
      */
    inline def free(): Unit = stdlib.free(buffer)

    /** Perform an operation using this buffer with the length temporarily
      * changed. This can be useful after a read that only partially filled the
      * buffer.
      */
    inline def withLength[A](tempLength: Int)(f: Buffer => A): A = {
      val oldLength = length
      length = tempLength
      val result = f(buffer)
      length = oldLength
      result
    }

    inline def withOffset[A](offset: Int)(f: Buffer => A): A = {
      val origBase = base
      val origLength = length
      base += offset
      length -= offset
      val result = f(buffer)
      length = origLength
      base = origBase
      result
    }

  }

  /** The size of the `uv_buf_t` structure. Useful for manual allocation.
    */
  val structureSize: CSize = h.scala_uv_buf_struct_size()

  /** Cast a native pointer to a buffer structure pointer.
    */
  inline def unsafeFromPtr(ptr: Ptr[Byte]): Buffer = ptr

  /** Allocates a new buffer structure on the stack and initializes it with the
    * specified `base` and `length`.
    */
  inline def stackAllocate(base: Ptr[Byte], length: CSize): Buffer = {
    val uvBuf = stackalloc[Byte](structureSize)
    uvBuf.init(base, length)
    uvBuf
  }

  /** Allocates a new buffer structure on the stack and initializes it to point
    * to the specified array and index. `base` points to the byte and index
    * `index` in `array`. `length` is set to `array.length - index`.
    */
  inline def stackAllocate(
      array: Array[Byte],
      index: Int = 0
  ): Buffer = {
    val uvBuf = stackalloc[Byte](structureSize)
    uvBuf.init(array.at(index), (array.length - index).toUInt)
    uvBuf
  }

  /** Zone allocates a new buffer structure and initializes it with the
    * specified `base` and `length`.
    */
  def zoneAllocate(base: Ptr[Byte], length: CSize)(using Zone): Buffer = {
    val uvBuf = alloc[Byte](structureSize)
    uvBuf.init(base, length)
    uvBuf
  }

  /** Zone allocates a new buffer structure and initializes it to point to the
    * specified array and index. `base` points to the byte and index `index` in
    * `array`. `length` is set to `array.length - index`.
    */
  def zoneAllocate(array: Array[Byte], index: Int = 0)(using
      Zone
  ): Buffer = {
    val uvBuf = alloc[Byte](structureSize)
    uvBuf.init(array.at(index), (array.length - index).toUInt)
    uvBuf
  }

  /** Allocates a new buffer structure and initializes it with the specified
    * `base` and `length`.
    */
  def malloc(base: Ptr[CChar], size: CSize): Buffer = {
    val uvBuf = stdlib.malloc(structureSize)
    h.scala_uv_buf_init(base, size.toUInt, uvBuf)
    uvBuf
  }

  /** Allocates a new buffer structure and initializes it to point to the
    * specified array and index. `base` points to the byte and index `index` in
    * `array`. `length` is set to `array.length - index`.
    */
  def malloc(array: Array[Byte], index: Int = 0): Buffer = {
    val uvBuf = stdlib.malloc(structureSize)
    h.scala_uv_buf_init(
      array.at(index),
      (array.length - index).toUInt,
      uvBuf
    )
    uvBuf
  }

}
