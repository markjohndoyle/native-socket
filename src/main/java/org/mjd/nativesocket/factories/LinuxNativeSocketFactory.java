package org.mjd.nativesocket.factories;

import java.net.Socket;
import java.nio.channels.SocketChannel;

import org.mjd.nativesocket.NativeSocket;
import org.mjd.nativesocket.internal.LinuxSocket;

/**
 * A {@link NativeSocketFactory} for Linux platforms.
 * 
 * This factory creates Linux specific {@link NativeSocket} instances.
 *
 */
public class LinuxNativeSocketFactory implements NativeSocketFactory
{
    @Override
    public NativeSocket createFrom(Socket socket)
    {
        return new LinuxSocket(socket);
    }

    @Override
    public NativeSocket createFrom(SocketChannel socketChannel)
    {
        return new LinuxSocket(socketChannel.socket());
    }
}
