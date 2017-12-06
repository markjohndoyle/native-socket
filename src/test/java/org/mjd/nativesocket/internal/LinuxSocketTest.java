package org.mjd.nativesocket.internal;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import com.sun.jna.Platform;
import org.joda.time.Duration;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mjd.nativesocket.NativeSocket;
import org.mjd.nativesocket.NativeSocket.KeepAliveData;
import org.mjd.nativesocket.factories.LinuxNativeSocketFactory;
import org.mjd.nativesocket.factories.NativeSocketFactory;
import org.mjd.nativesocket.factories.NativeSocketStaticFactory;

import static org.junit.Assert.assertEquals;


/**
 * We could mock out the real sockets but it's relatively painless using the real constructs and
 * we get an integration test thrown in. This makes the test platform specific of course, but if the
 * library doens't work on this platform the test don't offer anything anyway!
 */
public class LinuxSocketTest
{
    private static int systemInterval;
    private static int systemIdleTime;
    private static int systemProbeCount;
    
    private SocketChannel socketChannel;
    private ServerSocketChannel server;
    private Socket socket;


    /**
     * Set up statics for tests and confirms we are running on Linux. If not tests are ignored.
     * 
     * @throws Exception
     */
    @BeforeClass
    public static void checkOs() throws Exception {
        Assume.assumeTrue(Platform.isLinux());
        systemInterval = Integer.valueOf(LinuxUtils.readProc("sys", "net", "ipv4", "tcp_keepalive_intvl").get(0));
        systemIdleTime = Integer.valueOf(LinuxUtils.readProc("sys", "net", "ipv4", "tcp_keepalive_time").get(0));
        systemProbeCount = Integer.valueOf(LinuxUtils.readProc("sys", "net", "ipv4", "tcp_keepalive_probes").get(0));
    }

    /**
     * Set-up quick server and client.
     * @throws Exception if the test cannot be setup.
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
    public void testIoSocketSetKeepAliveInterval() throws IOException
    {
        NativeSocket linSockUnderTest = new LinuxSocket(socket);
        linSockUnderTest.setKeepAliveInterval(Duration.standardSeconds(1));
        
        KeepAliveData keepAliveData = linSockUnderTest.getKeepAliveData();
        
        assertEquals(1, keepAliveData.getInterval());
    }

    /**
     * Tests we can set the keep alive with a duration on an IO {@link Socket} from a
     * {@link SocketChannel}
     * 
     * @throws IOException
     */
    @Test
    public void testNioSocketSetKeepAliveInterval() throws IOException
    {
        NativeSocket linSockUnderTest = new LinuxSocket(socketChannel.socket());
        linSockUnderTest.setKeepAliveInterval(Duration.standardSeconds(1));
        
        KeepAliveData keepAliveData = linSockUnderTest.getKeepAliveData();
        
        assertEquals(1, keepAliveData.getInterval());
    }
    
    /**
     * Tests we can set the keep alive with a duration on an IO {@link Socket}
     * 
     * @throws IOException
     */
    @Test
    public void testIoSocketSetKeepAliveIdle() throws IOException
    {
        NativeSocket linSockUnderTest = new LinuxSocket(socket);
        linSockUnderTest.setKeepAliveIdle(Duration.standardSeconds(1));
        
        KeepAliveData keepAliveData = linSockUnderTest.getKeepAliveData();
        
        assertEquals(1, keepAliveData.getIdleTime());
    }
    
    
    /**
     * Tests get keep alive data without previously setting it.
     *
     * @throws IOException
     */
    @Test
    public void testIoSocketGetKeepAliveData() throws IOException
    {
        NativeSocket linSockUnderTest = new LinuxSocket(socket);
        
        KeepAliveData actualData = linSockUnderTest.getKeepAliveData();
        
        assertEquals(systemInterval, actualData.getInterval());
        assertEquals(systemIdleTime, actualData.getIdleTime());
        assertEquals(systemProbeCount, actualData.getProbeCount());
    }

}
