package cn.flowerinsnow.mappicture.plugin.command;

import cn.flowerinsnow.mappicture.api.object.MapPicture;
import cn.flowerinsnow.mappicture.plugin.MapPicturePlugin;
import cn.flowerinsnow.mappicture.plugin.config.Messages;
import cn.flowerinsnow.mappicture.plugin.object.colour.MapColourSelector;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.function.Consumer;

public class CommandMapPicture {
    public static void register(@NotNull MapPicturePlugin plugin) {
        Objects.requireNonNull(plugin);
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            Commands commands = event.registrar();
            commands.register(
                    Commands.literal("mappicture")
                            .then(
                                    Commands.literal("give")
                                            .requires(commandSourceStack -> commandSourceStack.getSender().hasPermission("mappicture.give"))
                                            .then(
                                                    Commands.argument("player", EntityArgument.player())
                                                            .then(
                                                                    Commands.argument("width", IntegerArgumentType.integer(1))
                                                                            .then(
                                                                                    Commands.argument("height", IntegerArgumentType.integer(1))
                                                                                            .then(
                                                                                                    Commands.argument("dithering", BoolArgumentType.bool())
                                                                                                            .then(
                                                                                                                    Commands.argument("url", StringArgumentType.string())
                                                                                                                            .executes(context -> {
                                                                                                                                CommandSender sender = context.getSource().getSender();
                                                                                                                                int width = context.getArgument("width", Integer.class);
                                                                                                                                int height = context.getArgument("height", Integer.class);
                                                                                                                                boolean dithering = context.getArgument("dithering", Boolean.class);
                                                                                                                                String paramURL = context.getArgument("url", String.class);
                                                                                                                                Player player = context.getArgument("player", EntitySelector.class).findSinglePlayer((CommandSourceStack) context.getSource()).getBukkitEntity();

                                                                                                                                URL url;
                                                                                                                                try {
                                                                                                                                    url = new URI(paramURL).toURL();
                                                                                                                                } catch (URISyntaxException | MalformedURLException | IllegalArgumentException e) { // URL 不合法
                                                                                                                                    Messages.Command.Give.ILLEGAL_URL.send(sender, paramURL);
                                                                                                                                    return 0;
                                                                                                                                }

                                                                                                                                Server server = plugin.getServer();
                                                                                                                                Runnable asyncTask = () -> {
                                                                                                                                    BufferedImage image;
                                                                                                                                    try {
                                                                                                                                        image = ImageIO.read(url);
                                                                                                                                    } catch (IOException e) { // IO 读取失败
                                                                                                                                        Messages.Command.Give.IMAGEIO_READ_EXCEPTION.send(sender, paramURL);
                                                                                                                                        return;
                                                                                                                                    }

                                                                                                                                    // 缩放图片
                                                                                                                                    Image scaledInstance = image.getScaledInstance(width * 128, height * 128, BufferedImage.SCALE_DEFAULT);
                                                                                                                                    image = new BufferedImage(width * 128, height * 128, BufferedImage.TYPE_INT_ARGB);
                                                                                                                                    Graphics graphics = image.getGraphics();
                                                                                                                                    graphics.drawImage(scaledInstance, 0, 0, null);
                                                                                                                                    graphics.dispose();
                                                                                                                                    // 防止图片被分割后再抖动可能会导致边缘不吻合，提前在这里抖动
                                                                                                                                    MapColourSelector colourSelector = plugin.getCore().getColourSelector();
                                                                                                                                    if (dithering) {
                                                                                                                                        colourSelector.getDitherer().dither(image);
                                                                                                                                    }

                                                                                                                                    final BufferedImage imageCopy = image;

                                                                                                                                    for (int y = 0; y < height; y++) {
                                                                                                                                        for (int x = 0; x < width; x++) {
                                                                                                                                            BufferedImage subImage = imageCopy.getSubimage(x * 128, y * 128, 128, 128);
                                                                                                                                            MapPicture mapPicture = plugin.getCore().createMapPicture();
                                                                                                                                            mapPicture.draw(subImage, false);
                                                                                                                                            Runnable syncTask = () -> {
                                                                                                                                                ItemStack itemStack = mapPicture.createItemStack(1);
                                                                                                                                                if (!player.getInventory().addItem(itemStack).isEmpty()) {
                                                                                                                                                    player.getWorld().dropItem(player.getLocation(), itemStack);
                                                                                                                                                }
                                                                                                                                            };

                                                                                                                                            try {
                                                                                                                                                // Folia
                                                                                                                                                // EntityScheduler scheduler = player.getScheduler();
                                                                                                                                                Method methodGetScheduler = player.getClass().getMethod("getScheduler");
                                                                                                                                                Object scheduler = methodGetScheduler.invoke(player);
                                                                                                                                                // scheduler.execute(plugin, syncTask, null, 0L);
                                                                                                                                                Method methodExecute = scheduler.getClass().getMethod("execute", Plugin.class, Runnable.class, Runnable.class, Long.TYPE);
                                                                                                                                                methodExecute.invoke(scheduler, plugin, syncTask, null, 0L);
                                                                                                                                            } catch (
                                                                                                                                                    NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                                                                                                                                                server.getScheduler().runTask(plugin, syncTask);
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                    Messages.Command.Give.SUCCESS.send(sender, player.getName(), height * width);
                                                                                                                                };
                                                                                                                                try {
                                                                                                                                    // Folia
                                                                                                                                    // AsyncScheduler scheduler = server.getAsyncScheduler();
                                                                                                                                    Method methodGetAsyncScheduler = server.getClass().getMethod("getAsyncScheduler");
                                                                                                                                    Object scheduler = methodGetAsyncScheduler.invoke(server);
                                                                                                                                    // scheduler.runNow(plugin, task -> asyncTask.run());
                                                                                                                                    Method methodRunNow = scheduler.getClass().getMethod("runNow", Plugin.class, Consumer.class);
                                                                                                                                    methodRunNow.invoke(scheduler, plugin, (Consumer<ScheduledTask>) task -> asyncTask.run());
                                                                                                                                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                                                                                                                                    Bukkit.getScheduler().runTaskAsynchronously(plugin, asyncTask);
                                                                                                                                }
                                                                                                                                return Command.SINGLE_SUCCESS;
                                                                                                                            })
                                                                                                            )
                                                                                            )
                                                                            )
                                                            )
                                            )
                            )
                            .build()
            );
        });
    }
}
