/*
   Copyright (C) 2009 Clozure Associates
   Copyright (C) 1994-2001 Digitool, Inc
   This file is part of Clozure CL.  

   Clozure CL is licensed under the terms of the Lisp Lesser GNU Public
   License , known as the LLGPL and distributed with Clozure CL as the
   file "LICENSE".  The LLGPL consists of a preamble and the LGPL,
   which is distributed with Clozure CL as the file "LGPL".  Where these
   conflict, the preamble takes precedence.  

   Clozure CL is referenced in the preamble as the "LIBRARY."

   The LLGPL is also available online at
   http://opensource.franz.com/preamble.html
*/

#include <stdio.h>
#include <stdarg.h>

#include "lisp.h"
#include "area.h"
#include "lisp-exceptions.h"
#include "lisp_globals.h"

/* More-or-less like c printf(); */
int Dprintf(const char *format, ...);


char *
print_lisp_object(LispObj);
