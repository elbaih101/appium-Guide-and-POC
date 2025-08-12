package org.example.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtils {
    public static final String LOGS_PATH= "target/logs";
    private LogUtils() {
        super();
    }


    public static Logger logger(){
        return LogManager.getLogger(Thread.currentThread().getStackTrace()[3].getClassName());
    }

    public static void logInfo(String... message){
        logger().info(() -> String.join(" ", message));
    }
    public static void logError(String... message){
        logger().info(() -> String.join(" ", message));

    }
    public static void logWarn(String... message){
        logger().warn(() -> String.join(" ", message));

    }
    public static void logDebug(String... message){
        logger().debug(() -> String.join(" ", message));

    }
    public static void logFatal(String... message){
        logger().fatal(() -> String.join(" ", message));

    }
    public static void logTrace(String... message){
        logger().trace(() -> String.join(" ", message));

    }
}
