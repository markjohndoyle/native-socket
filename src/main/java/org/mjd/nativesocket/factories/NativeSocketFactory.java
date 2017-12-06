package org.mjd.nativesocket.factories;

import java.net.Socket;
import java.nio.channels.SocketChannel;

import org.mjd.nativesocket.NativeSocket;


/**
 * Abstract Factory for creating {@link NativeSocket} instances.
 */
public interface NativeSocketFactory
{
    /**
     * Creates a {@link NativeSocket} from a given {@link Socket}
     * 
     * @param socket
     *            the Java {@link Socket} to create a Native socket from
     * @return new {@link NativeSocket}
     * @see NativeSocket
     * @see Socket
     */
    NativeSocket createFrom(Socket socket);

    /**
     * Creates a {@link NativeSocket} from a given {@link SocketChannel}
     * 
     * @param socketChannel
     *            the Java {@link SocketChannel} to create a Native socket from
     * @return new {@link NativeSocket}
     * @see NativeSocket
     * @see Socket
     */
    NativeSocket createFrom(SocketChannel socketChannel);
}
