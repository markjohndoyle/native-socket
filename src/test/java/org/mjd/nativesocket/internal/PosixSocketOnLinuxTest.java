package org.mjd.nativesocket.internal;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mjd.nativesocket.NativeSocket;
import org.mjd.nativesocket.NativeSocket.KeepAliveData;
import org.mjd.nativesocket.internal.fd.FileDescriptorAccessor;
import org.mjd.nativesocket.internal.fd.FileDescriptorAccessor.FileDescriptorException;
import org.mjd.nativesocket.internal.fd.linux.LinuxJdkFileDescriptorAccessor;
import org.mjd.nativesocket.internal.jna.StandardLib;
import org.mjd.nativesocket.internal.sockets.posix.PosixSocket;
import org.mjd.nativesocket.internal.sockets.posix.PosixSocketLibrary;
import org.mjd.nativesocket.internal.stdlib.StandardLibStaticFactory;
import org.mjd.nativesocket.testsupport.LinuxUtils;
import org.mjd.nativesocket.time.joda.JodaTimeDuration;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;


/**
 * We could mock out the real sockets but it's relatively painless using the real constructs and
 * we get an integration test thrown in. This makes the test platform specific of course, but if the
 * library doens't work on this platform the test don't offer anything anyway!
 */
@RunWith(MockitoJUnitRunner.class)
public class PosixSocketOnLinuxTest
{
    private static int systemInterval;
    private static int systemIdleTime;
    private static int systemProbeCount;

    private SocketChannel socketChannel;
    private ServerSocketChannel server;
    private Socket socket;

    @Mock private FileDescriptorAccessor mockErrorThrowingAccessor;
    @Mock private StandardLib mockStdLib;
    @Mock private PosixSocketLibrary mockPosixSocketLib;


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

        when(mockErrorThrowingAccessor.extractFileDescriptor(any(Socket.class))).
                                                                    thenThrow(FileDescriptorException.class);

