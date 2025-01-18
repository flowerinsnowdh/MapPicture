package cn.flowerinsnow.mappicture.plugin.util;

import net.minecraft.server.level.ServerLevel;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;

public abstract class InternalCodeUtil {
    private InternalCodeUtil() {
    }

    public static ServerLevel getOverworld() {
        return ((CraftServer) Bukkit.getServer()).getHandle().getServer().overworld();
    }
}
