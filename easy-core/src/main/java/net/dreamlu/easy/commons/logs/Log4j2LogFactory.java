package net.dreamlu.easy.commons.logs;

import com.jfinal.log.ILogFactory;
import com.jfinal.log.Log;

public class Log4j2LogFactory implements ILogFactory {

    @Override
    public Log getLog(Class<?> clazz) {
        return new Log4j2Log(clazz);
    }

    @Override
    public Log getLog(String name) {
        return new Log4j2Log(name);
    }
}
