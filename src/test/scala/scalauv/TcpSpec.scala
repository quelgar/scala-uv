package scalauv

import org.junit.Test
import org.junit.Assert.*
import LibUv.*
import scalanative.unsafe.*
import scalanative.unsigned.*
import scala.scalanative.libc.stdlib
import scala.scalanative.posix.netinet.*
import inOps.*
import scala.scalanative.posix.sys.socket
import scala.scalanative.posix.arpa.inet

final class TcpSpec {

  import TcpSpec.*

  @Test
  def listen(): Unit = {

    var runResult = 0

    Zone {

      val loop = stackalloc[Byte](uv_loop_size()).asInstanceOf[Loop]
      uv_loop_init(loop).checkErrorThrowIO()

      val port = 10000
      val serverTcpHandle = TcpHandle.stackAllocate()
      uv_tcp_init(loop, serverTcpHandle).checkErrorThrowIO()
      val serverSocketAddress = stackalloc[in.sockaddr_in]()
      serverSocketAddress.sin_family = socket.AF_INET.toUShort
      serverSocketAddress.sin_port = inet.htons(port.toUShort)
      serverSocketAddress.sin_addr.s_addr = in.INADDR_ANY
      uv_tcp_bind(
        serverTcpHandle,
        serverSocketAddress.asInstanceOf[Ptr[socket.sockaddr]],
        0.toUInt
      )
        .checkErrorThrowIO()
      uv_listen(serverTcpHandle, 128, onNewConnection).checkErrorThrowIO()

      val clientTcpHandle = TcpHandle.stackAllocate()
      uv_tcp_init(loop, clientTcpHandle).checkErrorThrowIO()
      val clientSocketAddress = stackalloc[in.sockaddr_in]()
      clientSocketAddress.sin_family = socket.AF_INET.toUShort
      clientSocketAddress.sin_port = inet.htons(port.toUShort)
      Net.setLocalHostSocket4(clientSocketAddress)
      val connectReq = ConnectReq.stackAllocate()
      uv_tcp_connect(
        connectReq,
        clientTcpHandle,
        clientSocketAddress.asInstanceOf[Ptr[socket.sockaddr]],
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

  def recordReceived(s: String): Unit = {
    receivedData = receivedData :+ s
  }

  def setFailed(msg: String): Unit = {
    failed = Some(msg)
  }

  def allocBuffer: AllocCallback = {
    (handle: Handle, suggestedSize: CSize, buf: Buffer) =>
      buf.mallocInit(suggestedSize)
  }

  def onNewConnection: ConnectionCallback = {
    (handle: StreamHandle, status: ErrorCode) =>
      val loop = uv_handle_get_loop(handle)
      UvUtils.attemptCatch {
        status.checkErrorThrowIO()
        val clientTcpHandle = TcpHandle.malloc()
        println("New connection")
        uv_tcp_init(loop, clientTcpHandle)
          .onFail(clientTcpHandle.free())
          .checkErrorThrowIO()
        UvUtils.onFail(uv_close(clientTcpHandle, onClose))
        uv_handle_set_data(clientTcpHandle, handle.toPtr)
        uv_accept(handle, clientTcpHandle).checkErrorThrowIO()
        uv_read_start(clientTcpHandle, allocBuffer, onRead)
          .checkErrorThrowIO()
        ()
      } { exception =>
        setFailed(exception.getMessage())
      }
  }

  def onClose: CloseCallback = (_: Handle).free()

  def onWrite: StreamWriteCallback = { (req: WriteReq, status: ErrorCode) =>
    status.onFailMessage(setFailed)
    val buf = Buffer.unsafeFromPtr(uv_req_get_data(req))
    stdlib.free(buf.base)
    buf.free()
    req.free()
  }

  def onConnect: ConnectCallback = { (req: ConnectReq, status: ErrorCode) =>
    status.onFailMessage(setFailed)
    val stream = req.connectReqStreamHandle
    def doWrite(text: String) = {
      val writeReq = WriteReq.malloc()
      val cText = mallocCString(text)
      val buf = Buffer.malloc(cText, text.length.toCSize)
      uv_req_set_data(writeReq, buf.toPtr)
      uv_write(writeReq, stream, buf, 1.toUInt, onWrite).onFailMessage { s =>
        stdlib.free(cText)
        buf.free()
        writeReq.free()
        setFailed(s)
      }
    }
    doWrite(text)
    doWrite(text)
    doWrite(DoneMarker.toString)
    uv_close(stream, null)
    ()
  }

  def onRead: StreamReadCallback = {
    (handle: StreamHandle, numRead: CSSize, buf: Buffer) =>
      numRead.toInt match {
        case ErrorCodes.EOF =>
          uv_close(handle, onClose)
        case code if code < 0 =>
          uv_close(handle, onClose)
          setFailed(UvUtils.errorMessage(code.toInt))
        case _ =>
          buf.length = numRead.toInt
          val (text, done) = buf.asUtf8String.span(_ != DoneMarker)
          recordReceived(text)
          if done.nonEmpty then {
            val listenHandle = Handle.unsafeFromPtr(uv_handle_get_data(handle))
            uv_close(listenHandle, null)
          }
      }
      stdlib.free(buf.base)
  }

}
