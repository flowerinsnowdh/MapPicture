package online.flowerinsnow.mappicture.plugin.object.colour;

import online.flowerinsnow.mappicture.api.object.colour.MapColour;
import online.flowerinsnow.mappicture.api.object.colour.MapColourBrightness;
import online.flowerinsnow.mappicture.api.object.colour.SelectedMapColour;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SelectedMapColourImpl implements SelectedMapColour {
    @NotNull private final MapColour mapColour;
    @NotNull private final MapColourBrightness brightness;

    public SelectedMapColourImpl(@NotNull MapColour mapColour, @NotNull MapColourBrightness brightness) {
        this.mapColour = Objects.requireNonNull(mapColour);
        this.brightness = Objects.requireNonNull(brightness);
    }

    @Override
    public @NotNull MapColour getColour() {
        return this.mapColour;
    }

    @Override
    public @NotNull MapColourBrightness getBrightness() {
        return this.brightness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectedMapColourImpl that = (SelectedMapColourImpl) o;
        return Objects.equals(this.mapColour, that.mapColour) && this.brightness == that.brightness;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.mapColour.hashCode();
        result = 31 * result + this.brightness.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SelectedMapColourImpl{" +
                "mapColour=" + this.mapColour +
                ", brightness=" + this.brightness +
                '}';
    }
}
