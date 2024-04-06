package online.flowerinsnow.mappicture.plugin.util;

import net.minecraft.server.level.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;

public abstract class NMSUtils {
    private NMSUtils() {
    }

    public static WorldServer getOverworld() {
        //noinspection resource
        return ((CraftServer) Bukkit.getServer()).getHandle().b().F();
    }
}
