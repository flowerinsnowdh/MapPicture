package cn.flowerinsnow.mappicture.plugin.object;

import cn.flowerinsnow.mappicture.plugin.object.colour.MapColourSelector;
import cn.flowerinsnow.mappicture.plugin.object.colour.SelectedMapColour;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import cn.flowerinsnow.mappicture.api.object.MapPicture;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class MapPictureImpl implements MapPicture {
    private final MapItemSavedData data;
    private final MapColourSelector colourSelector;

    public MapPictureImpl(@NotNull MapItemSavedData data, @NotNull MapColourSelector colourSelector) {
        this.data = Objects.requireNonNull(data);
        this.colourSelector = Objects.requireNonNull(colourSelector);
    }

    @Override
    public int getMapId() {
        return this.data.id.id();
    }

    @Override
    public void draw(@NotNull BufferedImage image, boolean dither) {
        // 将图片缩放至 128*128
        Image scaledInstance = image.getScaledInstance(128, 128, BufferedImage.SCALE_DEFAULT);
        image = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();
        graphics.drawImage(scaledInstance, 0, 0, null);
        graphics.dispose();
        // 抖动
        if (dither) {
            this.colourSelector.getDitherer().dither(image);
        }

        // 绘制
        byte[] data = this.data.colors;
        for (int x = 0; x < 128; x++) {
            for (int y = 0; y < 128; y++) {
                int argb = image.getRGB(x, y);
                // 透明像素
                if (((argb >>> 24) & 0xFF) == 0) {
                    data[y * 128 + x] = (byte) 0;
                    continue;
                }
                SelectedMapColour select = colourSelector.select(argb);
                data[y * 128 + x] = select.mapColour().getPackedId(select.brightness());
            }
        }
    }

    @Override
    public @NotNull ItemStack createItemStack(int amount) {
        net.minecraft.world.item.ItemStack itemStack = new net.minecraft.world.item.ItemStack(Items.FILLED_MAP, amount);
        itemStack.set(DataComponents.MAP_ID, this.data.id);
        return CraftItemStack.asBukkitCopy(itemStack);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapPictureImpl that = (MapPictureImpl) o;
        return this.data.equals(that.data);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.data.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "FilledMapPictureImpl{" +
                "data=" + this.data +
                ", colourSelector=" + this.colourSelector +
                '}';
    }
}
