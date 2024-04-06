package online.flowerinsnow.mappicture.plugin.object;

import net.minecraft.server.level.WorldServer;
import net.minecraft.world.item.ItemWorldMap;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.WorldMap;
import net.minecraft.world.level.storage.WorldPersistentData;
import online.flowerinsnow.mappicture.api.object.FilledMapPicture;
import online.flowerinsnow.mappicture.api.object.colour.FloydSteinbergDitherer;
import online.flowerinsnow.mappicture.api.object.colour.MapColourSelector;
import online.flowerinsnow.mappicture.api.object.colour.SelectedMapColour;
import online.flowerinsnow.mappicture.plugin.util.NMSUtils;
import online.flowerinsnow.reflectionutils.object.ReflectionSession;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FilledMapPictureImpl implements FilledMapPicture {
    private final int mapID;
    private final WorldMap mapState;

    public FilledMapPictureImpl(int mapID, WorldMap mapState) {
        this.mapID = mapID;
        this.mapState = mapState;
    }


    @Override
    public int getMapID() {
        return this.mapID;
    }

    @Override
    public void draw(@NotNull BufferedImage image, @NotNull MapColourSelector colourSelector, @Nullable FloydSteinbergDitherer ditherer) {
        // 缩放
        Image scaledInstance = image.getScaledInstance(128, 128, BufferedImage.SCALE_DEFAULT);
        image = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();
        graphics.drawImage(scaledInstance, 0, 0, null);
        graphics.dispose();
        // 抖动
        if (ditherer != null) {
            ditherer.dither(image);
        }

        // 绘制
        byte[] data = mapState.g;
        for (int x = 0; x < 128; x++) {
            for (int y = 0; y < 128; y++) {
                int argb = image.getRGB(x, y);
                // 透明像素
                if (((argb >>> 24) & 0xFF) == 0) {
                    data[y * 128 + x] = (byte) 0;
                    continue;
                }
                SelectedMapColour select = colourSelector.select(new Color(argb));
                data[y * 128 + x] = select.getColour().getIDOfBrightness(select.getBrightness());
            }
        }

        /*
        mapState.locked = true;
         */
        mapState.h = true;

        WorldServer overworld = NMSUtils.getOverworld();

        /*
        overworld.getPersistentStateManager().save(); // 保存
         */
        ReflectionSession.ofInstance(WorldPersistentData.class, overworld.u())
                .invokeMethod("save", new Class[]{Boolean.TYPE}, null, true); // 保存
    }

    @Override
    public @NotNull ItemStack createItemStack(int amount) {
        /*
        ItemStack itemStack = new ItemStack(Items.FILLED_MAP, amount);
        FilledMapItem.setMapId(itemStack, this.getMapID());
         */
        net.minecraft.world.item.ItemStack itemStack = new net.minecraft.world.item.ItemStack(Items.rR, amount);
        ReflectionSession.ofType(ItemWorldMap.class)
                .invokeMethod("a", new Class[]{net.minecraft.world.item.ItemStack.class, Integer.TYPE}, null, itemStack, this.getMapID());
        return CraftItemStack.asBukkitCopy(itemStack);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilledMapPictureImpl that = (FilledMapPictureImpl) o;
        return this.mapID == that.mapID && this.mapState.equals(that.mapState);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.mapID;
        result = 31 * result + this.mapState.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "FilledMapPictureImpl{" +
                "mapID=" + this.mapID +
                ", mapState=" + this.mapState +
                '}';
    }
}
