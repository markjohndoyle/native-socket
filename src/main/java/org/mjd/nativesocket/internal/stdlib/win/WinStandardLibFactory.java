package org.mjd.nativesocket.internal.stdlib.win;

import org.mjd.nativesocket.internal.jna.StandardLib;
import org.mjd.nativesocket.internal.jna.win.WinStandardLib;
import org.mjd.nativesocket.internal.stdlib.StandardLibFactory;

public final class WinStandardLibFactory implements StandardLibFactory
{

    @Override
    public StandardLib createStandardLib()
    {
        return new WinStandardLib();
    }

}
