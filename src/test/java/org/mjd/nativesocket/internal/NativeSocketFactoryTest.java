package org.mjd.nativesocket.internal;

import java.net.Socket;
import java.nio.channels.SocketChannel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mjd.nativesocket.NativeSocket;
import org.mjd.nativesocket.NativeSocketFactory;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class NativeSocketFactoryTest
{
    @Mock private Socket mockSocket;
    @Mock private SocketChannel mockSocketChannel;

    @Test
    public void testCreateFromSocketOnThisSystem()
    {
        NativeSocket nativeSocket = NativeSocketFactory.createFrom(mockSocket);
    }

    @Test
    public void testCreateFromSocketChannelOnThisSystem()
    {
        when(mockSocketChannel.socket()).thenReturn(mockSocket);
        NativeSocket nativeSocket = NativeSocketFactory.createFrom(mockSocketChannel);
    }

}
