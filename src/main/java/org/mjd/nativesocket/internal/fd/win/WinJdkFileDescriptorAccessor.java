package org.mjd.nativesocket.internal.fd.win;

import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.nio.channels.SocketChannel;

import org.mjd.nativesocket.internal.fd.FileDescriptorAccessor;

import static org.mjd.nativesocket.internal.fd.jdk.JdkFileDescriptorAccessor.getJavaFileDescriptor;
import static sun.misc.SharedSecrets.getJavaIOFileDescriptorAccess;

/**
 * JDK specific {@link FileDescriptorAccessor} that works for all JDK versions < 9 on Windows
 *
 * NOTE: This more than likely works for 9 too, it's just not been checked.
 */
public final class WinJdkFileDescriptorAccessor implements FileDescriptorAccessor
{
    // SharedSecrets required to get the file descriptor.
    @SuppressWarnings("restriction")
    @Override
    public long extractFileDescriptor(Socket socket) throws FileDescriptorException
    {
        try
        {
            return getJavaIOFileDescriptorAccess().getHandle(getJavaFileDescriptor(socket));
        }
        catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
               | InvocationTargetException e)
        {
            throw new FileDescriptorException(e);
        }
    }

    @SuppressWarnings("restriction")
    @Override
    public long extractFileDescriptor(SocketChannel socketChannel) throws FileDescriptorException {
        try
        {
            return getJavaIOFileDescriptorAccess().getHandle(getJavaFileDescriptor(socketChannel));
        }
        catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalAccessException
                | IllegalArgumentException e)
        {
            throw new FileDescriptorException(e);
        }
    }

}
