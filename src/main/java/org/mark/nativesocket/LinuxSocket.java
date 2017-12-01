package org.mark.nativesocket;

import java.io.FileDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.SocketImpl;

import com.google.common.primitives.Ints;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import org.joda.time.Duration;
import org.mark.nativesocket.jna.SocketLibrary;

import static sun.misc.SharedSecrets.getJavaIOFileDescriptorAccess;


/**
 * {@link NativeSocket} implementation for Linux.
 * 
 * Uses JNA and the standard C library to provide functionality.
 */
public final class LinuxSocket implements NativeSocket
{
    private static final int SET_OPT_ERROR = -1;
    private static final int SOL_SOCKET = 1;
    private static final int SO_KEEPALIVE = 9;
    private final int fd;
    private final SocketLibrary sockLib;

    /**
     * @param socket
     */
    LinuxSocket(Socket socket)
    {
        sockLib = Native.loadLibrary("c", SocketLibrary.class);
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

    /**
     * @param interval
     */
    @Override
    public void setKeepAliveInterval(Duration interval)
    {
        IntByReference newInterval = new IntByReference(Ints.checkedCast(interval.getMillis()));
        int result = sockLib.setsockopt(fd, SOL_SOCKET, SO_KEEPALIVE, newInterval, Pointer.SIZE);
        processReturn(result);
    }

    @SuppressWarnings("restriction")
    private static int extractFileDescriptor(Socket socket) throws NoSuchMethodException, IllegalAccessException,
                                                            InvocationTargetException
    {
        Method getImpl = Socket.class.getDeclaredMethod("getImpl");
        Method getFileDescriptor = SocketImpl.class.getDeclaredMethod("getFileDescriptor");
        getImpl.setAccessible(true);
        getFileDescriptor.setAccessible(true);

        SocketImpl impl = (SocketImpl) getImpl.invoke(socket);
        FileDescriptor javaFileDesc = (FileDescriptor) getFileDescriptor.invoke(impl);
        return getJavaIOFileDescriptorAccess().get(javaFileDesc);
    }

    private static void processReturn(int result)
    {
        if (result == SET_OPT_ERROR)
        {
            throw new IllegalStateException("Setting socket option failed; the last OS error = " +
                            Native.getLastError());
        }
    }

}
