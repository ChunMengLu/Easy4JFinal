package net.dreamlu.easy.commons.logs;

import com.jfinal.log.Log;

import java.io.*;
import java.util.Locale;

/**
 * Created by L.cm on 2016/7/13.
 */
public class LogPrintStream extends PrintStream {
    private final Log log = Log.getLog("System.out");

    private final boolean error;

    public LogPrintStream(boolean error) {
        super(System.out);
        this.error = error;
    }

    @Override
    public void print(String s) {
        if (error) {
            log.error(s);
        } else {
            log.info(s);
        }
    }

    /**
     * 重写掉它，因为它会打印很多无用的新行
     */
    @Override
    public void println() {}

    @Override
    public void println(String x) {
        if (error) {
            log.error(x);
        } else {
            log.info(x);
        }
    }

    @Override
    public PrintStream printf(String format, Object... args) {
        if (error) {
            log.error(String.format(format, args));
        } else {
            log.info(String.format(format, args));
        }
        return this;
    }

    @Override
    public PrintStream printf(Locale l, String format, Object... args) {
        if (error) {
            log.error(String.format(l, format, args));
        } else {
            log.info(String.format(l, format, args));
        }
        return this;
    }
}
