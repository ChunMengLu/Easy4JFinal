package net.dreamlu.easy.commons.logs;

import com.jfinal.log.Log;

public class Log4j2Log extends Log {

    private org.apache.logging.log4j.Logger log;

    Log4j2Log(Class<?> clazz) {
        log = org.apache.logging.log4j.LogManager.getLogger(clazz);
    }

    Log4j2Log(String name) {
        log = org.apache.logging.log4j.LogManager.getLogger(name);
    }

    // (marker, this, level, msg,params, t);注意参数顺序
    public void info(String message) {
        log.info(message);
    }

    public void info(String message, Throwable t) {
        log.info(message, t);
    }

    public void debug(String message) {
        log.debug(message);
    }

    public void debug(String message, Throwable t) {
        log.debug(message, t);
    }

    public void warn(String message) {
        log.warn(message);
    }

    public void warn(String message, Throwable t) {
        log.warn(message, t);
    }

    public void error(String message) {
        log.error(message);
    }

    public void error(String message, Throwable t) {
        log.error(message, t);
    }

    public void fatal(String message) {
        log.error(message);
    }

    public void fatal(String message, Throwable t) {
        log.error(message, t);
    }

    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    public boolean isFatalEnabled() {
        return log.isErrorEnabled();
    }

}
