package org.mjd.nativesocket;

/**
 * Used for time duration inputs in the {@link NativeSocket} type.
 *
 */
public interface TimeDuration
{
    /**
     * @return the time duration this instance represents in standard seconds.
     */
    long getStandardSeconds();
}
