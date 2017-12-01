package org.mjd.nativesocket;

import java.net.Socket;
import java.nio.channels.SocketChannel;

import com.sun.jna.Platform;
import org.mjd.nativesocket.internal.LinuxSocket;


/**
 * Factory for creating {@link NativeSocket} instances. The Factory takes care of creating the correct
 * type of {@link NativeSocket} for your platform.
 *
 */
public final class NativeSocketFactory
{
    private NativeSocketFactory()
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
        if (Platform.isLinux())
        {
            return new LinuxSocket(socket);
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
