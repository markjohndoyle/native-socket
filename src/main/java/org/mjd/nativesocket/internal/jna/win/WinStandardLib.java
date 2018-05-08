package org.mjd.nativesocket.internal.jna.win;

import com.sun.jna.Native;
import org.mjd.nativesocket.internal.jna.StandardLib;

public final class WinStandardLib implements StandardLib
{
    private static final String C_LIB_NAME = "msvcrt";

    @Override
    public <T> T loadStandardLibrary(Class<T> accessingInterface)
    {
        return Native.loadLibrary(C_LIB_NAME, accessingInterface);
    }

}
