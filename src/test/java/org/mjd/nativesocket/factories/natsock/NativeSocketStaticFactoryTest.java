package org.mjd.nativesocket.factories.natsock;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mjd.nativesocket.NativeSocket;
import org.mjd.nativesocket.staticfactories.NativeSocketStaticFactory;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class NativeSocketStaticFactoryTest
{
    private SocketChannel socketChannel;
    private ServerSocketChannel server;

    @Mock private Socket mockSocket;

    @Before
    public void setupPerTest() throws Exception
    {
        final InetSocketAddress serverAddress = new InetSocketAddress(InetAddress.getLocalHost(), 0);
        server = ServerSocketChannel.open().bind(serverAddress);
        final int serverPort = server.socket().getLocalPort();
        socketChannel = SocketChannel.open(new InetSocketAddress(server.socket().getInetAddress(), serverPort));
    }

    @Test
    public void testCreateFromSocketOnThisSystem()
    {
        NativeSocket nativeSocket = NativeSocketStaticFactory.createFrom(mockSocket);
    }

    @Test
    public void testCreateFromSocketChannelOnThisSystem()
    {
        NativeSocket nativeSocket = NativeSocketStaticFactory.createFrom(socketChannel);
    }

}
