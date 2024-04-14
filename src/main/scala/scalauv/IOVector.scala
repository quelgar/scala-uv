package scalauv

import scala.util.boundary
import scala.scalanative.unsigned.*
import scala.scalanative.unsafe.*

/** A collection of multiple buffers.
  *
  * @param nativeBuffers
  *   Pointer to a consecutive sequence of `uv_buf_t` structures.
  * @param numberOfBuffers
  *   The number of buffers located at `nativeBuffers`.
  */
final class IOVector(val nativeBuffers: Buffer, numberOfBuffers: Int) {

  /** Gets the buffer structure at the specified index.
    */
  inline def apply(index: Int): Buffer = {
    nativeBuffers + index
  }

  /** Calls the specified function for each buffer in this collection.
    */
  inline def foreachBuffer(f: Buffer => Unit): Unit =
    for index <- 0 until numberOfBuffers do {
      f(apply(index))
    }

  /** Calls the specified function for each buffer in this collection, up to a
    * maximum number.
    *
    * @param max
    *   The maximum number of buffers to process.
    */
  def foreachBufferMax(max: Int)(f: Buffer => Unit): Unit = {
    var remaining = max
    boundary {
      for index <- 0 until numberOfBuffers do {
        val buf = apply(index)
        val length = scala.math.min(buf.length, remaining)
        f(buf)
        remaining -= length
        assert(remaining >= 0)
        if remaining == 0 then boundary.break()
      }
    }
  }

  inline def nativeNumBuffers: CUnsignedInt = numberOfBuffers.toUInt

}

object IOVector {

  /** Stack allocats a single buffer structure.
    */
  inline def stackAllocateForBuffer(
      ptr: Ptr[Byte],
      size: CSize
  ): IOVector = {
    val buffer = Buffer.stackAllocate(ptr, size)
    IOVector(buffer, 1)
  }

  /** Stack allocates multiple buffer structures.
    */
  inline def stackAllocateForBuffers(
      buffers: Seq[(Ptr[Byte], CSize)]
  ): IOVector = {
    val uvBufs =
      stackalloc[Byte](buffers.size.toUInt * Buffer.structureSize)
        .asInstanceOf[Buffer]
    buffers.zipWithIndex.foreach { case ((ptr, size), index) =>
      (uvBufs + index).init(ptr, size)
    }
    IOVector(uvBufs, buffers.size)
  }

  /** Zone allocates both buffer structures and the underlying base byte arrays.
    *
    * @param bufferSizes
    *   The sizes of the buffers to allocate.
    */
  def zoneAllocate(bufferSizes: Int*)(using Zone): IOVector = {
    val uvBufs =
      alloc[Byte](bufferSizes.size.toUInt * Buffer.structureSize)
        .asInstanceOf[Buffer]
    bufferSizes.zipWithIndex.foreach { case (size, index) =>
      val base = alloc[Byte](size)
      (uvBufs + index).init(base, size.toUInt)
    }
    IOVector(uvBufs, bufferSizes.size)
  }

}
