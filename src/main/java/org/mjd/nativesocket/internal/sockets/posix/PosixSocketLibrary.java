/*
 * PROJECTNAME: IDPF MTG
 * AUTHOR: CGI
 * COPYRIGHT: EUMETSAT 2011
 */
package org.mjd.nativesocket.internal.sockets.posix;

import com.sun.jna.Library;
import com.sun.jna.Pointer;


/**
 * Interface to expose required socket functions from the socket.h library
 */
public interface PosixSocketLibrary extends Library
{

    /**
     * Exposes the native setsockopt from {@literal <sys/socket.h>}, specifically:
     *
     * <pre>
     * {@code int setsockopt(int sockfd, int level, int optname, const void* optval, socklen_t optlen);}
     * </pre>
     *
     * It is not recommended to use this interface directly, in an OSGi environment it will be restricted.
     *
     * @param sockFileDesc
     *            the file descriptor associated with the target socket
     * @param level
     *            protocol argument specifies the protocol level at which the option resides
     * @param optionName
     *            the option to set
     * @param optionValue
     *            integer pointer to the new value
     * @param optionLength
     *            the length of the new value
     * @return Upon successful completion, setsockopt() shall return 0. Otherwise, -1 shall be returned and
     *         errno set to indicate the error.
     */
    int setsockopt(int sockFileDesc, int level, int optionName, Pointer optionValue, int optionLength);

    /**
     * Exposes the native getsockopt from {@literal <sys/socket.h>}, specifically:
     *
     * <pre>
     * {@code int getsockopt(int sockfd, int level, int optname, const void* optval, socklen_t optlen);}
     * </pre>
     *
     * @param sockFileDesc
     *            the file descriptor associated with the target socket
     * @param level
     *            protocol argument specifies the protocol level at which the option resides
     * @param optionName
     *            the option to get
     * @param optionValue
     *            the buffer to receive the option value
     * @param optionLength
     *            the length of the buffer to receive the option value
     * @return Upon successful completion, getsockopt() shall return 0. Otherwise, -1 shall be returned
     *         and errno set to indicate the error.
     */
    int getsockopt(int sockFileDesc, int level, int optionName, Pointer optionValue, Pointer optionLength);
}
