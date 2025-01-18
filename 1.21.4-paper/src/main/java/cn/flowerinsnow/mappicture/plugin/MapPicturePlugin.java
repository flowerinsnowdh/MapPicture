package cn.flowerinsnow.mappicture.plugin;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.mineconfiguration.bukkit.MineConfiguration;
import cn.flowerinsnow.mappicture.api.MapPictureAPI;
import cn.flowerinsnow.mappicture.plugin.command.CommandMapPicture;
import cn.flowerinsnow.mappicture.plugin.config.Messages;
import cn.flowerinsnow.mappicture.plugin.core.MapPictureCore;
import cn.flowerinsnow.mappicture.plugin.util.LogUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class MapPicturePlugin extends JavaPlugin {
    private static MapPicturePlugin instance;
    private MapPictureCore core;

    @Override
    public void onLoad() {
        MapPicturePlugin.instance = this;
    }

    @Override
    public void onEnable() {
        ConfigurationProvider<?> messagesProvider = MineConfiguration.from(this, "messages.yml");
        messagesProvider.initialize(Messages.class);
        try {
            messagesProvider.reload();
        } catch (Exception e) {
            LogUtil.throwing(this.getLogger(), e);
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.core = new MapPictureCore();
        MapPictureAPI.setInstance(this.core);

        CommandMapPicture.register(this);
    }

    public MapPictureCore getCore() {
        return this.core;
    }

    public static MapPicturePlugin getInstance() {
        return MapPicturePlugin.instance;
    }
}
