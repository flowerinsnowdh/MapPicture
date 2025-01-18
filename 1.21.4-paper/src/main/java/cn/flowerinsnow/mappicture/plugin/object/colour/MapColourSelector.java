package cn.flowerinsnow.mappicture.plugin.object.colour;

import cn.flowerinsnow.mappicture.api.object.colour.*;
import net.minecraft.world.level.material.MapColor;
import cn.flowerinsnow.mappicture.plugin.object.colour.ditherer.ColourPalette;
import cn.flowerinsnow.mappicture.plugin.object.colour.ditherer.JavaFloydSteinbergDitherer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class MapColourSelector {
    // Key: RGB
    private final HashMap<Integer, SelectedMapColour> mapColourList = new HashMap<>();
    private final FloydSteinbergDitherer ditherer;

    public MapColourSelector(MapColor[] mapColours) {
        // 遍历原版地图颜色，分别记录它们在不同阴影下的实际渲染颜色
        for (MapColor mapColour : mapColours) {
            // 如果当前地图颜色为空或者是透明，则跳过
            if (mapColour == null || mapColour == MapColor.NONE) {
                continue;
            }
            this.addColour(mapColour);
        }

        // 统计所有颜色，并制作调色盘，合成抖动算法
        this.ditherer = JavaFloydSteinbergDitherer.ofPalette(
                ColourPalette.ofColors(
                        this.mapColourList
                                .keySet()
                                .stream()
                                .map(Color::new)
                                .toList()
                )
        );
    }

    public void addColour(@NotNull MapColor mapColour) {
        for (MapColor.Brightness brightness : MapColor.Brightness.values()) {
            this.mapColourList.put(mapColour.calculateARGBColor(brightness) & 0xFFFFFF, new SelectedMapColour(mapColour, brightness));
        }
    }

    public @NotNull SelectedMapColour select(int rgb) {
        //noinspection OptionalGetWithoutIsPresent
        return mapColourList.entrySet().stream()
                .min(Comparator.comparingDouble(it -> euclideanDistance(it.getKey(), rgb)))
                .map(Map.Entry::getValue)
                .get();
    }

    public @NotNull FloydSteinbergDitherer getDitherer() {
        return this.ditherer;
    }

    private static double euclideanDistance(int a, int b) {
        int r1 = (a >>> 16) & 0xFF;
        int g1 = (a >>> 8) & 0xFF;
        int b1 = a & 0xFF;
        int r2 = (b >>> 16) & 0xFF;
        int g2 = (b >>> 8) & 0xFF;
        int b2 = b & 0xFF;
        // 欧几里得距离： sqrt[(r₁ - r₂)² + (g₁ - g₂)² + (b₁ - b₂)²]
        // 这里不需要注意 a 和 b 的顺序，它们两个调换位置不会影响结果
        // 这里不需要注意 r₁/g₁/b₁ - r₂/g₂/b₂ 可能为负数结果，因为它们最后都会进行平方运算
        return sqrt(pow(r1 - r2, 2) + pow((g1 - g2), 2) + pow(b1 - b2, 2));
    }
}
