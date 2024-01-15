package scalauv

import scala.scalanative.unsafe.*

opaque type Thread = Ptr[Byte]

type ThreadLocalKey = Ptr[Byte]

type OnceOnly = Ptr[Byte]

opaque type Mutex = Ptr[Byte]

object Mutex {

  extension (mutex: Mutex) {

    inline def withLock[A](f: => A): A = {
      LibUv.uv_mutex_lock(mutex)
      try f
      finally LibUv.uv_mutex_unlock(mutex)
    }

  }

}

type ReadWriteLock = Ptr[Byte]

type Semaphore = Ptr[Byte]

type Condition = Ptr[Byte]

type Barrier = Ptr[Byte]
