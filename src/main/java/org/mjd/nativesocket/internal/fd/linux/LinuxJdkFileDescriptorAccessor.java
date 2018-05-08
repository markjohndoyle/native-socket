package org.mjd.nativesocket.internal.fd.linux;

import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import org.mjd.nativesocket.internal.fd.FileDescriptorAccessor;

import static org.mjd.nativesocket.internal.fd.jdk.JdkFileDescriptorAccessor.getJavaFileDescriptor;
import static sun.misc.SharedSecrets.getJavaIOFileDescriptorAccess;

/**
 * JDK specific {@link FileDescriptorAccessor} that works for all JDK versions < 9 on Linux
 *
 * NOTE: This more than likely works for 9 too, it's just not been checked.
 */
public final class LinuxJdkFileDescriptorAccessor implements FileDescriptorAccessor
{
    // SharedSecrets required to get the file descriptor.
    @SuppressWarnings("restriction")
    @Override
    public long extractFileDescriptor(Socket socket) throws FileDescriptorException
    {
        try
        {
            return getJavaIOFileDescriptorAccess().get(getJavaFileDescriptor(socket));
        }
        catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
               | InvocationTargetException e)
        {
            throw new FileDescriptorException(e);
        }
    }

}
