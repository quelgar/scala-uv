#!/bin/bash

repo_dir=$(pwd)

cd libuv-build

mkdir -p build

(cd build && cmake ..)
cmake --build build

find build

echo SBT_NATIVE_COMPILE="set nativeCompileOptions += \"--include-directory=$repo_dir/libuv-build/include\" ; " >> $GITHUB_ENV

echo SBT_NATIVE_LINK="set nativeLinkingOptions += \"--library-directory=$repo_dir/libuv-build/build/Debug\" ; " >> $GITHUB_ENV
