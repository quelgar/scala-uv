#!/bin/bash

cd libuv-build

mkdir -p build

(cd build && cmake ..)
cmake --build build

find build

echo SBT_NATIVE_COMPILE='set nativeCompileOptions += "--include-directory=libuv-build/include" ; ' >> $GITHUB_ENV

echo SBT_NATIVE_LINK='set nativeLinkingOptions += "--library-directory=libuv-build/build/Debug" ; ' >> $GITHUB_ENV
