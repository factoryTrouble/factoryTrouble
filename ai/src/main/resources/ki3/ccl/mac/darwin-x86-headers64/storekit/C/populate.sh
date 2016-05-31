#!/bin/sh
# Current versions of the FFI translator have difficulty dealing with
# the "typed enum" construct used to define the SKDownloadState type
# in <Storekik/SKDownload.h>.  To partially work around that:
# copy <Storekit/StoreKit.h> to this directory.
# edit the copy, changing
#  #import <StoreKit/SKDownload.h>
# to
#  #import "SKDownload.h"
# copy <StoreKit/SKDownload.h> to this directory
# edit the copy of that file, changing
#  typedef enum : NSInteger
# to
#  typedef enum
# that (incorrectly) defines the SKDownloadState enumeration type
# to be 'int' instead of 'NSInteger', which barely matters as of 10.8
rm -rf Applications
SDK=/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX10.8.sdk
CFLAGS="-m64 -isysroot ${SDK} -mmacosx-version-min=10.8"; export CFLAGS
h-to-ffi.sh `pwd`/StoreKit.h
