package scalauv

import scalanative.unsafe.*
import scalanative.unsigned.*
import java.nio
import java.nio.charset.StandardCharsets
import scala.scalanative.libc.stdlib

opaque type Buffer = Ptr[Byte]

object Buffer {

  given Tag[Buffer] = Tag.Ptr(Tag.Byte)

  extension (buffer: Buffer) {

    def base: Ptr[Byte] = helpers.scala_uv_buf_base(buffer)
    def length: Int = helpers.scala_uv_buf_len(buffer).toInt

    def apply(index: Int): Byte = base(index)

    // TODO in Scala Native 0.5
    def asNio: nio.ByteBuffer = ???

    def asUtf8String(max: Int): String =
      new String(asArray(max), StandardCharsets.UTF_8)

    def asArray(max: Int): Array[Byte] = {
      val a = Array.ofDim[Byte](max)
      for i <- 0 until max do {
        a(i) = base(i)
      }
      a
    }

    def foreachByte(max: Int)(f: Byte => Unit): Unit = {
      for i <- 0 until max do {
        f(base(i))
      }
    }

    def +(index: Int): Buffer =
      buffer + (index.toLong * Buffer.structureSize.toLong)

    inline def toPtr: Ptr[Byte] = buffer

    inline def init(base: Ptr[Byte], size: CSize): Unit =
      helpers.scala_uv_buf_init(base, size.toUInt, buffer)

    inline def mallocInit(size: CSize): Unit =
      helpers.scala_uv_buf_init(stdlib.malloc(size), size.toUInt, buffer)

    inline def free(): Unit = stdlib.free(buffer)
  }

  val structureSize: CSize = helpers.scala_uv_buf_struct_size()

  inline def unsafeFromPtr(ptr: Ptr[Byte]): Buffer = ptr

  inline def stackAllocate(
      ptr: Ptr[Byte],
      size: CUnsignedInt
  ): Buffer = {
    val uvBuf = stackalloc[Byte](structureSize)
    helpers.scala_uv_buf_init(ptr, size, uvBuf)
    uvBuf
  }

  inline def stackAllocate(
      array: Array[Byte],
      index: Int = 0
  ): Buffer = {
    val uvBuf = stackalloc[Byte](structureSize)
    helpers.scala_uv_buf_init(
      array.at(index),
      (array.length - index).toUInt,
      uvBuf
    )
    uvBuf
  }

  def zoneAllocate(array: Array[Byte], index: Int = 0)(using
      Zone
  ): Buffer = {
    val uvBuf = alloc[Byte](structureSize)
    helpers.scala_uv_buf_init(
      array.at(index),
      (array.length - index).toUInt,
      uvBuf
    )
    uvBuf
  }

  def malloc(base: Ptr[Byte], size: CSize): Buffer = {
    val uvBuf = stdlib.malloc(structureSize.toULong)
    helpers.scala_uv_buf_init(base, size.toUInt, uvBuf)
    uvBuf
  }

  def malloc(array: Array[Byte], index: Int = 0): Buffer = {
    val uvBuf = stdlib.malloc(structureSize.toULong)
    helpers.scala_uv_buf_init(
      array.at(index),
      (array.length - index).toUInt,
      uvBuf
    )
    uvBuf
  }

}
