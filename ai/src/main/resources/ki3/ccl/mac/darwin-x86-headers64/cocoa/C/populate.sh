#!/bin/sh
rm -rf Applications
SDK=/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX10.8.sdk
CFLAGS="-m64 -isysroot ${SDK} -mmacosx-version-min=10.6"; export CFLAGS
h-to-ffi.sh ${SDK}/usr/include/objc/objc-runtime.h
h-to-ffi.sh ${SDK}/usr/include/objc/objc-exception.h
h-to-ffi.sh ${SDK}/usr/include/objc/Object.h
h-to-ffi.sh ${SDK}/usr/include/objc/Protocol.h
h-to-ffi.sh ${SDK}/System/Library/Frameworks/Cocoa.framework/Headers/Cocoa.h
