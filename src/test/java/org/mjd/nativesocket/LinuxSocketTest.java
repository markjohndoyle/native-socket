package org.mjd.nativesocket;

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


public class LinuxSocketTest
{
    private SocketChannel socketChannel;
    private ServerSocketChannel server;
    private Socket socket;


    @Before
    public void setupPerTest() throws Exception
    {
        final InetSocketAddress serverAddress = new InetSocketAddress(InetAddress.getLocalHost(), 0);
        server = ServerSocketChannel.open().bind(serverAddress);
        final int serverPort = server.socket().getLocalPort();
        socketChannel = SocketChannel.open(new InetSocketAddress(server.socket().getInetAddress(), serverPort));
        socket = new Socket(server.socket().getInetAddress(), serverPort);
    }

    @Test
    public void testIoSocket() throws IOException
    {
        NativeSocket linSockUnderTest = new LinuxSocket(socket);
        linSockUnderTest.setKeepAliveInterval(Duration.standardSeconds(1));
    }

    @Test
    public void testNioSocket() throws IOException
    {
        NativeSocket linSockUnderTest = new LinuxSocket(socketChannel.socket());
        linSockUnderTest.setKeepAliveInterval(Duration.standardSeconds(1));
    }

}
