package cn.flowerinsnow.mappicture.plugin.object.colour;

import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record SelectedMapColour(@NotNull MapColor mapColour, @NotNull MapColor.Brightness brightness) {
    public SelectedMapColour(@NotNull MapColor mapColour, @NotNull MapColor.Brightness brightness) {
        this.mapColour = Objects.requireNonNull(mapColour);
        this.brightness = Objects.requireNonNull(brightness);
    }
}
