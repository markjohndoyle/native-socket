package org.mjd.nativesocket.internal.stdlib;

import com.sun.jna.Platform;
import org.mjd.nativesocket.internal.jna.StandardLib;
import org.mjd.nativesocket.internal.jna.linux.LinuxStandardLib;
import org.mjd.nativesocket.internal.jna.win.WinStandardLib;

public final class StandardLibStaticFactory
{
    private StandardLibStaticFactory()
    {
        // Utility class, no constrution.
    }

    public static StandardLib getStandardLib()
    {
        if (Platform.isLinux())
        {
            return new LinuxStandardLib();
        }
        else if(Platform.isWindows())
        {
            return new WinStandardLib();
        }
        throw new IllegalStateException("No " + StandardLib.class.getName() + " implementation for this platform: " +
                        System.getProperty("os.name"));
    }
}
