#
#   Copyright (C) 2005 Clozure Associates and contributors.
#   This file is part of Clozure CL.  
#
#   Clozure CL is licensed under the terms of the Lisp Lesser GNU Public
#   License , known as the LLGPL and distributed with Clozure CL as the
#   file "LICENSE".  The LLGPL consists of a preamble and the LGPL,
#   which is distributed with Clozure CL as the file "LGPL".  Where these
#   conflict, the preamble takes precedence.  
#
#   Clozure CL is referenced in the preamble as the "LIBRARY."
#
#   The LLGPL is also available online at
#   http://opensource.franz.com/preamble.html


SVN_REVISION := "$(shell svnversion || echo unknown)"

SDKROOT=/
OSVERSION=10.6

MDYNAMIC_NO_PIC = $(shell ($(CC) --help -v 2>&1 | grep -q -e "-mdynamic-no-pic") && /bin/echo "-mdynamic-no-pic")

VPATH = ..
RM = /bin/rm
LD = ld
LDFLAGS =  -macosx_version_min $(OSVERSION) -arch i386 -dynamic  -o $@ -e start -pagezero_size 0x11000 -seg1addr 0x00011000 -sectalign __TEXT __text 0x1000
AS = as
M4 = gm4
M4FLAGS = -DDARWIN -DX86 -DX8632
ASFLAGS = -arch i386 -g

# In Xcode 7, the clang integrated assember complains that .stabs
# directives are unsupported.  Work around this by using the system
# assembler.  (Apparently, we'll have to quit using .stabs directives
# at some point soon.)

# Since Xcode 7 only runs on Yosemite and later, use OS version as a
# heuristic to detect whether the -Q flag is available.  It seems to
# be available as far back as Lion, but on Snow Leopard, "as" doesn't
# know about -Q.

yosemite_plus := $(shell uname -r | awk -F. '{ if ($$1 >= 14) print "t" }')
ifeq ($(yosemite_plus),t)
ASFLAGS += -Q
endif

CDEFINES = -DDARWIN -DX86 -DX8632 -DSVN_REVISION=$(SVN_REVISION) -D_DARWIN_NO_64_BIT_INODE -DUSE_DTRACE
CDEBUG = -g
COPT = -O2
# Once in a while, -Wformat says something useful.  The odds are against that,
# however.
WFORMAT = -Wno-format
CC=cc
PLATFORM_H = platform-darwinx8632.h

.s.o:
	$(M4) $(M4FLAGS) -I../ $< | $(AS) $(ASFLAGS) -o $@
.c.o:
	$(CC) -include ../$(PLATFORM_H) -c -arch i386 $< $(CDEFINES) \
	$(CDEBUG) $(COPT) $(MDYNAMIC_NO_PIC) $(WFORMAT) \
	-I. -mmacosx-version-min=$(OSVERSION) -isysroot $(SDKROOT) -o $@

SPOBJ = x86-spjump32.o x86-spentry32.o x86-subprims32.o
ASMOBJ = x86-asmutils32.o imports.o

COBJ  = pmcl-kernel.o gc-common.o bits.o  \
	thread_manager.o lisp-debug.o image.o memory.o x86-gc.o \
	x86-utils.o \
	x86-exceptions.o unix-calls.o mach-o-image.o

DEBUGOBJ = lispdcmd.o plprint.o plsym.o x86_print.o xlbt.o
KERNELOBJ= imports.o $(COBJ) x86-asmutils32.o mach_exc_server.o

SPINC =	lisp.s m4macros.m4 x86-constants.s x86-macros.s errors.s x86-uuo.s x86-constants32.s lisp_globals.s

CHEADERS = area.h bits.h x86-constants.h lisp-errors.h gc.h lisp.h \
	lisp-exceptions.h lisp_globals.h macros.h memprotect.h image.h \
	threads.h lisptypes.h x86-constants32.h x86-exceptions.h \
	x86-utils.h \
	$(PLATFORM_H) constants.h os-darwin.h

# Subprims linked into the kernel ?
# Yes:

KSPOBJ= $(SPOBJ)
all:	../../dx86cl


# No:

# KSPOBJ=

OSEARLYLIBS = -lcrt1.o
OSLATELIBS = -lSystem

# is this needed?
#OSMIDDLELIBS = -lSystemStubs

OSLIBS = $(OSEARLYLIBS) $(OSMIDDLELIBS) $(OSLATELIBS)

../../dx86cl:	 $(KSPOBJ) $(KERNELOBJ) $(DEBUGOBJ) Makefile
	$(LD)  $(LDFLAGS) $(KSPOBJ) $(KERNELOBJ) $(DEBUGOBJ)   $(OSLIBS)


$(SPOBJ): $(SPINC)
$(ASMOBJ): $(SPINC)
$(COBJ): $(CHEADERS) probes.h
$(DEBUGOBJ): $(CHEADERS) lispdcmd.h

probes.h: probes.d
	dtrace -h -s $<


thread_manager.o: thread_manager.c 

mach_exc_server.o: mach_exc_server.c mach_exc.h

mach_exc.h mach_exc_server.c: mach_exc.defs
	mig -arch i386 -header /dev/null -user /dev/null \
		-sheader mach_exc.h -server mach_exc_server.c \
		 $<

cclean:
	$(RM) -f $(KERNELOBJ) $(DEBUGOBJ) ../../dx86cl 

# Some earlier versions of this Makefile built "subprims_r.o".  
# (That file is now defunct.)
clean:	cclean
	$(RM) -f $(SPOBJ) $(KSPOBJ) subprims_r.o

# retain file not here at the moment
strip:	../../dx86cl
	strip -s retain ../../dx86cl

