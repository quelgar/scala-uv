#!/bin/bash

cd libuv-build

mkdir -p build

(cd build && cmake ..)
cmake --build build

find build

echo CL="/I libuv-build\\include" >> $GITHUB_ENV

echo LIB="libuv-build\\build\\Debug" >> $GITHUB_ENV
