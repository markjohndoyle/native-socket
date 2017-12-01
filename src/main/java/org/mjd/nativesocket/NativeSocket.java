package org.mjd.nativesocket;

import org.joda.time.Duration;


/**
 * {@link NativeSocket} instances provide functionality not exposed by the JDK due to platform/JVM/and
 * JDK API reasons.
 * 
 * The Factory will provide a safe implementation of {@link NativeSocket} if one exists. Use of the
 * code is non-portable.
 */
public interface NativeSocket
{
    /**
     * Enables the keep alive mechanism on this {@link NativeSocket} with the given time interval.
     * 
     * @param interval
     *            keep alive interval
     */
    void setKeepAliveInterval(Duration interval);

}
