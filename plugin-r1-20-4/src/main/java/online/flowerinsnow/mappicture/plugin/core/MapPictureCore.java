package online.flowerinsnow.mappicture.plugin.core;

import net.minecraft.server.level.WorldServer;
import net.minecraft.world.item.ItemWorldMap;
import net.minecraft.world.level.material.MaterialMapColor;
import net.minecraft.world.level.saveddata.maps.WorldMap;
import net.minecraft.world.level.storage.WorldPersistentData;
import online.flowerinsnow.mappicture.api.MapPicture;
import online.flowerinsnow.mappicture.api.object.FilledMapPicture;
import online.flowerinsnow.mappicture.api.object.colour.MapColourSelector;
import online.flowerinsnow.mappicture.plugin.MapPicturePlugin;
import online.flowerinsnow.mappicture.plugin.object.FilledMapPictureImpl;
import online.flowerinsnow.mappicture.plugin.object.colour.MapColourSelectorNMSImpl;
import online.flowerinsnow.mappicture.plugin.util.NMSUtils;
import online.flowerinsnow.reflectionutils.object.ReflectionSession;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MapPictureCore implements MapPicture {
    @NotNull private final MapPicturePlugin plugin;
    @NotNull private final MapColourSelector mapColourSelector;

    public MapPictureCore(@NotNull MapPicturePlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin);

        this.mapColourSelector = new MapColourSelectorNMSImpl(MaterialMapColor.am);
    }

    @Override
    public @NotNull FilledMapPicture createFilledMapPicture() {
        WorldServer overworld = NMSUtils.getOverworld();

        /*
        int mapID = overworld.getNextMapId();
         */
        int mapID = overworld.v();
        return getFilledMapPicture(mapID);
    }

    @Override
    public @NotNull FilledMapPicture getFilledMapPicture(int mapID) {
        WorldServer overworld = NMSUtils.getOverworld();

        /*
        String mapName = FilledMapItem.getMapName(mapID);
        MapState mapState = overworld.getMapState(mapName);
        if (mapState == null) {
            MapState mapState = MapState.of((byte) 0, true, overworld.getRegistryKey());
            overworld.putMapState(FilledMapItem.setMapId(mapID), mapState);
            overworld.getPersistentStateManager().save(); // 保存
        }
         */
        String mapName = ItemWorldMap.a(mapID);
        WorldMap mapState = overworld.a(mapName);
        if (mapState == null) {
            mapState = WorldMap.a((byte) 0, true, overworld.ae());
            overworld.a(mapName, mapState);
            ReflectionSession.ofInstance(WorldPersistentData.class, overworld.u())
                    .invokeMethod("save", new Class[]{Boolean.TYPE}, null, true); // 保存
        }
        return new FilledMapPictureImpl(mapID, mapState);
    }

    @Override
    public @NotNull MapColourSelector getColourSelector() {
        return this.mapColourSelector;
    }
}
