package scalauv
package main

import scala.scalanative.*
import unsafe.*
import unsigned.*
import LibUv.*

object Main {

  private var done = false

  private val callback: AsyncCallback = { (handle: AsyncHandle) =>
    println("Callback!!")
    done = true
    uv_close(handle, null)
  }

  def main(args: Array[String]): Unit = {

    withZone {
      val loop = uv_default_loop()

      val asyncHandle = AsyncHandle.zoneAllocate()
      uv_async_init(loop, asyncHandle, callback).checkErrorThrowIO()

      uv_async_send(asyncHandle).checkErrorThrowIO()

      println(s"Main before, done = $done")
      uv_run(loop, RunMode.DEFAULT).checkErrorThrowIO()
      println(s"Main after, done = $done")
    }
  }

}
