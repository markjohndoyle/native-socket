package org.mjd.nativesocket.internal.fd;

import java.net.Socket;

/**
 * File Descriptor accessor class.
 * Used to extract the file descriptor of any abstraction that uses the
 */
public interface FileDescriptorAccessor
{

    /**
     * Exception thrown when the file descriptor cannot be extracted/accessed.
     *
     */
    final class FileDescriptorException extends Exception
    {
        public FileDescriptorException(Exception e)
        {
            super(e);
        }
    }

    /**
     * Extracts the underlying file descriptor for a {@link Socket}
     * @param socket the {@link Socket} to extract the file descriptor for.
     * @return the int representation of the file descriptor
     * @throws FileDescriptorException
     */
    long extractFileDescriptor(Socket socket) throws FileDescriptorException;
}
