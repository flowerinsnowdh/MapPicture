package cn.flowerinsnow.mappicture.plugin.config;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;

public class Messages extends ConfigurationRoot {
    public static class Command extends ConfigurationRoot {
        public static class Give extends ConfigurationRoot {
            public static final ConfiguredMessageList<String> SUCCESS = ConfiguredMessageList.asStrings()
                    .defaults("&7[&b!&7]&b给予了 &a%(player)&e %(amount)&b 张地图")
                    .params("player", "amount")
                    .build();
            public static final ConfiguredMessageList<String> ILLEGAL_URL = ConfiguredMessageList.asStrings()
                    .defaults("&7[&c!&7]&c非法URL")
                    .params("url")
                    .build();

            public static final ConfiguredMessageList<String> IMAGEIO_READ_EXCEPTION = ConfiguredMessageList.asStrings()
                    .defaults("&7[&c!&7]&c图片资源读取失败")
                    .params("url")
                    .build();
        }
    }
}
