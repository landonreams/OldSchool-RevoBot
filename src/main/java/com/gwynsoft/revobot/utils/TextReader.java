package com.gwynsoft.revobot.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Landon on 12/18/2016.
 */
public class TextReader {
    public static String getRawText(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}
