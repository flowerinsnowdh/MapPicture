package online.flowerinsnow.mappicture.plugin.object.colour.ditherer;

import online.flowerinsnow.mappicture.api.object.colour.FloydSteinbergDitherer;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * <p><a href="https://github.com/Regarrzo/Java-Floyd-Steinberg-Dithering/blob/master/src/ditherer/Ditherer.java">https://github.com/Regarrzo/Java-Floyd-Steinberg-Dithering/blob/master/src/ditherer/Ditherer.java</a></p>
 */
public class JavaFloydSteinbergDitherer implements FloydSteinbergDitherer {

    private ColorPalette palette;

    public JavaFloydSteinbergDitherer(ColorPalette palette) {
        this.setPalette(palette);
    }

    public ColorPalette getPalette() {
        return palette;
    }

    public void setPalette(ColorPalette palette) {
        this.palette = palette;
    }

    public void dither(@NotNull BufferedImage img) {
        Objects.requireNonNull(img);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {

                VectorRGB current_color = new VectorRGB(img.getRGB(x, y));
                VectorRGB closest_match = palette.getClosestMatch(current_color);
                VectorRGB error = current_color.subtract(closest_match);

                img.setRGB(x, y, closest_match.toRGB());

                if (!(x == img.getWidth() - 1)) {
                    img.setRGB(x + 1, y,
                            ((new VectorRGB(img.getRGB(x + 1, y)).add(error.scalarMultiply((float) 7 / 16)))
                                    .clip(0, 255).toRGB()));

                    if (!(y == img.getHeight() - 1)) {
                        img.setRGB(x + 1, y + 1,
                                ((new VectorRGB(img.getRGB(x + 1, y + 1)).add(error.scalarMultiply((float) 1 / 16)))
                                        .clip(0, 255).toRGB()));
                    }
                }

                if (!(y == img.getHeight() - 1))

                {

                    img.setRGB(x, y + 1,
                            ((new VectorRGB(img.getRGB(x, y + 1)).add(error.scalarMultiply((float) 3 / 16)))
                                    .clip(0, 255).toRGB()));

                    if (!(x == 0)) {
                        img.setRGB(x - 1, y + 1, ((new VectorRGB(img.getRGB(x - 1, y + 1))
                                .add(error.scalarMultiply(5.0F / 16.0F)).clip(0, 255).toRGB())));

                    }
                }

            }

        }

    }

}