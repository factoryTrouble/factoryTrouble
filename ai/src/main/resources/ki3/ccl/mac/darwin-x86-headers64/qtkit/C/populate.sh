#!/bin/sh
rm -rf Applications
SDK=/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX10.8.sdk
CFLAGS="-m64 -isysroot ${SDK} -mmacosx-version-min=10.6"; export CFLAGS
h-to-ffi.sh ${SDK}/System/Library/Frameworks/QTKit.framework/Headers/QTKit.h
