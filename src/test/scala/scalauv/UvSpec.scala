package scalauv

import org.junit.Test
import org.junit.Assert.*

final class UvSpec {

  @Test
  def doesNotRunCallbacksOnSuccess(): Unit = {

    var results = Vector.empty[String]

    def record(s: String): Unit = {
      results = results :+ s
    }

    val run = for {
      a <- 1.attempt.onFail(record("a"))
      b <- 2.attempt.onFail(record("b"))
      c <- 3.attempt.onFail(record("c"))
    } yield (a, b, c)

    assertTrue("succeeded", run.isSuccess)
    assertEquals("value", Some((1, 2, 3)), run.toOption)
    assertEquals("on fail callbacks not executed", Vector.empty, results)
  }

  @Test
  def runsCallbacksOnFailure(): Unit = {

    var results = Vector.empty[String]

    def record(s: String): Unit = {
      results = results :+ s
    }

    val run = for {
      a <- 1.attempt.onFail(record("a"))
      b <- 2.attempt.onFail(record("b"))
      c <- (-666).attempt.onFail(record("c"))
      d <- 4.attempt.onFail(record("d"))
      e <- (-1000).attempt.onFail(record("e"))
    } yield (a, b, c, d, e)

    assertTrue("failed", run.isFailure)
    assertEquals("on fail callbacks executed", Vector("c", "b", "a"), results)

  }

}
