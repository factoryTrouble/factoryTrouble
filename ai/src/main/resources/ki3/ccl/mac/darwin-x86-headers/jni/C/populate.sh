#!/bin/sh
SDK=/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX10.8.sdk
if [ $# -eq 1 ]
then
SDK=$1
fi
CFLAGS="-m32 -msse2 -Wno-endif-labels -isysroot ${SDK} -mmacosx-version-min=10.8";export CFLAGS
rm -rf usr Developer
h-to-ffi.sh ${SDK}/System/Library/Frameworks/JavaVM.framework/Headers/jni.h

