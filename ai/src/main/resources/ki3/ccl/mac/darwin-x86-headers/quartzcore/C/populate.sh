#!/bin/sh
rm -rf System Developer usr
SDK=/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX10.8.sdk
if [ $# -eq 1 ]
then
SDK=$1
fi
CFLAGS="-m32 -msse2 -Wno-multichar -isysroot ${SDK} -mmacosx-version-min=10.8" ; export CFLAGS
h-to-ffi.sh ${SDK}/System/Library/Frameworks/QuartzCore.framework/Headers/QuartzCore.h

