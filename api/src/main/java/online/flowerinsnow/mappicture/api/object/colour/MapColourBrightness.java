package online.flowerinsnow.mappicture.api.object.colour;

/**
 * <p>地图颜色的阴影</p>
 * <p>阴影可以使用阶梯型方法而让每一个基础颜色拥有4种不同的阴影，而让选择器有更多的颜色选择</p>
 * <p>使用地图颜色阴影可以让 1.20.4 的地图有240种不同的颜色</p>
 * <p>详见<a href="https://zh.minecraft.wiki/w/%E5%9C%B0%E5%9B%BE%E7%89%A9%E5%93%81%E6%A0%BC%E5%BC%8F">地图物品格式</a></p>
 */
public enum MapColourBrightness {
    LOW((byte) 0, (short) 180),
    NORMAL((byte) 1, (short) 220),
    HIGH((byte) 2, (short) 255),
    LOWEST((byte) 3, (short) 135);

    /**
     * <p>颜色ID</p>
     */
    public final byte id;
    /**
     * <p>阴影乘数</p>
     * <p>“每个基色都有4个相关联的地图色。该转换将用红、绿和蓝分别乘上一个值再除以255” —— zh.minecraft.wiki</p>
     */
    public final short brightness;

    MapColourBrightness(byte id, short brightness) {
        this.id = id;
        this.brightness = brightness;
    }
}
