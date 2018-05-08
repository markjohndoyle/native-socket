package org.mjd.nativesocket.time.joda;

import java.util.concurrent.TimeUnit;

import org.joda.time.Duration;
import org.mjd.nativesocket.TimeDuration;

public final class JodaTimeDuration implements TimeDuration
{
    private final Duration jodaDuration;

    private JodaTimeDuration(long milliseconds)
    {
        jodaDuration = Duration.millis(milliseconds);
    }

    public static final JodaTimeDuration millis(long milliseconds)
    {
        return new JodaTimeDuration(milliseconds);
    }

    public static final JodaTimeDuration standardSeconds(int milliseconds)
    {
        return new JodaTimeDuration(TimeUnit.SECONDS.toMillis(milliseconds));
    }

    @Override
    public long getStandardSeconds()
    {
        return jodaDuration.getStandardSeconds();
    }
}
