package org.mjd.nativesocket.internal.stdlib.linux;

import org.mjd.nativesocket.internal.jna.StandardLib;
import org.mjd.nativesocket.internal.jna.linux.LinuxStandardLib;
import org.mjd.nativesocket.internal.stdlib.StandardLibFactory;

public final class LinuxStandardLibFactory implements StandardLibFactory
{

    @Override
    public StandardLib createStandardLib()
    {
        return new LinuxStandardLib();
    }

}
