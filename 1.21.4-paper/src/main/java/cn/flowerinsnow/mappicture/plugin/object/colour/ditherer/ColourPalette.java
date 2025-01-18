package cn.flowerinsnow.mappicture.plugin.object.colour.ditherer;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <a href="https://github.com/Regarrzo/Java-Floyd-Steinberg-Dithering/blob/master/src/ditherer/ColorPalette.java">https://github.com/Regarrzo/Java-Floyd-Steinberg-Dithering/blob/master/src/ditherer/ColorPalette.java</a>
 * @author <a href="https://github.com/Regarrzo/Java-Floyd-Steinberg-Dithering/commits?author=Regarrzo">Regarrzo</a>
 * @author <a href="https://github.com/flowerinsnowdh">flowerinsnow</a>
 */
public record ColourPalette(@NotNull Set<VectorRGB> colours) {
    public static @NotNull ColourPalette ofVectorRGBs(@NotNull Collection<VectorRGB> colours) throws IllegalArgumentException {
        Objects.requireNonNull(colours);
        if (colours.isEmpty()) {
            throw new IllegalArgumentException("empty colours");
        }
        return new ColourPalette(Set.copyOf(colours));
    }

    public static @NotNull ColourPalette ofColors(@NotNull Collection<Color> colors) throws IllegalArgumentException {
        Objects.requireNonNull(colors);
        if (colors.isEmpty()) {
            throw new IllegalArgumentException("empty colours");
        }
        return new ColourPalette(colors.stream().map(VectorRGB::ofColor).collect(Collectors.toUnmodifiableSet()));
    }

    public @NotNull VectorRGB getClosestMatch(@NotNull VectorRGB color) {
        Objects.requireNonNull(color);
        //noinspection OptionalGetWithoutIsPresent
        return this.colours.stream().min(Comparator.comparingDouble(a -> a.fastDifferenceTo(color))).get();
    }

}