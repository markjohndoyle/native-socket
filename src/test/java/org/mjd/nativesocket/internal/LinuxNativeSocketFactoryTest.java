package org.mjd.nativesocket.internal;

import java.net.Socket;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mjd.nativesocket.NativeSocket;
import org.mjd.nativesocket.factories.LinuxNativeSocketFactory;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Unit tests for the {@link LinuxNativeSocketFactory}
 */
@RunWith(MockitoJUnitRunner.class)
public class LinuxNativeSocketFactoryTest
{
    @Mock private Socket mockSocket;
    
    private LinuxNativeSocketFactory factoryUnderTest = new LinuxNativeSocketFactory();
    
    /**
     * Test that the {@link LinuxNativeSocketFactory} creates the correct type of {@link NativeSocket}
     * namely a {@link LinuxSocket}
     */
    @Test
    public void testFactoryCreatesTheCorrectType()
    {
        NativeSocket nativeSocket = factoryUnderTest.createFrom(mockSocket);
        assertThat(nativeSocket, is(instanceOf(LinuxSocket.class)));
    }
}