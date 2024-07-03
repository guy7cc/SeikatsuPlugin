package io.github.guy7cc.seikatsu.system;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;

import java.util.logging.Logger;

public class LoggerWrapper {
    public final Logger logger;

    public LoggerWrapper(Logger logger) {
        this.logger = logger;
    }

    private String setCallerInfo(String msg) {
        StackTraceElement trace = Thread.currentThread().getStackTrace()[3];
        String className = trace.getClassName();
        String methodName = trace.getMethodName();
        return String.format("[%s.%s] %s", className, methodName, msg);
    }

    public void sayHi() {
        if (!SeikatsuPlugin.ON_RELEASE) logger.info(setCallerInfo("hi"));
    }

    public void info(String format, Object... args){
        logger.info(setCallerInfo(String.format(format, args)));
    }

    public void warn(String format, Object... args){
        logger.warning(setCallerInfo(String.format(format, args)));
    }

    public void exception(String msg, Exception exception) {
        logger.warning(setCallerInfo(msg));
        logger.warning(exception.getMessage());
        exception.printStackTrace();
    }

    public void debug(String msg) {
        if (!SeikatsuPlugin.ON_RELEASE) logger.info(setCallerInfo(msg));
    }
}
