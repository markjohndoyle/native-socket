package org.mjd.nativesocket.factories.natsock;

import java.net.Socket;
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
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link PosixNativeSocketFactory}
 */
@RunWith(MockitoJUnitRunner.class)
public final class PosixNativeSocketFactoryTest
{
    @Mock private Socket mockSocket;
    @Mock private SocketChannel mockSocketChannel;

    private PosixNativeSocketFactory factoryUnderTest = new PosixNativeSocketFactory();

    @Before
    public void setupPerTest()
    {
        when(mockSocketChannel.socket()).thenReturn(mockSocket);
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
        NativeSocket nativeSocket = factoryUnderTest.createFrom(mockSocketChannel);
        assertThat(nativeSocket, is(instanceOf(PosixSocket.class)));
    }
}