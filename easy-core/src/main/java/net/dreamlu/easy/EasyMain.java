package net.dreamlu.easy;

import com.jfinal.core.JFinal;

/**
 * Created by L.cm on 2016/6/23.
 */
public class EasyMain {

    public static void run() {
        run(8080);
    }

    public static void run(int port) {
        String userDir = System.getProperty("user.dir");
        // 适应测试阶段的jetty，日志写入目录，tomcat写入tomcat/logs下
        String catalinaBase = System.getProperty("catalina.base", userDir);
        System.setProperty("catalina.base", catalinaBase);

        JFinal.start("src/main/webapp", port, "/", 10);
    }
}
