package org.mjd.nativesocket.internal.sockets.posix;

import java.net.Socket;

import com.google.common.primitives.Ints;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import org.mjd.nativesocket.NativeSocket;
import org.mjd.nativesocket.TimeDuration;
import org.mjd.nativesocket.internal.fd.FileDescriptorAccessor;
import org.mjd.nativesocket.internal.fd.FileDescriptorAccessor.FileDescriptorException;
import org.mjd.nativesocket.internal.jna.CBool;
import org.mjd.nativesocket.internal.jna.StandardLib;
import org.mjd.nativesocket.staticfactories.NativeSocketStaticFactory;


/**
 * {@link NativeSocket} implementation for POSIX compliant socket.
 *
 * Uses JNA and the standard C library to provide functionality for an OS agnostic POSIX compliant socket.
 */
public final class PosixSocket implements NativeSocket
{
    private static final int SOL_SOCKET = 1;
    private static final int SOL_TCP = 6;
    private static final int TCP_KEEPIDLE = 4;
    private static final int TCP_KEEPINTVL = 5;
    private static final int TCP_KEEPCNT = 6;
    private static final int SO_KEEPALIVE = 9;
    private static final int SET_OPT_ERROR = -1;
    private final int fd;
    private final StandardLib stdLib;
    private final FileDescriptorAccessor fdAccessor;
    private final PosixSocketLibrary sockLib;


    /**
     * Do not use this directly, instead use the {@link NativeSocketStaticFactory}
     *
     * Constructs a {@link NativeSocket} for a Linux system, fully initialised and ready to use.
     *
     * <p>
     * Requires the standard C library and sys/socket.h
     *
     * @param socket
     *            the socket to wrap by this {@link NativeSocket}
     * @param standardLib
     *            strategy used to load the OS specific standard library.
     * @param fileDescriptorAccessor
     *            strategy used to extract file descriptor.
     *
     * @see NativeSocket
     */
    public PosixSocket(final Socket socket, StandardLib standardLib, FileDescriptorAccessor fileDescriptorAccessor)
    {
        this.stdLib = standardLib;
        this.fdAccessor = fileDescriptorAccessor;
        try
        {
            sockLib = stdLib.loadStandardLibrary(PosixSocketLibrary.class);
            fd = Ints.checkedCast(fdAccessor.extractFileDescriptor(socket));
        }
        catch (FileDescriptorException e)
        {
            throw new IllegalStateException("The file descriptor cannot be determined for the given socket", e);
        }
    }

    @Override
    public void setKeepAlive(TimeDuration idleTime, TimeDuration interval, TimeDuration probes) {
        IntByReference newTime = new IntByReference(Ints.checkedCast(idleTime.getStandardSeconds()));
        IntByReference newInterval = new IntByReference(Ints.checkedCast(interval.getStandardSeconds()));
        IntByReference newProbes = new IntByReference(Ints.checkedCast(probes.getStandardSeconds()));

        enableKeepAlive();
        socketCall(sockLib.setsockopt(fd, SOL_TCP, TCP_KEEPIDLE, newTime.getPointer(), Ints.BYTES));
        socketCall(sockLib.setsockopt(fd, SOL_TCP, TCP_KEEPINTVL, newInterval.getPointer(), Ints.BYTES));
        socketCall(sockLib.setsockopt(fd, SOL_TCP, TCP_KEEPCNT, newProbes.getPointer(), Ints.BYTES));
    }

    @Override
    public KeepAliveData getKeepAliveData()
    {
        IntByReference enabled = new IntByReference();
        IntByReference interval = new IntByReference();
        IntByReference idle = new IntByReference();
        IntByReference probes = new IntByReference();
        Pointer optionLen = new IntByReference(Ints.BYTES).getPointer();

        socketCall(sockLib.getsockopt(fd, SOL_SOCKET, SO_KEEPALIVE, enabled.getPointer(), optionLen));
        socketCall(sockLib.getsockopt(fd, SOL_TCP, TCP_KEEPINTVL, interval.getPointer(), optionLen));
        socketCall(sockLib.getsockopt(fd, SOL_TCP, TCP_KEEPIDLE, idle.getPointer(), optionLen));
        socketCall(sockLib.getsockopt(fd, SOL_TCP, TCP_KEEPCNT, probes.getPointer(), optionLen));

        return new KeepAliveData(CBool.fromInt(enabled.getValue()).toBoolean(), probes.getValue(), interval.getValue(),
                                 idle.getValue());
    }

    private void enableKeepAlive()
    {
        socketCall(sockLib.setsockopt(fd, SOL_SOCKET, SO_KEEPALIVE, CBool.TRUE_PTR, CBool.SIZE));
    }

    /**
     * Processes the last OS error and throw an {@link IllegalStateException} when there is an error on a
     * native socket library call. Used to handle return codes from C calls to socket related functions.
     *
     * @param result
     *            the return code.
     */
    public static void socketCall(int result)
    {
        if (result == SET_OPT_ERROR)
        {
            throw new IllegalStateException("Setting socket option failed; the last OS error = " +
                            Native.getLastError());
        }
    }
}
