/*
 * PROJECTNAME: IDPF MTG
 * AUTHOR: CGI
 * COPYRIGHT: EUMETSAT 2011
 */
package org.mark.nativesocket.jna;

import com.sun.jna.Library;
import com.sun.jna.ptr.IntByReference;


/**
 * @author mark
 *
 */
public interface SocketLibrary extends Library
{
    // int getsockopt(int sockfd, int level, int optname, void* optval, socklen_t* optlen);

    /**
     * int setsockopt(int sockfd, int level, int optname, const void* optval, socklen_t optlen);
     * 
     * @param sockFileDesc
     * @param level
     * @param optionName
     * @param optionValue
     * @param optionLength
     * @return
     */
    int setsockopt(int sockFileDesc, int level, int optionName, IntByReference optionValue, int optionLength);
    
    
}
