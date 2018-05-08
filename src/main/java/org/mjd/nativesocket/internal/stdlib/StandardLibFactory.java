package org.mjd.nativesocket.internal.stdlib;

import org.mjd.nativesocket.internal.jna.StandardLib;

/**
 * {@link StandardLib} factory for producing relevant {@link StandardLib} instances.
 *
 */
public interface StandardLibFactory
{
    /**
     * Creates a new {@link StandardLib} instance.
     *
     * @return new {@link StandardLib}
     */
    StandardLib createStandardLib();
}
