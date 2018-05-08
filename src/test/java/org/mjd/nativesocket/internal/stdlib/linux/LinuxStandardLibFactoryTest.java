package org.mjd.nativesocket.internal.stdlib.linux;

import org.junit.Test;
import org.mjd.nativesocket.internal.jna.linux.LinuxStandardLib;
import org.mjd.nativesocket.internal.stdlib.StandardLibFactory;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class LinuxStandardLibFactoryTest
{

    @Test
    public void test()
    {
        StandardLibFactory factoryUnderTest = new LinuxStandardLibFactory();
        assertThat(factoryUnderTest.createStandardLib(), is(instanceOf(LinuxStandardLib.class)));
    }

}
