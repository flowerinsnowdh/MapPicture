package cn.flowerinsnow.mappicture.plugin.util;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public final class LogUtil {
    public static void throwing(@NotNull Logger logger, @NotNull Throwable throwable) {
        LogUtil.throwing(logger, throwable, false);
    }

    public static void throwing(@NotNull Logger logger, @NotNull Throwable throwable, boolean causedBy) {
        logger.severe((causedBy ? "Caused by: " : "") + throwable);
        for (StackTraceElement stackTraceElement : throwable.getStackTrace()) {
            logger.severe("\tat " + stackTraceElement);
        }
    }
}
