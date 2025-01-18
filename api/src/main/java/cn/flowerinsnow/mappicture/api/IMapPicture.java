package cn.flowerinsnow.mappicture.api;

import cn.flowerinsnow.mappicture.api.object.MapPicture;
import org.bukkit.event.server.MapInitializeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * <p>Map Picture API类</p>
 */
public interface IMapPicture {
    static IMapPicture getInstance() {
        return MapPictureAPI.getInstance();
    }

    /**
     * <p>创建一张新的空的带 ID 的地图像素画</p>
     * <p>地图 ID 由世界顺序分配</p>
     * <p>此方法会触发 {@link MapInitializeEvent} 事件</p>
     *
     * @return 一张新的空的带ID的地图像素画
     */
    @NotNull MapPicture createMapPicture();

    /**
     * <p>获取 ID 对应的地图像素画</p>
     * <p>若该 ID 对应的地图不存在，将会返回 null</p>
     *
     * @return 一张新的空的带ID的地图像素画
     */
    @NotNull Optional<MapPicture> getMapPicture(int mapID);
}
