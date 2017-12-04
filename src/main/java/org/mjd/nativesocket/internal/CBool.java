package org.mjd.nativesocket.internal;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;


public enum CBool
{
     /** False C boolean */
     FALSE(0),
     /** True C boolean */
     TRUE(1);

    public static final Pointer TRUE_PTR = new IntByReference(TRUE.asCInt()).getPointer();
    public static final Pointer FALSE_PTR = new IntByReference(FALSE.asCInt()).getPointer();

    private final int cIntRep;

    /**
     * In c
     * @param integerRep
     */
    private CBool(final int integerRep)
    {
        cIntRep = integerRep;
    }

    public int asCInt()
    {
        return cIntRep;
    }
    
    public boolean toBoolean()
    {
        return cIntRep != 0;
    }
    
    public static CBool fromInt(int cIntRep)
    {
        return values()[cIntRep];
    }
}
