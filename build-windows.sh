#!/bin/bash

repo_dir=$(cygpath --mixed $(pwd))

cd libuv-build

mkdir -p build

(cd build && cmake ..)
cmake --build build

echo SBT_NATIVE_CONFIG="set nativeConfig ~= (_.withCompileOptions(_ :+ \"--include-directory=$repo_dir/libuv-build/include\").withLinkingOptions(_ :+ \"--library-directory=$repo_dir/libuv-build/build/Debug\")) ; " >> $GITHUB_ENV
