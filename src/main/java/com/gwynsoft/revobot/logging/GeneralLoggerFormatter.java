package com.gwynsoft.revobot.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Created by Landon on 12/18/2016.
 */
public class GeneralLoggerFormatter extends Formatter {

    public static final SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

    //                                    Time Level Class#Method Message
    private static final String FORMAT = "[%s] [%s] [%s#%s]: %s";

    @Override
    public String format(LogRecord record) {
        Date timestamp = new Date(record.getMillis() * 1000000);
        return String.format(FORMAT,
                timeStampFormat.format(timestamp),
                record.getLevel(),
                record.getSourceClassName(),
                record.getSourceClassName(),
                record.getMessage()
        );
    }
}

