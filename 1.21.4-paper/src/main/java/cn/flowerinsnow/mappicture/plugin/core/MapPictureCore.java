package cn.flowerinsnow.mappicture.plugin.core;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import cn.flowerinsnow.mappicture.api.IMapPicture;
import cn.flowerinsnow.mappicture.api.object.MapPicture;
import cn.flowerinsnow.mappicture.plugin.object.MapPictureImpl;
import cn.flowerinsnow.mappicture.plugin.object.colour.MapColourSelector;
import cn.flowerinsnow.mappicture.plugin.util.InternalCodeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class MapPictureCore implements IMapPicture {
    @NotNull private final MapColourSelector mapColourSelector;

    public MapPictureCore() {
        this.mapColourSelector = new MapColourSelector(MapColor.MATERIAL_COLORS);
    }

    @Override
    public @NotNull MapPictureImpl createMapPicture() {
        ServerLevel overworld = InternalCodeUtil.getOverworld();
        MapId mapId = MapItem.createNewSavedData(overworld, 0, 0, 0, false, false, overworld.dimension());
        MapItemSavedData mapData = overworld.getMapData(mapId);
        //noinspection DataFlowIssue
        mapData.locked = true;
        return new MapPictureImpl(mapData, this.mapColourSelector);
    }

    @Override
    public @NotNull Optional<MapPicture> getMapPicture(int mapId) {
        return Optional.ofNullable(
                InternalCodeUtil.getOverworld().getMapData(new MapId(mapId))
        ).map((MapItemSavedData data) -> new MapPictureImpl(data, MapPictureCore.this.mapColourSelector));
    }

    public @NotNull MapColourSelector getColourSelector() {
        return this.mapColourSelector;
    }
}
