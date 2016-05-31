#!/bin/sh
rm -rf Applications
SDK=/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX10.8.sdk
if [ $# -eq 1 ]
then
SDK=$1
fi 
CFLAGS="-m64 -isysroot ${SDK} -mmacosx-version-min=10.6"; export CFLAGS
h-to-ffi.sh ${SDK}/System/Library/Frameworks/OpenGL.framework/Headers/OpenGL.h
h-to-ffi.sh ${SDK}/System/Library/Frameworks/OpenGL.framework/Headers/glu.h
h-to-ffi.sh ${SDK}/System/Library/Frameworks/GLUT.framework/Headers/glut.h
h-to-ffi.sh ${SDK}/System/Library/Frameworks/AGL.framework/Headers/agl.h
