package com.gwynsoft.revobot.utils;

/**
 * Created by Landon on 12/22/2016.
 */
public class CommandFileReader {
    public enum ReturnCode { SUCCESS, FILE_NOT_FOUND; }

    public static ReturnCode readToStringBuilder(StringBuilder message, String directory) {
        try {
            message.append(TextUtil.getRawText(directory));
            return ReturnCode.SUCCESS;
        } catch (Exception e) {
           return ReturnCode.FILE_NOT_FOUND;
        }
    }
}
