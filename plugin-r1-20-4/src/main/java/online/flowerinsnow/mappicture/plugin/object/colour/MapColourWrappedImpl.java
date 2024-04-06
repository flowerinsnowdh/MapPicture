package online.flowerinsnow.mappicture.plugin.object.colour;

import net.minecraft.world.level.material.MaterialMapColor;
import online.flowerinsnow.mappicture.api.object.colour.MapColour;
import online.flowerinsnow.mappicture.api.object.colour.MapColourBrightness;
import online.flowerinsnow.reflectionutils.object.ReflectionSession;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

public class MapColourWrappedImpl implements MapColour {
    @NotNull private final MaterialMapColor mapColor;

    public MapColourWrappedImpl(@NotNull MaterialMapColor mapColor) {
        this.mapColor = Objects.requireNonNull(mapColor);
    }

    @Override
    public byte getID() {
//        return (byte) this.materialMapColor.id;
        return (byte) this.mapColor.al;
    }

    @Override
    public byte getIDOfBrightness(@NotNull MapColourBrightness brightness) {
        // 先通过本项目的 Brightness 对象 获取 Minecraft 的 Brightness 对象
        /*
        MapColor.Brightness b = MapColor.Brightness.validateAndGet(this.getID());
        return this.mapColor.getRenderColorByte(b);
         */
        MaterialMapColor.a b = ReflectionSession.ofTypeNotNull("net.minecraft.world.level.material.MaterialMapColor$a")
                .invokeMethod("a", new Class[]{Integer.TYPE}, MaterialMapColor.a.class, brightness.id);
        return this.mapColor.b(b);
    }

    @Override
    public @NotNull Color getBaseColor() {
        /*
        return new Color(this.mapColor.color);
         */
        return new Color(this.mapColor.ak);
    }

    @Override
    public @NotNull Color getColorOfBrightness(@NotNull MapColourBrightness brightness) {
        // 地图会根据方块的地图颜色基色属性和颜色修正来着色。方块的基色会乘以135, 180, 220或255，再除以255来得到地图色。而除以这四个数中的哪一个数，则取决于此方块北侧那个方块的高度。  —— zh.minecraft.wiki
        Color baseColor = this.getBaseColor();
        int r = baseColor.getRed() * brightness.brightness / 255;
        int g = baseColor.getGreen() * brightness.brightness / 255;
        int b = baseColor.getBlue() * brightness.brightness / 255;
        return new Color(r, g, b);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapColourWrappedImpl that = (MapColourWrappedImpl) o;
        return this.mapColor == that.mapColor;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.mapColor.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MapColorWrappedImpl{" +
                "mapColor=" + this.mapColor +
                '}';
    }
}
