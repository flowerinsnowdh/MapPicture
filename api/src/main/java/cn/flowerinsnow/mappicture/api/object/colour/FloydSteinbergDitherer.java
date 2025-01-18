package cn.flowerinsnow.mappicture.api.object.colour;

import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

/**
 * <p><a href="https://en.wikipedia.org/wiki/Floyd%E2%80%93Steinberg_dithering">Floyd–Steinberg dithering</a>抖动算法</p>
 */
public interface FloydSteinbergDitherer {
    void dither(@NotNull BufferedImage image);
}
