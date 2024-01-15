package scalauv

import scala.util.boundary
import scala.scalanative.unsigned.*
import scala.scalanative.unsafe.*

final class IOVector(val nativeBuffers: Buffer, numberOfBuffers: Int) {

  inline def apply(index: Int): Buffer = {
    nativeBuffers + index
  }

  inline def foreachBuffer(f: Buffer => Unit): Unit =
    for index <- 0 until numberOfBuffers do {
      f(apply(index))
    }

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

  inline def stackAllocateForBuffer(
      ptr: Ptr[Byte],
      size: CUnsignedInt
  ): IOVector = {
    val buffer = Buffer.stackAllocate(ptr, size)
    IOVector(buffer, 1)
  }

  inline def stackAllocateForBuffers(
      buffers: Seq[(Ptr[Byte], CUnsignedInt)]
  ): IOVector = {
    val uvBufs =
      stackalloc[Byte](buffers.size.toUInt * Buffer.structureSize)
        .asInstanceOf[Buffer]
    buffers.zipWithIndex.foreach { case ((ptr, size), index) =>
      (uvBufs + index).init(ptr, size)
    }
    IOVector(uvBufs, buffers.size)
  }

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
