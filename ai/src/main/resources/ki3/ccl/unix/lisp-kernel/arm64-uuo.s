/* Copyright (C) 2012 Clozure Associates */
/* This file is part of Clozure CL.   */

/* Clozure CL is licensed under the terms of the Lisp Lesser GNU Public */
/* License , known as the LLGPL and distributed with Clozure CL as the */
/* file "LICENSE".  The LLGPL consists of a preamble and the LGPL, */
/* which is distributed with Clozure CL as the file "LGPL".  Where these */
/* conflict, the preamble takes precedence.   */

/* Clozure CL is referenced in the preamble as the "LIBRARY." */

/* The LLGPL is also available online at */
/* http://opensource.franz.com/preamble.html */

/* Encode (some) exceptional conditions in aarch64 'hlt' instructions,
   which accept a 16-bit immediate operand.  The low 3 bits of this operand
   define the format of the upper 13 bits ; in general, there are 3 formats:
   nullary (the upper 13 bits contain arbitrary immediate values),
   unary (bits 7:3 encode a register (likely a GPR), bits 15:8 encode
   type or other info, and binary (bits 7:3 encode register operand 0,
   bits 12:8 encode register operand 1, and bits 15:13 encode extra info */

hlt_code_nullary = 0
hlt_code_unary_reg_not_lisptag = 1
hlt_code_unary_reg_not_fulltag = 2
hlt_code_unary_reg_not_subtag = 3
hlt_code_unary_reg_not_xtype = 4
hlt_code_unary_misc = 5
hlt_code_binary = 6

define(`uuo_error_reg_not_lisptag',`
        __(hlt #(hlt_code_unary_reg_not_lisptag|(gprval($1)<<3)|$2<<8))
        ')

define(`uuo_error_reg_not_fulltag',`
        __(hlt #(hlt_code_unary_reg_not_fulltag|(gprval($1)<<3)|$2<<8))
        ')
        
define(`uuo_alloc_trap',`
        __(hlt #(hlt_code_nullary|(0<<3)))
        ')
	
define(`uuo_error_not_callable',`
        __(hlt #(hlt_code_unary_misc|(gprval($1)<<3)|(0<<8)))
        ')

define(`uuo_error_no_throw_tag',`
        __(hlt #(hlt_code_unary_misc|(gprval($1)<<3)|(1<<8)))
        ')

define(`uuo_tlb_too_small',`
        __(hlt #(hlt_code_unary_misc|(gprval($1)<<3)|(2<<8)))
        ')

define(`uuo_error_unbound',`
        __(hlt #(hlt_code_unary_misc|(gprval($1)<<3)|(3<<8)))
        ')

define(`uuo_error_reg_not_xtype',`
        __(hlt #(hlt_code_unary_reg_not_xtype|(gprval($1)<<3)|($2<<8)))
        ')

define(`uuo_error_vector_bounds',`
        __(hlt #(hlt_code_binary|(gprval($1)<<3)|(gprval($2)<<8)|(0<<13)))
        ')
        