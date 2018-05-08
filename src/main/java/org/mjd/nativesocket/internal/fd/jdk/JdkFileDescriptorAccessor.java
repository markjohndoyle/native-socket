package org.mjd.nativesocket.internal.fd.jdk;

import java.io.FileDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.SocketImpl;

/**
 * JDK Specific class for extracting {@link FileDescriptor} instances from various abstractions such as {@link Socket}
 */
public final class JdkFileDescriptorAccessor
{
    /** Utility class; no construction */
    private JdkFileDescriptorAccessor() {}

    /**
     * Data class for {@link Socket} class specifics
     */
    protected static final class SocketData
    {
        /** method used to get the socket implementation */
        public static final String GET_IMPL_METHOD = "getImpl";

        /** Data class */
        private SocketData() { }
    }

    /**
     * Data class for {@link SocketImpl} class specifics
     */
    protected static final class SocketImplData
    {
        /** method used to get the {@link FileDescriptor} */
        public static final String GET_FILE_DESC_METHOD = "getFileDescriptor";

        /** Data class */
        private SocketImplData() { }
    }


    /**
     * Discovers and returns the Java {@link FileDescriptor} from a {@link Socket}
     *
     * @param socket
     *            the {@link Socket} to get the {@link FileDescriptor} for.
     * @return the {@link FileDescriptor} of the {@link Socket} socket
     * @throws NoSuchMethodException
     *             if the {@link FileDescriptor} cannot be retrieved
     * @throws IllegalAccessException
     *             if the {@link FileDescriptor} cannot be retrieved
     * @throws InvocationTargetException
     *             if the {@link FileDescriptor} cannot be retrieved
     */
    public static FileDescriptor getJavaFileDescriptor(Socket socket) throws NoSuchMethodException,
                                                                         IllegalAccessException,
                                                                         InvocationTargetException
    {
        Method getImpl = Socket.class.getDeclaredMethod(SocketData.GET_IMPL_METHOD);
        Method getFileDescriptor = SocketImpl.class.getDeclaredMethod(SocketImplData.GET_FILE_DESC_METHOD);
        getImpl.setAccessible(true);
        getFileDescriptor.setAccessible(true);

        SocketImpl impl = (SocketImpl) getImpl.invoke(socket);
        return (FileDescriptor) getFileDescriptor.invoke(impl);
    }

}
