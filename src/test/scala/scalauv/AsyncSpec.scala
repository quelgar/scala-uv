package scalauv

import org.junit.Test
import org.junit.Assert.*
import scalanative.unsafe.*
import scalanative.unsigned.*

final class AsyncSpec {

  import AsyncSpec.*

  @Test
  def runsAnAsyncCallback(): Unit = {
    Zone {
      val loop = LibUv.uv_default_loop()

      val asyncHandle = AsyncHandle.zoneAllocate()

      UvUtils.attempt {
        LibUv.uv_async_init(loop, asyncHandle, callback).checkErrorThrowIO()

        UvUtils.onFail(LibUv.uv_close(asyncHandle, closeCallback))

        LibUv.uv_async_send(asyncHandle).checkErrorThrowIO()
      }

      LibUv.uv_run(loop, RunMode.DEFAULT).checkErrorThrowIO()
      ()
    }

    assertTrue("callback was run", callbackRun)
    assertTrue("handle was closed", closed)
  }

}

object AsyncSpec {

  private var callbackRun = false

  private var closed = false

  private val closeCallback: LibUv.CloseCallback = { (handle: Handle) =>
    closed = true
  }

  private val callback: LibUv.AsyncCallback = { (handle: AsyncHandle) =>
    callbackRun = true
    LibUv.uv_close(handle, closeCallback)
  }
}
