package online.flowerinsnow.mappicture.api.object.colour;

import online.flowerinsnow.mappicture.api.object.exception.DithererNotAvailableException;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Set;

/**
 * <p>将指定颜色选定为地图颜色的选择器</p>
 */
public interface MapColourSelector {
    /**
     * <p>获取所有选择器内的地图颜色</p>
     *
     * @return 所有选择器内的颜色，不可变集合
     */
    @NotNull Set<MapColour> getSelectableColourList();

    /**
     * <p>添加可选择的地图颜色</p>
     *
     * @param mapColour 可选择的地图颜色
     * @return 当前对象
     */
    @NotNull MapColourSelector addColour(@NotNull MapColour mapColour);

    /**
     * <p>从RGB颜色选择一个最接近的颜色</p>
     *
     * @return mapColour 最接近的颜色
     */
    @NotNull SelectedMapColour select(@NotNull Color color);

    /**
     * <p>获取当前颜色选择器的抖动算法</p>
     *
     * @return 当前颜色选择器的抖动算法
     * @throws DithererNotAvailableException 预留异常，当当前颜色选择器无可用抖动算法时抛出
     */
    @NotNull FloydSteinbergDitherer getDitherer() throws DithererNotAvailableException;
}
