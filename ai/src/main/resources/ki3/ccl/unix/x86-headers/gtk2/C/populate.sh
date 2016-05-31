#!/bin/sh
ARCH_SUBDIR=i386-linux-gnu
CFLAGS="-isystem /usr/include/$ARCH_SUBDIR -m32 -D_GNU_SOURCE $CFLAGS";export CFLAGS
rm -rf usr

h-to-ffi.sh `pkg-config --cflags gtk+-2.0` /usr/include/gtk-2.0/gtk/gtk.h
