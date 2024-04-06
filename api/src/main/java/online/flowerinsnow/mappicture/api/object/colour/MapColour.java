package online.flowerinsnow.mappicture.api.object.colour;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * <p>代表一个 Minecraft 支持的地图像素颜色</p>
 */
public interface MapColour {
    /**
     * <p>获取颜色在 Minecraft 中的基础 ID</p>
     *
     * @return 颜色ID
     */
    byte getID();

    /**
     * <p>获取颜色在指定阴影时的 ID</p>
     *
     * @return 颜色在指定阴影时的 ID
     */
    byte getIDOfBrightness(@NotNull MapColourBrightness brightness);

    /**
     * <p>获取颜色基础 RGB 颜色</p>
     *
     * @return 颜色基础 RGB 颜色
     */
    @NotNull Color getBaseColor();

    /**
     * <p>获取颜色在指定阴影时的 RGB 颜色</p>
     *
     * @return 颜色在指定阴影时的 RGB 颜色
     */
    @NotNull Color getColorOfBrightness(@NotNull MapColourBrightness brightness);
}
