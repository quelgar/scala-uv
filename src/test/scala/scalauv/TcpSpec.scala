package scalauv
import org.junit.Test
import org.junit.Assert.*
import LibUv.*
import scalanative.unsafe.*
import scalanative.unsigned.*
import scala.scalanative.libc.stdlib

final class TcpSpec {

  import TcpSpec.*

  def recordReceived(s: String): Unit = {
    receivedData = receivedData :+ s
  }

  def setFailed(msg: String): Unit = {
    failed = Some(msg)
  }

  def onClose: CloseCallback = stdlib.free

  def onRead: StreamReadCallback = {
    (handle: StreamHandle, numRead: CSSize, buf: Buffer) =>
      numRead match {
        case ErrorCodes.EOF =>
          uv_close(handle, onClose)
        case code if code < 0 =>
          uv_close(handle, onClose)
          setFailed(UvUtils.errorMessage(code.toInt))
        case _ =>
          val (text, done) =
            buf.asUtf8String(numRead.toInt).span(_ != DoneMarker)
          recordReceived(text)
          if done.nonEmpty then {
            val listenHandle = uv_handle_get_data(handle)
            uv_close(listenHandle, null)
          }
      }
      stdlib.free(buf.base)
  }

  @Test
  def foo(): Unit = {
    println("XXXXXX")
    assertEquals(1, 1)
  }

  @Test
  def listen(): Unit = {

    var runResult = 0

    withZone {

      val loop = stackalloc[Byte](uv_loop_size()).asInstanceOf[Loop]
      uv_loop_init(loop).checkErrorThrowIO()

      def allocBuffer: AllocCallback = {
        (handle: StreamHandle, suggestedSize: CSize, buf: Buffer) =>
          buf.mallocInit(suggestedSize)
      }

      def onNewConnection: ConnectionCallback = {
        (handle: StreamHandle, status: ErrorCode) =>
          val loop = uv_handle_get_loop(handle)
          val attempt = for {
            _ <- status.attempt.mapErrorMessage(s =>
              s"New connection error: $s"
            )
            clientTcpHandle = UvUtils.mallocHandle(HandleType.UV_TCP)
            _ <- Uv.onFail(stdlib.free(clientTcpHandle))
            _ <- {
              println("New connection")
              uv_tcp_init(loop, clientTcpHandle).attempt
                .mapErrorMessage(s => s"TCP handle init failed: $s")
            }
            _ <- Uv.succeed {
              uv_handle_set_data(clientTcpHandle, handle)
            }
            _ <- uv_accept(handle, clientTcpHandle).attempt
              .mapErrorMessage(s => s"Accept failed: $s")
            _ <- Uv.onFail(uv_close(clientTcpHandle, onClose))
            _ <- uv_read_start(clientTcpHandle, allocBuffer, onRead).attempt
              .mapErrorMessage(s => s"Read start failed: $s")
          } yield ()
          attempt.foreachFailure(e => setFailed(e.message))
      }

      val port = 10000
      val serverTcpHandle = UvUtils.stackAllocateHandle(HandleType.UV_TCP)
      uv_tcp_init(loop, serverTcpHandle).checkErrorThrowIO()
      val serverSocketAddress = SocketAddress4.unspecifiedAddress(port)
      uv_tcp_bind(serverTcpHandle, serverSocketAddress, 0.toUInt)
        .checkErrorThrowIO()
      uv_listen(serverTcpHandle, 128, onNewConnection).checkErrorThrowIO()

      def onWrite: StreamWriteCallback = { (req: WriteReq, status: ErrorCode) =>
        status.onFailMessage(setFailed)
        val buf = Buffer.unsafeFromNative(uv_req_get_data(req))
        stdlib.free(buf.base)
        buf.free()
        stdlib.free(req)
      }

      def onConnect: ConnectCallback = { (req: ConnectReq, status: ErrorCode) =>
        status.onFailMessage(setFailed)
        val stream = req.connectReqStreamHandle
        def doWrite(text: String) = {
          val writeReq = UvUtils.mallocRequest(RequestType.WRITE)
          val cText = mallocCString(text)
          val buf = Buffer.malloc(cText, text.length.toULong)
          uv_req_set_data(writeReq, buf.toNative)
          uv_write(writeReq, stream, buf, 1.toUInt, onWrite).onFailMessage {
            s =>
              stdlib.free(cText)
              buf.free()
              stdlib.free(writeReq)
              setFailed(s)
          }
        }
        doWrite(text)
        doWrite(text)
        doWrite(DoneMarker.toString)
        uv_close(stream, null)
        ()
      }

      val clientTcpHandle = UvUtils.stackAllocateHandle(HandleType.UV_TCP)
      uv_tcp_init(loop, clientTcpHandle).checkErrorThrowIO()
      val clientSocketAddress = SocketAddress4.loopbackAddress(port)
      val connectReq = UvUtils.stackAllocateRequest(RequestType.CONNECT)
      uv_tcp_connect(
        connectReq,
        clientTcpHandle,
        clientSocketAddress,
        onConnect
      ).checkErrorThrowIO()

      runResult = uv_run(loop, RunMode.DEFAULT).checkErrorThrowIO()

      uv_loop_close(loop).checkErrorThrowIO()
    }

    assertEquals("runResult", 0, runResult)
    assertEquals("failed", None, failed)
    assertEquals("recievedData", text + text, receivedData.mkString)

  }

}

object TcpSpec {
  private val DoneMarker = '!'

  private val text = "my country is the world, and my religion is to do good"

  private var receivedData = Vector.empty[String]
  private var failed = Option.empty[String]

}
