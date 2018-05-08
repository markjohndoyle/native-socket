package org.mjd.nativesocket.testsupport;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


/**
 * Linux specific utilities.
 */
public final class LinuxUtils
{
    private static final String PROC_ROOT = "/proc";

    private LinuxUtils()
    {
        // Utility class.
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
