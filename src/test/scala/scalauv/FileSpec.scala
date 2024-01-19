package scalauv

import LibUv.*
import scalanative.unsafe.*
import scalanative.unsigned.*
import java.nio.charset.StandardCharsets

import org.junit.Test
import org.junit.Assert.*
import scala.scalanative.libc.string
import java.nio.file.Files
import java.nio.file.Path

final class FileSpec {

  @Test
  def readFile(): Unit = {
    val expected = "Hello, world!"
    val filename = c"src/test/resources/test.txt"

    withZone {
      val loop = uv_default_loop()

      val BufSize: CSize = 1024.toUInt

      val buffer = alloc[Byte](BufSize)

      val readFileHandle = FileReq
        .use { openReq =>
          uv_fs_open(
            loop,
            openReq,
            filename,
            FileOpenFlags.O_RDONLY,
            0,
            null
          )
        }
        .checkErrorThrowIO()

      val bytesRead = FileReq
        .use { readReq =>
          val iov = IOVector.stackAllocateForBuffer(buffer, BufSize.toUInt)
          uv_fs_read(
            loop,
            readReq,
            readFileHandle,
            iov.nativeBuffers,
            iov.nativeNumBuffers,
            -1,
            null
          )
        }
        .checkErrorThrowIO()

      val readText = fromCString(buffer, StandardCharsets.UTF_8)

      FileReq
        .use { closeReq =>
          uv_fs_close(loop, closeReq, readFileHandle, null)
        }
        .checkErrorThrowIO()

      assertEquals(13, bytesRead)
      assertEquals(expected, readText)
    }

  }

  @Test
  def readNonExistentFileReturnsError() = {
    val loop = uv_default_loop()
    val result = FileReq
      .use { openReq =>
        uv_fs_open(
          loop,
          openReq,
          c"i_do_not_exist.fake",
          FileOpenFlags.O_RDONLY,
          0,
          null
        )
      }

    assertEquals("error code", ErrorCodes.ENOENT, result)
  }

  @Test
  def writeNewFile(): Unit = {
    val text = "my country is the world, and my religion is to do good"
    val filename = "src/test/resources/write-test.txt"

    withZone {
      val loop = uv_default_loop()

      val cText = toCString(text, StandardCharsets.UTF_8)
      val cFilename = toCString(filename, StandardCharsets.UTF_8)

      val fileHandle = FileReq
        .use { openReq =>
          uv_fs_open(
            loop,
            openReq,
            cFilename,
            FileOpenFlags.O_WRONLY | FileOpenFlags.O_CREAT | FileOpenFlags.O_TRUNC,
            CreateMode.S_IRUSR | CreateMode.S_IWUSR,
            null
          )
        }
        .checkErrorThrowIO()

      val bytesWritten = FileReq
        .use { writeReq =>
          val iov =
            IOVector.stackAllocateForBuffer(cText, string.strlen(cText).toUInt)
          uv_fs_write(
            loop,
            writeReq,
            fileHandle,
            iov.nativeBuffers,
            iov.nativeNumBuffers,
            -1,
            null
          )
        }
        .checkErrorThrowIO()

      FileReq
        .use { closeReq =>
          uv_fs_close(loop, closeReq, fileHandle, null)
        }
        .checkErrorThrowIO()

      val path = Path.of(filename)
      val actualText = Files.readString(path)
      FileReq.use { deleteReq =>
        uv_fs_unlink(loop, deleteReq, cFilename, null)
      }
      val exists = Files.exists(path)

      assertEquals(text.length(), bytesWritten)
      assertEquals(text, actualText)
      assertFalse("created file should be deleted", exists)
    }
  }

}
