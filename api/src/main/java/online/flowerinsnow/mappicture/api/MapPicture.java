package online.flowerinsnow.mappicture.api;

import online.flowerinsnow.mappicture.api.object.FilledMapPicture;
import online.flowerinsnow.mappicture.api.object.colour.MapColourSelector;
import org.bukkit.event.server.MapInitializeEvent;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Map Picture API类</p>
 */
public interface MapPicture {
    static MapPicture getInstance() {
        return MapPictureAPI.getInstance();
    }

    /**
     * <p>创建一张新的空的带ID的地图像素画</p>
     * <p>地图ID由世界顺序分配</p>
     * <p>此方法会触发 {@link MapInitializeEvent} 事件</p>
     *
     * @return 一张新的空的带ID的地图像素画
     */
    @NotNull FilledMapPicture createFilledMapPicture();

    /**
     * <p>获取ID对应的地图像素画</p>
     * <p>若该ID对应的地图存在，将会获取</p>
     * <p>若该ID对应的地图不存在，将会创建。同时此方法会触发 {@link MapInitializeEvent} 事件</p>
     * <p>注：此举不会更新世界最新地图ID（idcounts），新的玩家创建的地图可能会覆盖此地图或被此地图覆盖</p>
     *
     * @return 一张新的空的带ID的地图像素画
     */
    @NotNull FilledMapPicture getFilledMapPicture(int mapID);

    /**
     * <p>获取地图颜色选择器</p>
     * <p>会通过可用地图颜色自动生成，不包含透明（CLEAR）对象</p>
     *
     * @return 地图颜色选择器
     */
    @NotNull MapColourSelector getColourSelector();
}
