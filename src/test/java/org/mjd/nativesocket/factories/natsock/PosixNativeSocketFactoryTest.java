package org.mjd.nativesocket.factories.natsock;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mjd.nativesocket.NativeSocket;
import org.mjd.nativesocket.factories.posix.PosixNativeSocketFactory;
import org.mjd.nativesocket.internal.sockets.posix.PosixSocket;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Unit tests for the {@link PosixNativeSocketFactory}
 */
@RunWith(MockitoJUnitRunner.class)
public final class PosixNativeSocketFactoryTest
{
    private SocketChannel socketChannel;
    private ServerSocketChannel server;

    private PosixNativeSocketFactory factoryUnderTest = new PosixNativeSocketFactory();

    @Mock private Socket mockSocket;

    @Before
    public void setupPerTest() throws Exception
    {
        final InetSocketAddress serverAddress = new InetSocketAddress(InetAddress.getLocalHost(), 0);
        server = ServerSocketChannel.open().bind(serverAddress);
        final int serverPort = server.socket().getLocalPort();
        socketChannel = SocketChannel.open(new InetSocketAddress(server.socket().getInetAddress(), serverPort));
    }

    /**
     * Test that the {@link PosixNativeSocketFactory} creates the correct type of {@link NativeSocket}
     * namely a {@link PosixSocket}
     */
    @Test
    public void testFactoryCreatesTheCorrectTypeFromSocket()
    {
        NativeSocket nativeSocket = factoryUnderTest.createFrom(mockSocket);
        assertThat(nativeSocket, is(instanceOf(PosixSocket.class)));
    }

    @Test
    public void testFactoryCreatesTheCorrectTypeFromSocketChannel()
    {
        NativeSocket nativeSocket = factoryUnderTest.createFrom(socketChannel);
        assertThat(nativeSocket, is(instanceOf(PosixSocket.class)));
    }
}