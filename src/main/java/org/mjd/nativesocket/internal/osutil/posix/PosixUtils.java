package org.mjd.nativesocket.internal.osutil.posix;

import static com.sun.jna.Platform.isAIX;
import static com.sun.jna.Platform.isLinux;
import static com.sun.jna.Platform.isWindows;

/**
 * POSIX related utility functions.
 */
public final class PosixUtils
{
    /** Utility class, no construction. */
    private PosixUtils() { }

    /**
     * Discover whether your platform is POSIX compliant
     *
     * @return true if the platform is POSIX compliant, else false.
     */
    public static boolean isPlatformPosixCompliant()
    {
        return isWindows() || isLinux() || isAIX();
    }
}
