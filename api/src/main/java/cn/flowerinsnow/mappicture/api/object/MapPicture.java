package cn.flowerinsnow.mappicture.api.object;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

/**
 * <p>代表一个地图像素画</p>
 */
public interface MapPicture {
    /**
     * <p>获取地图 Id</p>
     * <p>idcounts.dat 文件包含了当前地图的最新 ID。每个地图的文件名使用 map_<#>.dat 的格式，<#> 是该地图的独有数字。  —— zh.minecraft.wiki</p>
     *
     * @return 地图ID
     */
    int getMapId();

    /**
     * <p>根据绘制地图</p>
     *
     * @param image 图片
     * @param dither 使用 Floyd-Steinberg dithering 算法抖动。当参数为 false 时，不进行抖动。注：抖动会丢失所有透明度
     */
    void draw(@NotNull BufferedImage image, boolean dither);

    /**
     * <p>创建当前地图像素画的物品堆</p>
     *
     * @param amount 物品数量
     * @return 当前地图像素画的物品堆
     */
    @NotNull ItemStack createItemStack(int amount);
}
