package online.flowerinsnow.mappicture.plugin;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.mineconfiguration.bukkit.MineConfiguration;
import online.flowerinsnow.mappicture.plugin.config.Messages;
import online.flowerinsnow.mappicture.plugin.core.MapPictureCore;
import online.flowerinsnow.mappicture.plugin.manager.CommandManager;
import org.apache.logging.log4j.LogManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MapPicturePlugin extends JavaPlugin {
    private static MapPicturePlugin instance;
    private MapPictureCore core;
    private ConfigurationProvider<?> messagesProvider;

    @Override
    public void onLoad() {
        MapPicturePlugin.instance = this;
    }

    @Override
    public void onEnable() {
        this.messagesProvider = MineConfiguration.from(this, "messages.yml");
        this.messagesProvider.initialize(Messages.class);
        try {
            this.messagesProvider.reload();
        } catch (Exception e) {
            LogManager.getLogger("MapPicture").throwing(e);
            setEnabled(false);
            return;
        }

        this.core = new MapPictureCore(this);
        new CommandManager(this);
    }

    public MapPictureCore getCore() {
        return core;
    }

    public static MapPicturePlugin getInstance() {
        return MapPicturePlugin.instance;
    }
}
