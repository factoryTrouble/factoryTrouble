# this file generated when environment is closed
# refresh . to make sure sizes are right

wm deiconify .
update
wm withdraw .
if {[winfo screenwidth .] != 1366 || [winfo screenheight .] != 768 || [lindex [wm maxsize .] 0] != 1362 || [lindex [wm maxsize .] 1] != 741} {
  set size_mismatch 1
} else {
  set size_mismatch 0
}

if $size_mismatch {
  set reset_window_sizes [tk_messageBox -icon warning -title "Screen resolution changed" -type yesno \
                                         -message "The screen resolution is not the same as it was the last time the Environment was used.  Should the window positions reset to the defaults?"]
} else { set reset_window_sizes 0}
if {$reset_window_sizes != "yes"} {
  set window_config(.pgraph) 1366x705+22+34
  set changed_window_list(.pgraph) 1
  set window_config(.stepper) 503x550+433+109
  set changed_window_list(.stepper) 1
  set window_config(.declarative) 420x300+38+401
  set changed_window_list(.declarative) 1
  set window_config(.control_panel) 170x700+1176+34
  set changed_window_list(.control_panel) 1
  set window_config(.procedural) 500x400+16+328
  set changed_window_list(.procedural) 1
  set window_config(.buffers) 350x240+752+34
  set changed_window_list(.buffers) 1
  set window_config(.reload_response) 500x230+433+269
  set changed_window_list(.reload_response) 1
  set window_config(.copyright) 400x290+483+239
  set changed_window_list(.copyright) 1
  set window_config(.whynot) 200x300+583+234
  set changed_window_list(.whynot) 1
}
