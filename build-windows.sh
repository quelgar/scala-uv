#!/bin/bash

cd libuv-build

mkdir -p build

(cd build && cmake ..)
cmake --build build

find build

echo CPATH="libuv-build/include" >> $GITHUB_ENV

echo LIBRARY_PATH="libuv-build/build/Debug" >> $GITHUB_ENV
