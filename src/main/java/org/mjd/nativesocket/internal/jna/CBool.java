package org.mjd.nativesocket.internal.jna;

import com.google.common.primitives.Ints;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;


/**
 * Java mapping for C boolean
 *
 */
public enum CBool
{
    /** False C boolean */
    FALSE(0),
    /** True C boolean */
    TRUE(1);

    /** C true as int pointer */
    public static final Pointer TRUE_PTR = new IntByReference(TRUE.asCInt()).getPointer();
    /** C false as int pointer */
    public static final Pointer FALSE_PTR = new IntByReference(FALSE.asCInt()).getPointer();

    public static final int SIZE = Ints.BYTES;

    private final int cIntRep;

    /**
     * In c
     * @param integerRep
     */
    private CBool(final int integerRep)
    {
        cIntRep = integerRep;
    }

    /**
     * @return this boolean as a C integer
     */
    public int asCInt()
    {
        return cIntRep;
    }

    /**
     * @return this booleans as a java boolean.
     */
    public boolean toBoolean()
    {
        return cIntRep != 0;
    }

    /**
     * Create a {@link CBool} from an int.
     *
     * @param cIntRep
     *            the c integer representation of the boolean.
     * @return new CBool representing cIntRep
     */
    public static CBool fromInt(int cIntRep)
    {
        return values()[cIntRep];
    }
}
