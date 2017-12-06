package org.mjd.nativesocket.internal;

import java.io.FileDescriptor;
import java.net.Socket;
import java.net.SocketImpl;

/**
 * JDK specific platform utilities.
 * This code could possible by auto-generated or use configuration in the future.
 */
public final class JdkUtils
{
    /** Utility class */
    private JdkUtils() {}
    
    /** 
     * Data class for {@link Socket} class specifics 
     */
    public static final class SocketData
    {
        /** method used to get the socket implementation */
        public static final String GET_IMPL_METHOD = "getImpl";
    
        /** Data class */
        private SocketData() { }
    }
    
    /**
     * Data class for {@link SocketImpl} class specifics
     */
    public static final class SocketImplData
    {
        /** method used to get the {@link FileDescriptor} */
        public static final String GET_FILE_DESC_METHOD = "getFileDescriptor";
        
        /** Data class */
        private SocketImplData() { }
    }
}
