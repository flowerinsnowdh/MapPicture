package cn.flowerinsnow.mappicture.plugin.object.colour.ditherer;

import cn.flowerinsnow.mappicture.api.object.colour.FloydSteinbergDitherer;
import cn.flowerinsnow.mappicture.plugin.util.ImageUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * <p><a href="https://github.com/Regarrzo/Java-Floyd-Steinberg-Dithering/blob/master/src/ditherer/Ditherer.java">https://github.com/Regarrzo/Java-Floyd-Steinberg-Dithering/blob/master/src/ditherer/Ditherer.java</a></p>
 *
 * @author <a href="https://github.com/Regarrzo/Java-Floyd-Steinberg-Dithering/commits?author=Regarrzo">Regarrzo</a>
 * @author <a href="https://github.com/flowerinsnowdh">flowerinsnow</a>
 */
public record JavaFloydSteinbergDitherer(@NotNull ColourPalette palette) implements FloydSteinbergDitherer {
    public JavaFloydSteinbergDitherer(@NotNull ColourPalette palette) {
        this.palette = Objects.requireNonNull(palette);
    }

    public static @NotNull JavaFloydSteinbergDitherer ofPalette(@NotNull ColourPalette palette) {
        Objects.requireNonNull(palette);
        return new JavaFloydSteinbergDitherer(palette);
    }

    public void dither(@NotNull BufferedImage img) {
        Objects.requireNonNull(img);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int argb = img.getRGB(x, y);

                VectorRGB currentColour = VectorRGB.ofRGB(argb);
                VectorRGB closestMatch = this.palette.getClosestMatch(currentColour);
                VectorRGB error = currentColour.subtract(closestMatch);

                img.setRGB(x, y, closestMatch.toRGB());

                if (x != img.getWidth() - 1) {
                    img.setRGB(
                            x + 1, y,
                            VectorRGB.ofRGB(img.getRGB(x + 1, y))
                                    .add(error.multiply((float) 7 / 16))
                                    .clip(0, 255)
                                    .toRGB()
                    );

                    if (y != img.getHeight() - 1) {
                        img.setRGB(
                                x + 1, y + 1,
                                VectorRGB.ofRGB(img.getRGB(x + 1, y + 1))
                                        .add(error.multiply((float) 1 / 16))
                                        .clip(0, 255)
                                        .toRGB());
                    }
                }

                if (y != img.getHeight() - 1) {
                    img.setRGB(x, y + 1,
                            VectorRGB.ofRGB(img.getRGB(x, y + 1))
                                    .add(error.multiply((float) 3 / 16))
                                    .clip(0, 255).toRGB());

                    if (x != 0) {
                        img.setRGB(
                                x - 1, y + 1,
                                VectorRGB.ofRGB(img.getRGB(x - 1, y + 1))
                                        .add(error.multiply(5.0F / 16.0F))
                                        .clip(0, 255)
                                        .toRGB());

                    }
                }

            }

        }

    }

}