package cn.flowerinsnow.mappicture.plugin.util;

import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

public final class ImageUtil {
    public static boolean isTransparent(@NotNull BufferedImage image, int x, int y) {
        return ((image.getRGB(x, y) << 24) & 0xFF) == 0;
    }
}
