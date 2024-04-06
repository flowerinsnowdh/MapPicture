package online.flowerinsnow.mappicture.api.object;

import online.flowerinsnow.mappicture.api.object.colour.FloydSteinbergDitherer;
import online.flowerinsnow.mappicture.api.object.colour.MapColourSelector;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;

/**
 * <p>代表一个地图像素画</p>
 */
public interface FilledMapPicture {
    /**
     * <p>获取地图ID</p>
     * <p>idcounts.dat文件包含了当前地图的最新ID。每个地图的文件名使用map_<#>.dat的格式，<#>是该地图的独有数字。  —— zh.minecraft.wiki</p>
     *
     * @return 地图ID
     */
    int getMapID();

    /**
     * <p>根据绘制地图</p>
     *
     * @param image 图片
     * @param colourSelector 地图颜色选择器
     * @param ditherer 使用 Floyd-Steinberg dithering 算法抖动。当参数为空时，不进行操作。注：抖动会丢失所有透明度
     */
    void draw(@NotNull BufferedImage image, @NotNull MapColourSelector colourSelector, @Nullable FloydSteinbergDitherer ditherer);

    /**
     * <p>创建当前地图像素画的物品堆</p>
     *
     * @param amount 物品数量
     * @return 当前地图像素画的物品堆
     */
    @NotNull ItemStack createItemStack(int amount);
}
