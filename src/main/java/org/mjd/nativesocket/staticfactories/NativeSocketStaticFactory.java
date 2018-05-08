package org.mjd.nativesocket.staticfactories;

import java.net.Socket;
import java.nio.channels.SocketChannel;

import org.mjd.nativesocket.NativeSocket;
import org.mjd.nativesocket.factories.NativeSocketFactory;
import org.mjd.nativesocket.internal.fd.linux.LinuxJdkFileDescriptorAccessor;
import org.mjd.nativesocket.internal.sockets.posix.PosixSocket;
import org.mjd.nativesocket.internal.stdlib.StandardLibStaticFactory;

import static org.mjd.nativesocket.internal.osutil.posix.PosixUtils.isPlatformPosixCompliant;

/**
 * Factory for creating {@link NativeSocket} instances. The Factory takes care of creating the correct
 * type of {@link NativeSocket} for your platform.
 * Note: The {@link NativeSocketFactory} is a more flexible method but requires you inject the correct
 * factory into your application. This is better but you may have reasons for not following that strategy
 * (which would be wrong).
 */
public final class NativeSocketStaticFactory
{
    private NativeSocketStaticFactory()
    {
        // Factory class.
    }

    /**
     * Creates a {@link NativeSocket} from a given {@link Socket}
     *
     * @param socket
     *            the Java {@link Socket} to create a Native socket from
     * @return new {@link NativeSocket} for this Platform
     * @see NativeSocket
     * @see Socket
     */
    public static NativeSocket createFrom(Socket socket)
    {
        if (isPlatformPosixCompliant())
        {
            return new PosixSocket(socket, StandardLibStaticFactory.getStandardLib(),
                                   new LinuxJdkFileDescriptorAccessor());
        }
        throw new IllegalStateException("No Native socket implementation for this platform: " +
                        System.getProperty("os.name"));
    }

    /**
     * Creates a {@link NativeSocket} from a given {@link SocketChannel}
     *
     * @param socketChannel
     *            the Java {@link SocketChannel} to create a Native socket from
     * @return new {@link NativeSocket} for this Platform
     * @see NativeSocket
     * @see Socket
     */
    public static NativeSocket createFrom(SocketChannel socketChannel)
    {
        return createFrom(socketChannel.socket());
    }
}
