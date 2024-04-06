package online.flowerinsnow.mappicture.api.object.colour;

import org.jetbrains.annotations.NotNull;

/**
 * <p>选择到的颜色+阴影组合</p>
 */
public interface SelectedMapColour {
    /**
     * <p>获取地图基色</p>
     *
     * @return 地图基色
     */
    @NotNull MapColour getColour();

    /**
     * <p>获取颜色阴影</p>
     *
     * @return 颜色阴影
     */
    @NotNull MapColourBrightness getBrightness();
}
