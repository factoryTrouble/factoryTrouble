#!/bin/sh
# Note that it may be necessary to patch <sys/procfs.h>, since
# it (mis)uses features not supported by GCC 4.0.  See
# <http://gcc.gnu.org/ml/gcc/2005-01/msg00509.html>
ARCH_SUBDIR=i386-linux-gnu
CFLAGS="-isystem /usr/include/$ARCH_SUBDIR -m32 -D_GNU_SOURCE $CFLAGS";export CFLAGS
rm -rf usr

arch_specific_file()
{
 if [ -e /usr/include/$ARCH_SUBDIR/$1 ]; then
  echo /usr/include/$ARCH_SUBDIR/$1
 elif [ -e /usr/include/$1 ]; then
  echo /usr/include/$1
 else
  echo /not/found/$1
 fi
}

h-to-ffi.sh /usr/include/_G_config.h
h-to-ffi.sh `arch_specific_file a.out.h`
h-to-ffi.sh /usr/include/aio.h
h-to-ffi.sh /usr/include/aliases.h
h-to-ffi.sh /usr/include/alloca.h
h-to-ffi.sh /usr/include/ar.h
h-to-ffi.sh /usr/include/argp.h
h-to-ffi.sh /usr/include/argz.h
h-to-ffi.sh /usr/include/arpa/ftp.h
h-to-ffi.sh /usr/include/arpa/inet.h
h-to-ffi.sh /usr/include/arpa/nameser.h
h-to-ffi.sh /usr/include/arpa/telnet.h
h-to-ffi.sh /usr/include/arpa/tftp.h
h-to-ffi.sh /usr/include/assert.h
h-to-ffi.sh /usr/include/byteswap.h
h-to-ffi.sh /usr/include/complex.h
h-to-ffi.sh /usr/include/cpio.h
h-to-ffi.sh /usr/include/crypt.h
h-to-ffi.sh /usr/include/ctype.h
#h-to-ffi.sh /usr/include/db1/db.h
#h-to-ffi.sh /usr/include/db1/mpool.h
#h-to-ffi.sh /usr/include/db1/ndbm.h
h-to-ffi.sh /usr/include/dirent.h
h-to-ffi.sh -D _GNU_SOURCE  /usr/include/dlfcn.h
h-to-ffi.sh /usr/include/elf.h
h-to-ffi.sh /usr/include/endian.h
h-to-ffi.sh /usr/include/envz.h
h-to-ffi.sh /usr/include/err.h
h-to-ffi.sh /usr/include/errno.h
h-to-ffi.sh /usr/include/error.h
h-to-ffi.sh /usr/include/execinfo.h
h-to-ffi.sh /usr/include/fcntl.h
h-to-ffi.sh /usr/include/features.h
h-to-ffi.sh /usr/include/fenv.h
h-to-ffi.sh /usr/include/fmtmsg.h
h-to-ffi.sh /usr/include/fnmatch.h
h-to-ffi.sh `arch_specific_file fpu_control.h`
h-to-ffi.sh /usr/include/fstab.h
h-to-ffi.sh /usr/include/fts.h
h-to-ffi.sh /usr/include/ftw.h
h-to-ffi.sh /usr/include/gconv.h
h-to-ffi.sh /usr/include/getopt.h
h-to-ffi.sh /usr/include/glob.h
h-to-ffi.sh /usr/include/gnu-versions.h
h-to-ffi.sh `arch_specific_file gnu/lib-names.h`
h-to-ffi.sh `arch_specific_file gnu/libc-version.h`
h-to-ffi.sh `arch_specific_file gnu/stubs.h`
h-to-ffi.sh /usr/include/grp.h
h-to-ffi.sh /usr/include/iconv.h
h-to-ffi.sh `arch_specific_file ieee754.h`
h-to-ffi.sh /usr/include/ifaddrs.h
h-to-ffi.sh /usr/include/inttypes.h
h-to-ffi.sh /usr/include/langinfo.h
h-to-ffi.sh /usr/include/lastlog.h
h-to-ffi.sh /usr/include/libgen.h
h-to-ffi.sh /usr/include/libintl.h
h-to-ffi.sh /usr/include/libio.h
#h-to-ffi.sh /usr/include/limits.h
h-to-ffi.sh /usr/include/link.h
h-to-ffi.sh /usr/include/locale.h
h-to-ffi.sh /usr/include/malloc.h
h-to-ffi.sh /usr/include/math.h
h-to-ffi.sh /usr/include/mcheck.h
h-to-ffi.sh /usr/include/memory.h
h-to-ffi.sh /usr/include/mntent.h
h-to-ffi.sh /usr/include/monetary.h
h-to-ffi.sh /usr/include/net/ethernet.h
h-to-ffi.sh /usr/include/net/if.h
h-to-ffi.sh /usr/include/net/if_arp.h
h-to-ffi.sh /usr/include/net/if_packet.h
h-to-ffi.sh -include `arch_specific_file sys/capability.h` /usr/include/net/if_ppp.h
h-to-ffi.sh /usr/include/net/if_shaper.h
h-to-ffi.sh /usr/include/net/if_slip.h
h-to-ffi.sh -include `arch_specific_file sys/capability.h` -include /usr/include/net/ppp_defs.h /usr/include/net/ppp-comp.h
h-to-ffi.sh /usr/include/net/route.h
h-to-ffi.sh /usr/include/netash/ash.h
#h-to-ffi.sh -include `arch_specific_file sys/socket.h` /usr/include/netatalk/at.h
h-to-ffi.sh /usr/include/netax25/ax25.h
h-to-ffi.sh /usr/include/netdb.h
h-to-ffi.sh /usr/include/neteconet/ec.h
h-to-ffi.sh /usr/include/netinet/ether.h
h-to-ffi.sh /usr/include/netinet/icmp6.h
h-to-ffi.sh /usr/include/netinet/if_ether.h
#h-to-ffi.sh /usr/include/netinet/if_fddi.h
#h-to-ffi.sh /usr/include/netinet/if_tr.h
h-to-ffi.sh /usr/include/netinet/igmp.h
h-to-ffi.sh /usr/include/netinet/in.h
h-to-ffi.sh /usr/include/netinet/in_systm.h
h-to-ffi.sh /usr/include/netinet/ip.h
h-to-ffi.sh /usr/include/netinet/ip6.h
h-to-ffi.sh /usr/include/netinet/ip_icmp.h
h-to-ffi.sh /usr/include/netinet/tcp.h
h-to-ffi.sh /usr/include/netinet/udp.h
h-to-ffi.sh /usr/include/netipx/ipx.h
h-to-ffi.sh /usr/include/netpacket/packet.h
h-to-ffi.sh /usr/include/netrom/netrom.h
h-to-ffi.sh -include /usr/include/netax25/ax25.h /usr/include/netrose/rose.h
h-to-ffi.sh /usr/include/nfs/nfs.h
h-to-ffi.sh /usr/include/nl_types.h
h-to-ffi.sh /usr/include/nss.h
h-to-ffi.sh /usr/include/obstack.h
h-to-ffi.sh /usr/include/paths.h
h-to-ffi.sh -include `arch_specific_file sys/types.h` -include `arch_specific_file sys/time.h`  -include /usr/include/stdio.h -include /usr/include/pcap-bpf.h /usr/include/pcap-namedb.h
h-to-ffi.sh /usr/include/pcap.h
h-to-ffi.sh /usr/include/pci/config.h
h-to-ffi.sh /usr/include/pci/header.h
h-to-ffi.sh /usr/include/pci/pci.h
h-to-ffi.sh /usr/include/poll.h
h-to-ffi.sh /usr/include/printf.h
h-to-ffi.sh /usr/include/protocols/routed.h
h-to-ffi.sh /usr/include/protocols/rwhod.h
h-to-ffi.sh /usr/include/protocols/talkd.h
h-to-ffi.sh /usr/include/protocols/timed.h
h-to-ffi.sh /usr/include/pthread.h
h-to-ffi.sh /usr/include/pty.h
h-to-ffi.sh /usr/include/pwd.h
h-to-ffi.sh -include `arch_specific_file sys/types.h` /usr/include/re_comp.h
h-to-ffi.sh -include `arch_specific_file sys/types.h` /usr/include/regex.h 
#h-to-ffi.sh /usr/include/regexp.h
h-to-ffi.sh /usr/include/rpc/auth.h
h-to-ffi.sh /usr/include/rpc/auth_des.h
h-to-ffi.sh /usr/include/rpc/auth_unix.h
h-to-ffi.sh /usr/include/rpc/clnt.h
h-to-ffi.sh /usr/include/rpc/des_crypt.h
h-to-ffi.sh /usr/include/rpc/key_prot.h
h-to-ffi.sh /usr/include/rpc/netdb.h
h-to-ffi.sh /usr/include/rpc/pmap_clnt.h
h-to-ffi.sh /usr/include/rpc/pmap_prot.h
h-to-ffi.sh /usr/include/rpc/pmap_rmt.h
h-to-ffi.sh /usr/include/rpc/rpc.h
h-to-ffi.sh /usr/include/rpc/rpc_des.h
h-to-ffi.sh /usr/include/rpc/rpc_msg.h
h-to-ffi.sh /usr/include/rpc/svc.h
h-to-ffi.sh /usr/include/rpc/svc_auth.h
h-to-ffi.sh /usr/include/rpc/types.h
h-to-ffi.sh /usr/include/rpc/xdr.h
h-to-ffi.sh /usr/include/rpcsvc/bootparam.h
h-to-ffi.sh /usr/include/rpcsvc/bootparam_prot.h
h-to-ffi.sh /usr/include/rpcsvc/key_prot.h
h-to-ffi.sh /usr/include/rpcsvc/klm_prot.h
h-to-ffi.sh /usr/include/rpcsvc/mount.h
h-to-ffi.sh /usr/include/rpcsvc/nfs_prot.h
h-to-ffi.sh /usr/include/rpcsvc/nis.h
h-to-ffi.sh /usr/include/rpcsvc/nlm_prot.h
h-to-ffi.sh /usr/include/rpcsvc/rex.h
h-to-ffi.sh /usr/include/rpcsvc/rquota.h
h-to-ffi.sh /usr/include/rpcsvc/rstat.h
h-to-ffi.sh /usr/include/rpcsvc/rusers.h
h-to-ffi.sh /usr/include/rpcsvc/sm_inter.h
h-to-ffi.sh /usr/include/rpcsvc/spray.h
h-to-ffi.sh /usr/include/rpcsvc/yp.h
h-to-ffi.sh /usr/include/rpcsvc/yp_prot.h
h-to-ffi.sh /usr/include/rpcsvc/ypclnt.h
h-to-ffi.sh /usr/include/rpcsvc/yppasswd.h
h-to-ffi.sh /usr/include/rpcsvc/ypupd.h
h-to-ffi.sh /usr/include/sched.h
h-to-ffi.sh /usr/include/scsi/scsi.h
h-to-ffi.sh /usr/include/scsi/scsi_ioctl.h
h-to-ffi.sh -include `arch_specific_file sys/types.h` /usr/include/scsi/sg.h
h-to-ffi.sh /usr/include/search.h
h-to-ffi.sh /usr/include/semaphore.h
h-to-ffi.sh /usr/include/setjmp.h
h-to-ffi.sh /usr/include/sgtty.h
h-to-ffi.sh /usr/include/shadow.h
h-to-ffi.sh /usr/include/spawn.h
h-to-ffi.sh /usr/include/signal.h
h-to-ffi.sh /usr/include/stab.h
#h-to-ffi.sh /usr/include/stack-alloc.h
h-to-ffi.sh /usr/include/stdint.h
h-to-ffi.sh /usr/include/stdio.h
h-to-ffi.sh -D_GNU_SOURCE /usr/include/stdlib.h
h-to-ffi.sh /usr/include/string.h
h-to-ffi.sh /usr/include/strings.h
h-to-ffi.sh /usr/include/stropts.h
h-to-ffi.sh `arch_specific_file sys/acct.h`
h-to-ffi.sh `arch_specific_file sys/bitypes.h`
h-to-ffi.sh `arch_specific_file sys/cdefs.h`
h-to-ffi.sh `arch_specific_file sys/dir.h`
h-to-ffi.sh `arch_specific_file sys/errno.h`
h-to-ffi.sh `arch_specific_file sys/fcntl.h`
h-to-ffi.sh `arch_specific_file sys/file.h`
h-to-ffi.sh `arch_specific_file sys/fsuid.h`
h-to-ffi.sh `arch_specific_file sys/gmon.h`
h-to-ffi.sh `arch_specific_file sys/gmon_out.h`
h-to-ffi.sh `arch_specific_file sys/ioctl.h`
h-to-ffi.sh `arch_specific_file sys/ipc.h`
h-to-ffi.sh `arch_specific_file sys/kd.h`
h-to-ffi.sh `arch_specific_file sys/kdaemon.h`
h-to-ffi.sh `arch_specific_file sys/klog.h`
h-to-ffi.sh `arch_specific_file sys/mman.h`
h-to-ffi.sh `arch_specific_file sys/mount.h`
h-to-ffi.sh `arch_specific_file sys/msg.h`
h-to-ffi.sh `arch_specific_file sys/mtio.h`
h-to-ffi.sh `arch_specific_file sys/param.h`
h-to-ffi.sh `arch_specific_file sys/pci.h`
h-to-ffi.sh `arch_specific_file sys/poll.h`
h-to-ffi.sh `arch_specific_file sys/prctl.h`
h-to-ffi.sh `arch_specific_file sys/procfs.h`
h-to-ffi.sh `arch_specific_file sys/profil.h`
h-to-ffi.sh `arch_specific_file sys/ptrace.h`
h-to-ffi.sh `arch_specific_file sys/queue.h`
h-to-ffi.sh `arch_specific_file sys/quota.h`
h-to-ffi.sh `arch_specific_file sys/raw.h`
h-to-ffi.sh `arch_specific_file sys/reboot.h`
h-to-ffi.sh `arch_specific_file sys/resource.h`
h-to-ffi.sh `arch_specific_file sys/select.h`
h-to-ffi.sh `arch_specific_file sys/sem.h`
h-to-ffi.sh `arch_specific_file sys/sendfile.h`
h-to-ffi.sh `arch_specific_file sys/shm.h`
h-to-ffi.sh `arch_specific_file sys/signal.h`
h-to-ffi.sh `arch_specific_file sys/socket.h`
h-to-ffi.sh `arch_specific_file sys/socketvar.h`
h-to-ffi.sh `arch_specific_file sys/soundcard.h`
h-to-ffi.sh `arch_specific_file sys/stat.h`
h-to-ffi.sh `arch_specific_file sys/statfs.h`
h-to-ffi.sh `arch_specific_file sys/statvfs.h`
h-to-ffi.sh `arch_specific_file sys/stropts.h`
h-to-ffi.sh `arch_specific_file sys/swap.h`
h-to-ffi.sh `arch_specific_file sys/syscall.h`
h-to-ffi.sh `arch_specific_file sys/sysctl.h`
h-to-ffi.sh `arch_specific_file sys/sysinfo.h`
h-to-ffi.sh `arch_specific_file sys/syslog.h`
h-to-ffi.sh `arch_specific_file sys/sysmacros.h`
h-to-ffi.sh `arch_specific_file sys/termios.h`
h-to-ffi.sh `arch_specific_file sys/time.h`
h-to-ffi.sh `arch_specific_file sys/timeb.h`
h-to-ffi.sh `arch_specific_file sys/times.h`
h-to-ffi.sh `arch_specific_file sys/timex.h`
h-to-ffi.sh `arch_specific_file sys/ttychars.h`
h-to-ffi.sh `arch_specific_file sys/ttydefaults.h`
h-to-ffi.sh `arch_specific_file sys/types.h`
h-to-ffi.sh `arch_specific_file sys/ucontext.h`
h-to-ffi.sh `arch_specific_file sys/uio.h`
h-to-ffi.sh `arch_specific_file sys/ultrasound.h`
h-to-ffi.sh `arch_specific_file sys/un.h`
h-to-ffi.sh `arch_specific_file sys/unistd.h`
h-to-ffi.sh -include `arch_specific_file sys/types.h` `arch_specific_file sys/user.h`
h-to-ffi.sh `arch_specific_file sys/ustat.h`
h-to-ffi.sh `arch_specific_file sys/utsname.h`
h-to-ffi.sh `arch_specific_file sys/vfs.h`
h-to-ffi.sh `arch_specific_file sys/vlimit.h`
h-to-ffi.sh `arch_specific_file sys/vt.h`
h-to-ffi.sh `arch_specific_file sys/vtimes.h`
h-to-ffi.sh `arch_specific_file sys/wait.h`
h-to-ffi.sh /usr/include/syscall.h
h-to-ffi.sh /usr/include/sysexits.h
h-to-ffi.sh /usr/include/syslog.h
h-to-ffi.sh /usr/include/tar.h
h-to-ffi.sh /usr/include/termio.h
h-to-ffi.sh /usr/include/termios.h
h-to-ffi.sh /usr/include/tgmath.h
h-to-ffi.sh /usr/include/thread_db.h
h-to-ffi.sh /usr/include/time.h
h-to-ffi.sh /usr/include/ttyent.h
h-to-ffi.sh /usr/include/ucontext.h
h-to-ffi.sh /usr/include/ulimit.h
h-to-ffi.sh /usr/include/unistd.h
h-to-ffi.sh /usr/include/ustat.h
h-to-ffi.sh /usr/include/utime.h
h-to-ffi.sh /usr/include/utmp.h
h-to-ffi.sh /usr/include/utmpx.h
h-to-ffi.sh /usr/include/values.h
h-to-ffi.sh /usr/include/wait.h
h-to-ffi.sh /usr/include/wchar.h
h-to-ffi.sh /usr/include/wctype.h
h-to-ffi.sh /usr/include/wordexp.h
h-to-ffi.sh /usr/include/xlocale.h
h-to-ffi.sh /usr/include/zlib.h
