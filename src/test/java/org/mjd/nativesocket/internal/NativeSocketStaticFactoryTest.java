package org.mjd.nativesocket.internal;

import java.net.Socket;
import java.nio.channels.SocketChannel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mjd.nativesocket.NativeSocket;
import org.mjd.nativesocket.factories.NativeSocketStaticFactory;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class NativeSocketStaticFactoryTest
{
    @Mock private Socket mockSocket;
    @Mock private SocketChannel mockSocketChannel;

    @Test
    public void testCreateFromSocketOnThisSystem()
    {
        NativeSocket nativeSocket = NativeSocketStaticFactory.createFrom(mockSocket);
    }

    @Test
    public void testCreateFromSocketChannelOnThisSystem()
    {
        when(mockSocketChannel.socket()).thenReturn(mockSocket);
        NativeSocket nativeSocket = NativeSocketStaticFactory.createFrom(mockSocketChannel);
    }

}
