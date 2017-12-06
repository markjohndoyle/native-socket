package org.mjd.nativesocket.internal;

import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import com.google.common.primitives.Ints;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import org.joda.time.Duration;
import org.mjd.nativesocket.NativeSocket;
import org.mjd.nativesocket.factories.NativeSocketStaticFactory;

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
    private static final int SOL_TCP = 6;
    private static final int TCP_KEEPIDLE = 4;
    private static final int TCP_KEEPINTVL = 5;
    private static final int TCP_KEEPCNT = 6;
    private static final int SO_KEEPALIVE = 9;
    private final int fd;
    private final SocketLibrary sockLib = loadStandardLibrary(SocketLibrary.class);


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
     * 
     * @see NativeSocket
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
        IntByReference newInterval = new IntByReference(Ints.checkedCast(interval.getStandardSeconds()));
        enableKeepAlive();
        callC(sockLib.setsockopt(fd, SOL_TCP, TCP_KEEPINTVL, newInterval.getPointer(), Ints.BYTES));
    }

    
    @Override
    public void setKeepAliveIdle(Duration idletime)
    {
        IntByReference newTime = new IntByReference(Ints.checkedCast(idletime.getStandardSeconds()));
        enableKeepAlive();
        callC(sockLib.setsockopt(fd, SOL_TCP, TCP_KEEPIDLE, newTime.getPointer(), Ints.BYTES));
    }
    
    @Override
    public KeepAliveData getKeepAliveData()
    {
        IntByReference enabled = new IntByReference();
        IntByReference interval = new IntByReference();
        IntByReference idle = new IntByReference();
        IntByReference probes = new IntByReference();
        Pointer optionLen = new IntByReference(Ints.BYTES).getPointer();
        
        callC(sockLib.getsockopt(fd, SOL_SOCKET, SO_KEEPALIVE, enabled.getPointer(), optionLen));
        callC(sockLib.getsockopt(fd, SOL_TCP, TCP_KEEPINTVL, interval.getPointer(), optionLen));
        callC(sockLib.getsockopt(fd, SOL_TCP, TCP_KEEPIDLE, idle.getPointer(), optionLen));
        callC(sockLib.getsockopt(fd, SOL_TCP, TCP_KEEPCNT, probes.getPointer(), optionLen));
        
        return new KeepAliveData(CBool.fromInt(enabled.getValue()), probes.getValue(), interval.getValue(),
                                 idle.getValue());
    }

    
    private void enableKeepAlive()
    {
        callC(sockLib.setsockopt(fd, SOL_SOCKET, SO_KEEPALIVE, CBool.TRUE_PTR, Ints.BYTES));
    }
}
