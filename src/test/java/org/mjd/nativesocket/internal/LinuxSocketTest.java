package org.mjd.nativesocket.internal;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.joda.time.Duration;
import org.junit.Before;
import org.junit.Test;
import org.mjd.nativesocket.NativeSocket;
import org.mjd.nativesocket.internal.LinuxSocket;


/**
 * We could mock out the real sockets but it's relatively painless using the real constructs and
 * we get an integration test thrown in. This makes the test platform specific of course, but if the
 * library doens't work on this platform the test don't offer anything anyway!
 */
public class LinuxSocketTest
{
    private SocketChannel socketChannel;
    private ServerSocketChannel server;
    private Socket socket;


    /**
     * 
     * @throws Exception
     */
    @Before
    public void setupPerTest() throws Exception
    {
        final InetSocketAddress serverAddress = new InetSocketAddress(InetAddress.getLocalHost(), 0);
        server = ServerSocketChannel.open().bind(serverAddress);
        final int serverPort = server.socket().getLocalPort();
        socketChannel = SocketChannel.open(new InetSocketAddress(server.socket().getInetAddress(), serverPort));
        socket = new Socket(server.socket().getInetAddress(), serverPort);
    }

    /**
     * Tests we can set the keep alive with a duration on an IO {@link Socket}
     * 
     * @throws IOException
     */
    @Test
    public void testIoSocket() throws IOException
    {
        NativeSocket linSockUnderTest = new LinuxSocket(socket);
        linSockUnderTest.setKeepAliveInterval(Duration.standardSeconds(1));
    }

    /**
     * Tests we can set the keep alive with a duration on an IO {@link Socket} from a
     * {@link SocketChannel}
     * 
     * @throws IOException
     */
    @Test
    public void testNioSocket() throws IOException
    {
        NativeSocket linSockUnderTest = new LinuxSocket(socketChannel.socket());
        linSockUnderTest.setKeepAliveInterval(Duration.standardSeconds(1));
    }

}
