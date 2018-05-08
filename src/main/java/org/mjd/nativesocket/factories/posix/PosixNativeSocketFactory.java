package org.mjd.nativesocket.factories.posix;

import java.net.Socket;
import java.nio.channels.SocketChannel;

import org.mjd.nativesocket.NativeSocket;
import org.mjd.nativesocket.factories.NativeSocketFactory;
import org.mjd.nativesocket.internal.fd.linux.LinuxJdkFileDescriptorAccessor;
import org.mjd.nativesocket.internal.sockets.posix.PosixSocket;
import org.mjd.nativesocket.internal.stdlib.StandardLibStaticFactory;

/**
 * A {@link NativeSocketFactory} for POSIX socket compliant platforms.
 *
 * This factory creates POSIX specific {@link NativeSocket} instances.
 *
 */
public class PosixNativeSocketFactory implements NativeSocketFactory
{
    @Override
    public NativeSocket createFrom(Socket socket)
    {
        return new PosixSocket(socket, StandardLibStaticFactory.getStandardLib(), new LinuxJdkFileDescriptorAccessor());
    }

    @Override
    public NativeSocket createFrom(SocketChannel socketChannel)
    {
        return new PosixSocket(socketChannel.socket(), StandardLibStaticFactory.getStandardLib(),
                               new LinuxJdkFileDescriptorAccessor());
    }
}
