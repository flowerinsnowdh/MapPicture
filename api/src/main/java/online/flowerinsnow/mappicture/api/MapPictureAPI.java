package online.flowerinsnow.mappicture.api;

/**
 * <p>Map Picture API入口</p>
 */
public abstract class MapPictureAPI {
    private MapPictureAPI() {
    }

    private static MapPicture instance;

    public static MapPicture getInstance() {
        return MapPictureAPI.instance;
    }

    public static void setInstance(MapPicture instance) {
        MapPictureAPI.instance = instance;
    }
}
