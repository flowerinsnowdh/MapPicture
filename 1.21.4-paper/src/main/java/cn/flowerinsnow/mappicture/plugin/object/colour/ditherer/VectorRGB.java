package cn.flowerinsnow.mappicture.plugin.object.colour.ditherer;


import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.Objects;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * <a href="https://github.com/Regarrzo/Java-Floyd-Steinberg-Dithering/blob/master/src/ditherer/VectorRGB.java">https://github.com/Regarrzo/Java-Floyd-Steinberg-Dithering/blob/master/src/ditherer/VectorRGB.java</a>
 * @author <a href="https://github.com/Regarrzo/Java-Floyd-Steinberg-Dithering/commits?author=Regarrzo">Regarrzo</a>
 */
public record VectorRGB(int r, int g, int b) {
    public static @NotNull VectorRGB ofRGB(int red, int green, int blue) {
        return new VectorRGB(red, green, blue);
    }

    public static @NotNull VectorRGB ofRGB(int rgb) {
        return VectorRGB.ofRGB(
                (rgb >> 16) & 0xFF,
                (rgb >> 8) & 0xFF,
                rgb & 0xFF
        );
    }

    public static @NotNull VectorRGB ofColor(@NotNull Color colour) {
        Objects.requireNonNull(colour);
        return VectorRGB.ofRGB(colour.getRed(), colour.getGreen(), colour.getBlue());
    }

    public int toRGB() {
        return 0xFF000000 | ((this.r & 0xFF) << 16) | ((this.g & 0xFF) << 8) | (this.b & 0xFF);
    }

    public @NotNull Color toColor() {
        return new Color(this.r, this.g, this.b);
    }

    public @NotNull VectorRGB subtract(@NotNull VectorRGB that) {
        Objects.requireNonNull(that);
        return VectorRGB.ofRGB(this.r - that.r, this.g - that.g, this.b - that.b);
    }

    public @NotNull VectorRGB add(@NotNull VectorRGB that) {
        Objects.requireNonNull(that);
        return VectorRGB.ofRGB(this.r + that.r, this.g + that.g, this.b + that.b);
    }

    public double euclideanDistance(@NotNull VectorRGB that) {
        // 欧几里得距离： sqrt[(r₁ - r₂)² + (g₁ - g₂)² + (b₁ - b₂)²]
        // 这里不需要注意 a 和 b 的顺序，它们两个调换位置不会影响结果
        // 这里不需要注意 r₁/g₁/b₁ - r₂/g₂/b₂ 可能为负数结果，因为它们最后都会进行平方运算
        return sqrt(pow(this.r - that.r, 2) + pow((this.g - that.g), 2) + pow(this.b - that.b, 2));
    }

    public int fastDifferenceTo(VectorRGB that) {
        VectorRGB difference = this.subtract(that);
        return Math.abs(difference.r) + Math.abs(difference.g) + Math.abs(difference.b);
    }

    public @NotNull VectorRGB multiply(float scalar) {
        return VectorRGB.ofRGB(
                (int) (this.r * scalar),
                (int) (this.g * scalar),
                (int) (this.b * scalar)
        );
    }

    public @NotNull VectorRGB clip(int minimum, int maximum) {
        return VectorRGB.ofRGB(
                Math.min(Math.max(this.r, minimum), maximum),
                Math.min(Math.max(this.g, minimum), maximum),
                Math.min(Math.max(this.b, minimum), maximum)
        );
    }
}
