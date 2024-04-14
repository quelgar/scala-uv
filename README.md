# Scala Native bindings for libuv

> Artisanal, handcrafted Scala Native bindings for libuv.

**scala-uv** is a Scala Native library that provides Scala bindings for [libuv](https://libuv.org), which is a multi-platform asynchronous IO library written in C. libuv was originally developed for Node.js, but it's also used by other software projects.

Only Scala 3 is supported.

## Getting it

```scala
libraryDependencies += "io.github.quelgar" %%% "scala-uv" % "0.0.2"
```

## Current status

Very early days, most of the APIs have bindings, but not all. Many are not tested at all. What I've tried so far:

* Error handling
* Event loop
* Async callbacks
* Read and write files
* TCP client and server

But many details of the API are still in flux.

## Examples

### Async callback

Runs a callback once, then closes the handle:

```scala
import scalauv.*

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

    Zone {
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
```

### Test examples

See also the tests

* [TcpSpec.scala](src/test/scala/scalauv/TcpSpec.scala)
* [FileSpec.scala](src/test/scala/scalauv/FileSpec.scala)

## Differences from the C API

scala-uv tries to expose the exact C API of libuv as directly as possible. However, due to the nature of Scala Native, some changes are necessary.

### Functions

Most of the libuv functions can be found in the `LibUv` object, with the same name as the C function. The exceptions are the following:

* `uv_loop_configuration` — not yet supported
* `uv_fileno` — not yet supported
* `uv_poll_init_socket` — not yet supported
* Process handle functions — not yet supported
* `uv_socketpair` — not yet supported
* `uv_udp_open` — not yet supported
* `uv_fs_chown` — not yet supported
* `uv_fs_fchown` — not yet supported
* `uv_fs_lchown` — not yet supported
* `uv_fs_getosfhandle` — not yet supported
* `uv_open_osfhandle` — not yet supported

### Handles

The C handle type `uv_handle_t*` is represented by the `Handle` type in Scala. There are subtypes of `Handle` for each of the pseudo-subtypes of `uv_handle_t` in C, such as `AsyncHandle`, `TcpHandle`, etc.

Each type of handle has a companion object with methods to allocate the memory for that type of handle, for example `AsyncHandle.zoneAllocate()`, `TcpHandle.stackAllocate()`, etc.

The `HandleType` object has the handle type constants.

### Requests

Similarly, the C request type `uv_req_t*` is represented by the `Req` type in Scala. There are subtypes of `Req` for each of the pseudo-subtypes of `uv_req_t` in C, such as `WriteReq`, `ConnectReq`, etc.

Each type of request has a companion object with methods to allocate the memory for that type of request, for example `WriteReq.zoneAllocate()`, `ConnectReq.stackAllocate()`, etc.

The `ReqType` object has the request type constants.

### Buffers

The `Buffer` type is a Scala wrapper around the `uv_buf_t*` type in C. To allocate and initialize a new `Buffer` on the stack:

```scala
val size = 100
val base = stackAlloc[Byte](size)
val buffer = Buffer.stackAllocate(base, size)
```

### Error codes

The `ErrorCodes` object has all the error codes from libuv as Scala constants, with the same names as in C.

### Constants

Various objects provide the constant values needed to use various aspectes of the libuv API:

* `RunMode`
* `FileOpenFlags`
* `CreateMode`
* `AccessCheckMode`
* `PollEvent`
* `ProcessFlags`
* `StdioFlags`
* `TtyMode`
* `TtyVtermState`
* `UdpFlags`
* `Membership`
* `FsEvent`
* `FsType`
* `DirEntType`
* `ClockType`

## Conveniences

The `LibUv` object provides most othe exact libuv API, but when using it directly you are basically writing C code with Scala syntax. A few convenienves are provided to make this a little less painful.

### Dealing with libuv failures

libuv functions that can fail return a negative integer on failure, with the value indicating the precise error. The possible error codes are in [errors.scala](src/main/scala/scalauv/errors.scala).

Pass an error code to `UvUtils.errorMessage` to get the human-readable error message as a Scala string.

Use `.onFail` to run some cleanup if the function failed.

```scala
uv_write(writeReq, stream, buf, 1.toUInt, onWrite).onFail {
    stdlib.free(writeReq)
}
```

Use `.checkErrorThrowIO()` on the result of a libuv function to throw an `IOException` if the function failed. Note this isn't useful inside a callback, since you definitely should *not* throw exceptions from a C callback.

```scala
uv_listen(serverTcpHandle, 128, onNewConnection).checkErrorThrowIO()
```

When using a callback-based library like libuv, it is common that when everything works, cleanup like freeing memory must be done in a different callback function. However if something fails, we need to immediately cleanup anything we've allocated already. We can use `.checkErrorThrowIO()` with `try`/`catch` to do this, but we need to mainain some `var`s to keep track of how far we got:

```scala
def onClose: CloseCallback = (_: Handle).free()

def onNewConnection: ConnectionCallback = {
  (handle: StreamHandle, status: ErrorCode) =>
    val loop = uv_handle_get_loop(handle)
    var clientTcpHandle: TcpHandle = null
    var initialized = false
    try {
      status.checkErrorThrowIO()
      clientTcpHandle = TcpHandle.malloc()
      uv_tcp_init(loop, clientTcpHandle).checkErrorThrowIO()
      initialized = true
      uv_handle_set_data(clientTcpHandle, handle.toPtr)
      uv_accept(handle, clientTcpHandle).checkErrorThrowIO()
      uv_read_start(clientTcpHandle, allocBuffer, onRead)
        .checkErrorThrowIO()
      ()
    } catch { 
      case e: IOException =>
        if (initialized)
          // note the onClose callback will free the handle
          uv_close(clientTcpHandle, onClose)
        else if (clientTcpHandle != null)
          clientTcpHandle.free()
        setFailed(exception.getMessage())
    }
}
```

As an alternative, scala-uv provides `UvUtils.attemptCatch` to make scenarios such as this easier. Within an `attemptCatch` block, you can register cleanup actions at any point using `UvUtils.onFail`. These cleanup actions will be performed (in reverse order to the order registered) if an exception is thrown. If the code block completes, no cleanup is performed. A function to handle the exception must also be provided. This simplifies the above example to:


```scala
def onClose: CloseCallback = (_: Handle).free()

def onNewConnection: ConnectionCallback = {
  (handle: StreamHandle, status: ErrorCode) =>
    val loop = uv_handle_get_loop(handle)
    UvUtils.attemptCatch {
      status.checkErrorThrowIO()
      val clientTcpHandle = TcpHandle.malloc()
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
```

`UvUtils.attemptCatch` is designed for use in callback functions where you don't want to throw any exceptions. There is also `UvUtils.attempt`, which runs the cleanup actions but does not catch the exception.

### File I/O

The libuv file I/O functions support use of multiple buffers at once. scala-uv provides the `IOVector` type for working with multiple buffers with varying amoutns of data.

scala-uv provides a shortcut for allocating, using and freeing `FileReq` objects, if you are doing *blocking* I/O: `FileReq.use`.

```scala
// writes the C string pointed to by `cText` to a file
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
```


### Malloc C strings

While the `Zone` memory allocation API from Scala Native is very nice, it's not useful when the memory is freed in a different callback, as there won't be a shared lexical scope. So `mallocCString` converts a Scala string to a C string, allocating the memory the old-fashioned way.

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