        when(mockStdLib.loadStandardLibrary(PosixSocketLibrary.class)).thenReturn(mockPosixSocketLib);
    }

    /**
     * Tests we can set the keep alive with an idle time on an IO {@link Socket}
     *
     * @throws IOException
     */
    @Test
    public void testIoSocketSetKeepAliveIdleTime() throws IOException
    {
        NativeSocket linSockUnderTest = new PosixSocket(socket, StandardLibStaticFactory.getStandardLib(),
                new LinuxJdkFileDescriptorAccessor());

        linSockUnderTest.setKeepAliveIdleTime(JodaTimeDuration.standardSeconds(2));

        KeepAliveData keepAliveData = linSockUnderTest.getKeepAliveData();

        assertEquals(2, keepAliveData.getIdleTime());
    }

    /**
     * Tests we can set the keep alive with an idle time on an NIO {@link SocketChannel}
     *
     * @throws IOException
     */
    @Test
    public void testNioSocketChannelSetKeepAliveIdleTime() throws IOException
    {
        NativeSocket linSockUnderTest = new PosixSocket(socketChannel, StandardLibStaticFactory.getStandardLib(),
                new LinuxJdkFileDescriptorAccessor());

        linSockUnderTest.setKeepAliveIdleTime(JodaTimeDuration.standardSeconds(2));

        KeepAliveData keepAliveData = linSockUnderTest.getKeepAliveData();

        assertEquals(2, keepAliveData.getIdleTime());
    }

    /**
     * Tests we can set the keep alive with an interval on an IO {@link Socket}
     *
     * @throws IOException
     */
    @Test
    public void testIoSocketSetKeepAliveInterval() throws IOException
    {
        NativeSocket linSockUnderTest = new PosixSocket(socket, StandardLibStaticFactory.getStandardLib(),
                new LinuxJdkFileDescriptorAccessor());

        linSockUnderTest.setKeepAliveInterval(JodaTimeDuration.standardSeconds(2));

        KeepAliveData keepAliveData = linSockUnderTest.getKeepAliveData();

        assertEquals(2, keepAliveData.getInterval());
    }

    /**
     * Tests we can set the keep alive with an interval on an NIO {@link SocketChannel}
     *
     * @throws IOException
     */
    @Test
    public void testNioSocketChannelSetKeepAliveInterval() throws IOException
    {
        NativeSocket linSockUnderTest = new PosixSocket(socketChannel, StandardLibStaticFactory.getStandardLib(),
                new LinuxJdkFileDescriptorAccessor());

        linSockUnderTest.setKeepAliveInterval(JodaTimeDuration.standardSeconds(3));

        KeepAliveData keepAliveData = linSockUnderTest.getKeepAliveData();

        assertEquals(3, keepAliveData.getInterval());
    }

    /**
     * Tests we can set the keep alive with probes counter on an IO {@link Socket}
     *
     * @throws IOException
     */
    @Test
    public void testIoSocketSetKeepAliveProbes() throws IOException
    {
        NativeSocket linSockUnderTest = new PosixSocket(socket, StandardLibStaticFactory.getStandardLib(),
                new LinuxJdkFileDescriptorAccessor());

        linSockUnderTest.setKeepAliveProbes(1);

        KeepAliveData keepAliveData = linSockUnderTest.getKeepAliveData();

        assertEquals(1, keepAliveData.getProbeCount());
    }

    /**
     * Tests we can set the keep alive with probes on an NIO {@link SocketChannel}
     *
     * @throws IOException
     */
    @Test
    public void testNioSocketChannelSetKeepAliveProbes() throws IOException
    {
        NativeSocket linSockUnderTest = new PosixSocket(socketChannel, StandardLibStaticFactory.getStandardLib(),
                new LinuxJdkFileDescriptorAccessor());

        linSockUnderTest.setKeepAliveProbes(5);

        KeepAliveData keepAliveData = linSockUnderTest.getKeepAliveData();

        assertEquals(5, keepAliveData.getProbeCount());
    }

    /**
     * Tests we can set the keep alive with an idle time, interval and probes count on an IO {@link Socket}
     *
     * @throws IOException
     */
    @Test
    public void testIoSocketSetKeepAlive() throws IOException
    {
        NativeSocket linSockUnderTest = new PosixSocket(socket, StandardLibStaticFactory.getStandardLib(),
                                                        new LinuxJdkFileDescriptorAccessor());

        JodaTimeDuration idleTime = JodaTimeDuration.standardSeconds(2);
        JodaTimeDuration interval = JodaTimeDuration.standardSeconds(1);

        linSockUnderTest.setKeepAlive(idleTime, interval, 3);

        KeepAliveData keepAliveData = linSockUnderTest.getKeepAliveData();

        assertEquals(2, keepAliveData.getIdleTime());
        assertEquals(1, keepAliveData.getInterval());
        assertEquals(3, keepAliveData.getProbeCount());
    }

    /**
     * Tests we can set the keep alive with an interval, idle time and probes count on an IO {@link Socket} from a
     * {@link SocketChannel}
     *
     * @throws IOException
     */
    @Test
    public void testNioSocketChannelSetKeepAlive() throws IOException
    {
        NativeSocket linSockUnderTest = new PosixSocket(socketChannel,
                                                        StandardLibStaticFactory.getStandardLib(),
                                                        new LinuxJdkFileDescriptorAccessor());

        JodaTimeDuration idleTime = JodaTimeDuration.standardSeconds(6);
        JodaTimeDuration interval = JodaTimeDuration.standardSeconds(3);

        linSockUnderTest.setKeepAlive(idleTime, interval, 2);

        KeepAliveData keepAliveData = linSockUnderTest.getKeepAliveData();

        assertEquals(6, keepAliveData.getIdleTime());
        assertEquals(3, keepAliveData.getInterval());
        assertEquals(2, keepAliveData.getProbeCount());
    }


    /**
     * Tests get keep alive data for {@link Socket} without previously setting it.
     *
     * @throws IOException
     */
    @Test
    public void testIoSocketGetKeepAliveData() throws IOException
    {
        NativeSocket linSockUnderTest = new PosixSocket(socket, StandardLibStaticFactory.getStandardLib(),
                                                        new LinuxJdkFileDescriptorAccessor());

        KeepAliveData actualData = linSockUnderTest.getKeepAliveData();

        assertEquals(systemInterval, actualData.getInterval());
        assertEquals(systemIdleTime, actualData.getIdleTime());
        assertEquals(systemProbeCount, actualData.getProbeCount());
        assertThat(actualData.isEnabled(), is(false));
    }

    /**
     * Tests get keep alive data for {@link SocketChannel} without previously setting it.
     *
     * @throws IOException
     */
    @Test
    public void testNioSocketChannelGetKeepAliveData() throws IOException
    {
        NativeSocket linSockUnderTest = new PosixSocket(socketChannel, StandardLibStaticFactory.getStandardLib(),
                new LinuxJdkFileDescriptorAccessor());

        KeepAliveData actualData = linSockUnderTest.getKeepAliveData();

        assertEquals(systemInterval, actualData.getInterval());
        assertEquals(systemIdleTime, actualData.getIdleTime());
        assertEquals(systemProbeCount, actualData.getProbeCount());
        assertThat(actualData.isEnabled(), is(false));
    }

    @Test
    public void testIoSocketIsEnabledReturnsCorrectValue() throws IOException
    {
        NativeSocket linSockUnderTest = new PosixSocket(socket, StandardLibStaticFactory.getStandardLib(),
                                                        new LinuxJdkFileDescriptorAccessor());

        KeepAliveData actualData = linSockUnderTest.getKeepAliveData();

        assertThat(actualData.isEnabled(), is(false));

        linSockUnderTest.setKeepAlive(JodaTimeDuration.standardSeconds(6), JodaTimeDuration.standardSeconds(3), 1);
        actualData = linSockUnderTest.getKeepAliveData();

        assertThat(actualData.isEnabled(), is(true));
    }

    @Test(expected =  IllegalStateException.class)
    public void testIoSocketDoesntConstructWhenMandatoryDataAquisitionFails()
    {
        new PosixSocket(socket, StandardLibStaticFactory.getStandardLib(), mockErrorThrowingAccessor);
    }

    @Test(expected =  IllegalStateException.class)
    public void testIoSocketThrowsExceptionOnFailedSocketLibCall()
    {
        NativeSocket linSockUnderTest = new PosixSocket(socket, mockStdLib, new LinuxJdkFileDescriptorAccessor());
        when(mockPosixSocketLib.setsockopt(anyInt(), anyInt(), anyInt(), any(Pointer.class), anyInt())).thenReturn(-1);

        linSockUnderTest.setKeepAlive(JodaTimeDuration.standardSeconds(6), JodaTimeDuration.standardSeconds(3), 1);
    }


}
