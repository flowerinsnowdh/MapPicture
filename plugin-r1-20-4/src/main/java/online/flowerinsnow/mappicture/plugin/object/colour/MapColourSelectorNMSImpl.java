package online.flowerinsnow.mappicture.plugin.object.colour;

import net.minecraft.world.level.material.MaterialMapColor;
import online.flowerinsnow.mappicture.api.object.colour.*;
import online.flowerinsnow.mappicture.plugin.object.colour.ditherer.ColorPalette;
import online.flowerinsnow.mappicture.plugin.object.colour.ditherer.JavaFloydSteinbergDitherer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class MapColourSelectorNMSImpl implements MapColourSelector {
    private final HashMap<Integer, SelectedMapColourImpl> mapColourList = new HashMap<>();
    private final FloydSteinbergDitherer ditherer;

    public MapColourSelectorNMSImpl(MaterialMapColor[] mapColours) {
        // 遍历原版地图颜色，分别记录它们在不同阴影下的实际渲染颜色
        for (MaterialMapColor nmsMapColour : mapColours) {
            // 如果当前地图颜色为空或者是透明，则跳过
            /*
            if (nmsMapColour == null || nmsMapColour == MapColor.CLEAR) {
             */
            if (nmsMapColour == null || nmsMapColour == MaterialMapColor.a) {
                continue;
            }
            MapColourWrappedImpl mapColourWrapped = new MapColourWrappedImpl(nmsMapColour);
            this.addColour(mapColourWrapped);
        }

        // 统计所有颜色，并制作调色盘，合成抖动算法
        List<Color> allColoursList = mapColourList.keySet().stream().map(Color::new).toList();
        Color[] allColoursArray = new Color[allColoursList.size()];
        allColoursList.toArray(allColoursArray);
        ditherer = new JavaFloydSteinbergDitherer(new ColorPalette(allColoursArray));
    }

    @Override
    public @NotNull Set<MapColour> getSelectableColourList() {
        return this.mapColourList.values().stream().map(SelectedMapColourImpl::getColour).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public @NotNull MapColourSelector addColour(@NotNull MapColour mapColour) {
        for (MapColourBrightness brightness : MapColourBrightness.values()) {
            Color colorOfBrightness = mapColour.getColorOfBrightness(brightness);
            this.mapColourList.put(colorOfBrightness.getRGB() & 0xFFFFFF, new SelectedMapColourImpl(mapColour, brightness));
        }
        return this;
    }

    @Override
    public @NotNull SelectedMapColour select(@NotNull Color color) {
        //noinspection OptionalGetWithoutIsPresent
        return mapColourList.entrySet().stream()
                .min(Comparator.comparingDouble(it -> euclideanDistance(it.getKey(), color.getRGB())))
                .map(Map.Entry::getValue)
                .get();
    }

    @Override
    public @NotNull FloydSteinbergDitherer getDitherer() {
        return this.ditherer;
    }

    private static double euclideanDistance(int a, int b) {
        int ar = (a >>> 16) & 0xFF;
        int ag = (a >>> 8) & 0xFF;
        int ab = a & 0xFF;
        int br = (b >>> 16) & 0xFF;
        int bg = (b >>> 8) & 0xFF;
        int bb = b & 0xFF;
        // 欧几里得距离： sqrt[(r₁ - r₂)² + (g₁ - g₂)² + (b₁ - b₂)²]
        // 这里不需要注意 a 和 b 的顺序，它们两个调换位置不会影响结果
        // 这里不需要注意 r₁/g₁/b₁ - r₂/g₂/b₂ 可能为负数结果，因为它们最后都会进行平方运算
        return sqrt(pow(ar - br, 2) + pow((ag - bg), 2) + pow(ab - bb, 2));
    }
}
