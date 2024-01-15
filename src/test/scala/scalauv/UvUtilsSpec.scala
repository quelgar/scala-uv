package scalauv

import org.junit.Test
import org.junit.Assert.*
import java.io.IOException
import org.junit.function.ThrowingRunnable

final class UvUtilsSpec {

  @Test
  def doesNotRunOnErrorFunctionsOnSuccess(): Unit = {
    var results = Vector.empty[Throwable]

    var completed = false

    def record(t: Throwable): Unit = {
      results = results :+ t
    }

    UvUtils.attempt {
      UvUtils.onFailWith(record)
      UvUtils.onComplete { completed = true }
      UvUtils.onFailWith(record)
      UvUtils.onFailWith(record)
    }
    assertEquals(
      "when successful, onFail operations should not be executed",
      Vector.empty,
      results
    )
    assertTrue("completed should be true", completed)
  }

  @Test
  def runOnFailFunctionsOnFailure(): Unit = {
    var results = Vector.empty[(Int, Throwable)]

    var completed = false

    def record(i: Int)(t: Throwable): Unit = {
      results = results :+ (i, t)
    }

    val e = new IOException("test")

    def test: ThrowingRunnable = { () =>
      UvUtils.attempt {
        UvUtils.onFailWith(record(1))
        UvUtils.onComplete { completed = true }
        UvUtils.onFailWith(record(2))
        throw e
        UvUtils.onFailWith(record(3))
      }
    }
    assertThrows(
      "IOException should be thrown",
      classOf[IOException],
      test
    )
    assertEquals(
      "when failed, onFail operations should be executed",
      Vector((2, e), (1, e)),
      results
    )
    assertTrue("completed should be true", completed)
  }

}
