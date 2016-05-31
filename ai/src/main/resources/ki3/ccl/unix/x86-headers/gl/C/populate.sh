#!/bin/sh
ARCH_SUBDIR=i386-linux-gnu
CFLAGS="-isystem /usr/include/$ARCH_SUBDIR -m32 -D_GNU_SOURCE $CFLAGS";export CFLAGS
rm -rf usr
h-to-ffi.sh /usr/include/GL/glx.h
h-to-ffi.sh /usr/include/GL/glu.h
h-to-ffi.sh /usr/include/GL/glut.h
