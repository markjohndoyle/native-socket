package org.mjd.nativesocket.internal.jna.linux;

import com.sun.jna.Native;
import org.mjd.nativesocket.internal.jna.StandardLib;

/**
 * {@link StandardLib} for the Linux platform.
 *
 */
public final class LinuxStandardLib implements StandardLib
{
    /** name of the standard library in Linux */
    private static final String C_LIB_NAME = "c";

    @Override
    public <T> T loadStandardLibrary(Class<T> accessingInterface)
    {
        return Native.loadLibrary(C_LIB_NAME, accessingInterface);
    }

}
