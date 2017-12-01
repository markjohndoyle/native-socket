package org.mjd.nativesocket.internal;

import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import com.google.common.primitives.Ints;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import org.joda.time.Duration;
import org.mjd.nativesocket.NativeSocket;
import org.mjd.nativesocket.NativeSocketFactory;

import static org.mjd.nativesocket.internal.LinuxUtils.extractFileDescriptor;
import static org.mjd.nativesocket.internal.LinuxUtils.loadStandardLibrary;
import static org.mjd.nativesocket.internal.LinuxUtils.callC;


/**
 * {@link NativeSocket} implementation for Linux.
 * 
 * Uses JNA and the standard C library to provide functionality.
 */
public final class LinuxSocket implements NativeSocket
{
    private static final int SOL_SOCKET = 1;
    private static final int SO_KEEPALIVE = 9;
    private final int fd;
    private final SocketLibrary sockLib = loadStandardLibrary(SocketLibrary.class);


    /**
     * Do not use this directly, instead use the {@link NativeSocketFactory}
     * 
     * Constructs a {@link NativeSocket} for a Linux system, fully initialised and ready to use.
     * 
     * <p>
     * Requires the standard C library and sys/socket.h
     * 
     * @param socket
     *            the socket to wrap by this {@link NativeSocket}
     * 
     * @See {@link NativeSocketFactory}
     */
    public LinuxSocket(final Socket socket)
    {
        try
        {
            fd = extractFileDescriptor(socket);
        }
        catch (SecurityException | IllegalArgumentException | IllegalAccessException
               | NoSuchMethodException | InvocationTargetException e)
        {
            throw new IllegalStateException("The file descriptor cannot be determined for the given socket", e);
        }
    }

    @Override
    public void setKeepAliveInterval(Duration interval)
    {
        IntByReference newInterval = new IntByReference(Ints.checkedCast(interval.getMillis()));
        callC(sockLib.setsockopt(fd, SOL_SOCKET, SO_KEEPALIVE, newInterval, Pointer.SIZE));
    }

}
