package org.mark.nativesocket;

import org.joda.time.Duration;

public interface NativeSocket
{

    void setKeepAliveInterval(Duration interval);

}
