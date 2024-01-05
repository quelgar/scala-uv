# Scala Native bindings for libuv

**scala-uv** is a Scala Native library that provides Scala bindings for [libuv](https://libuv.org), which is a multi-platform asynchronous IO library written in C. libuv was originally developed for Node.js, but it's also used by other software projects.

Only Scala 3 is supported at this time.

## Getting it

```scala
libraryDependencies += "io.github.quelgar" %%% "scala-uv" % "0.0.1"
```

## Current status

Very early days, only some of the APIs are bound. What works so far:

* Error handling
* Event loop
* Async callbacks
* Read and write files
* TCP client and server

But the API is still in flux.

## Examples

### Async callback

Runs a callback once, then closes the handle:

```scala
import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*
import scalauv.*
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

      val asyncHandle = UvUtils.zoneAllocateHandle(HandleType.UV_ASYNC)
      uv_async_init(loop, asyncHandle, callback).checkErrorThrowIO()

      uv_async_send(asyncHandle).checkErrorThrowIO()

      println(s"Main before, done = $done")
      uv_run(loop, RunMode.DEFAULT).checkErrorThrowIO()
      println(s"Main after, done = $done")
    }

  }

}
```

### Test examples

See also the tests

* [TcpSpec.scala](src/test/scala/scalauv/TcpSpec.scala)
* [FileSpec.scala](src/test/scala/scalauv/FileSpec.scala)

## Conveniences

The `LibUv` objects provides the exact libuv API, but when using it directly you are basically writing C code with Scala syntax. A few convenienves are provided to make this less painful.

### Dealing with libuv failures

libuv functions that can fail return a negative integer on failure, with the value indicating the precise error. The possible error codes are in [errors.scala](src/main/scala/scalauv/errors.scala).

Pass an error code to `UvUtils.errorMessage` to get the human-readable error message as a Scala string.

Use `.checkErrorThrowIO()` on the result of a libuv function to throw an `IOException` if the function failed. Note this isn't useful inside a callback, since you definitely should *not* throw exceptions from a C callback.

```scala
uv_listen(serverTcpHandle, 128, onNewConnection).checkErrorThrowIO()
```

Use `.onFail` to run some cleanup if the function failed.

```scala
uv_write(writeReq, stream, buf, 1.toUInt, onWrite).onFail {
    stdlib.free(writeReq)
}
```

### Malloc C strings

While the `Zone` memory allocation API from Scala Native is very nice, it's not useful when the memory is freed in a different callback, as there won't be a shared lexical scope. So `mallocCString` converts a Scala string to a C string, allocating the memory the old-fashioned way.

### Other UvUtils methods

* allocate memory for requests
* allocate memory for handles
* allocate, use and free file system requests

---

Copyright 2024 Lachlan O'Dea

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
