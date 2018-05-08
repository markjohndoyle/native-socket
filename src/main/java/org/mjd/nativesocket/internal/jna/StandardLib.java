package org.mjd.nativesocket.internal.jna;

/**
 * Type for loading the Standard library.
 */
public interface StandardLib
{

    /**
     * Loads the native standard library exposed through the passed interface.
     *
     * @param accessingInterface
     *            the interface to expose the standard library under.
     * @return Loaded standard library as type T
     */
    <T> T loadStandardLibrary(Class<T> accessingInterface);
}
