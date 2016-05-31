#!/bin/sh
if [ -e /usr/lib/jvm/java-6-openjdk-i386/include ]; then
 JVMINCLUDE=/usr/lib/jvm/java-6-openjdk-i386/include
elif [ -e /usr/lib/jvm/java-6-openjdk/include ]; then
 JVMINCLUDE=/usr/lib/jvm/java-6-openjdk/include
else
 echo Fix JVMINCLUDE and try again ...
fi
ARCH_SUBDIR=i386-linux-gnu
CFLAGS="-isystem /usr/include/$ARCH_SUBDIR -m32 -I${JVMINCLUDE}/linux";export CFLAGS
rm -rf usr
h-to-ffi.sh ${JVMINCLUDE}/jni.h
