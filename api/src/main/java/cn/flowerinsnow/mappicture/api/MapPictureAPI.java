package cn.flowerinsnow.mappicture.api;

/**
 * <p>Map Picture API入口</p>
 */
public abstract class MapPictureAPI {
    private MapPictureAPI() {
    }

    private static IMapPicture instance;

    public static IMapPicture getInstance() {
        return MapPictureAPI.instance;
    }

    public static void setInstance(IMapPicture instance) {
        MapPictureAPI.instance = instance;
    }
}
