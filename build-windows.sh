#!/bin/bash

cd libuv-build

mkdir -p build

(cd build && cmake ..)
cmake --build build

find build

export CPATH="libuv-build/build"

export LIBRARY_PATH="libuv-build/build"
