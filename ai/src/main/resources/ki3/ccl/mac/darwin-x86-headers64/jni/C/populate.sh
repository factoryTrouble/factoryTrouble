#!/bin/sh
rm -rf Applications
SDK=/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX10.8.sdk
if [ $# -eq 1 ]
then
SDK=$1
fi
CFLAGS="-m64 -isysroot ${SDK} -mmacosx-version-min=10.6"; export CFLAGS
h-to-ffi.sh ${SDK}/System/Library/Frameworks/JavaVM.framework/Headers/jni.h

