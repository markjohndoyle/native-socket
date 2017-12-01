package org.mjd.nativesocket.internal;

import java.net.Socket;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mjd.nativesocket.NativeSocket;
import org.mjd.nativesocket.NativeSocketFactory;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NativeSocketFactoryTest
{

    @Mock
    private Socket mockSocket;


    @Test
    public void testCreateFromOnThisSystem()
    {
        NativeSocket nativeSocket = NativeSocketFactory.createFrom(mockSocket);
    }

}
