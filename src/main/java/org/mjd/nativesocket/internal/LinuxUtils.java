package org.mjd.nativesocket.internal;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.SocketImpl;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.sun.jna.Native;
import com.sun.jna.Structure;

import static sun.misc.SharedSecrets.getJavaIOFileDescriptorAccess;


/**
 * Linux specific utilities.
 */
public final class LinuxUtils
{
    private static final String C_LIB_NAME = "c";
    private static final String PROC_ROOT = "/proc";
    private static final int SET_OPT_ERROR = -1;

    private static final class SocketData
    {
        /** Data class */
        private SocketData()
        {
        }


        public static final String GET_IMPL_METHOD = "getImpl";
    }

    private static final class SocketImplData
    {
        /** Data class */
        private SocketImplData()
        {
        }


        public static final String GET_FILE_DESC_METHOD = "getFileDescriptor";
    }


    private LinuxUtils()
    {
        // Utility class.
    }

    /**
     * Loads the Native C standard library and returns it under the JNA {@link Structure} interface of type T
     *
     * @param <T>
     *            the interface type to load the native library behind
     * @param libraryInterface
     *            the interface to expose the C standard library under
     * @return native library of type T
     */
    public static <T> T loadStandardLibrary(Class<T> libraryInterface)
    {
        return Native.loadLibrary(C_LIB_NAME, libraryInterface);
    }

    /**
     * Extracts the Linux file descriptor from the given socket.
     * 
     * JDK and platform specific - Requires {@link Socket} to use {@link SocketImpl} and those classes to
     * maintain {@link Socket#getImpl()} and {@link SocketImpl#getFileDescriptor()}.
     * 
     * @param socket
     *            the {@link Socket} to get the file descriptor for.
     * @return int representing the Linux file descriptor of the underlying socket.
     * @throws NoSuchMethodException
     *             if the required methods don not exist.
     * @throws IllegalAccessException
     *             if we can't force access to the methods.
     * @throws InvocationTargetException
     *             if we can't invoke the methods on the instances that make up the socket
     */
    // SharedSecrets required to get the file descriptor.
    @SuppressWarnings("restriction")
    public static int extractFileDescriptor(Socket socket) throws NoSuchMethodException, IllegalAccessException,
                                                           InvocationTargetException
    {
        Method getImpl = Socket.class.getDeclaredMethod(SocketData.GET_IMPL_METHOD);
        Method getFileDescriptor = SocketImpl.class.getDeclaredMethod(SocketImplData.GET_FILE_DESC_METHOD);
        getImpl.setAccessible(true);
        getFileDescriptor.setAccessible(true);

        SocketImpl impl = (SocketImpl) getImpl.invoke(socket);
        FileDescriptor javaFileDesc = (FileDescriptor) getFileDescriptor.invoke(impl);
        return getJavaIOFileDescriptorAccess().get(javaFileDesc);
    }

    /**
     * Processes the last OS error and throw an {@link IllegalStateException} when there is an error on a
     * native socket library call. Used to handle return codes from C calls to socket related functions.
     * 
     * @param result
     *            the return code.
     */
    public static void callC(int result)
    {
        if (result == SET_OPT_ERROR)
        {
            throw new IllegalStateException("Setting socket option failed; the last OS error = " +
                            Native.getLastError());
        }
    }
    
    /**
     * Reads the proc file at the give sub path. Returns all lines in the proc files as a list of Strings
     * 
     * @param subPaths
     *            the sub path to the proc file
     * @return contents of the proc file in a List.
     * @throws IOException
     *             if the proc file cannot be read
     */
    public static List<String> readProc(String ... subPaths) throws IOException
    {
        Path procPath = Paths.get(PROC_ROOT, subPaths);
        return Files.readAllLines(procPath, Charset.defaultCharset());
    }
}
