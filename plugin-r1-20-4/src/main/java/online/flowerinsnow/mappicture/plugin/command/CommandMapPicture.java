package online.flowerinsnow.mappicture.plugin.command;

import online.flowerinsnow.mappicture.api.object.FilledMapPicture;
import online.flowerinsnow.mappicture.api.object.colour.MapColourSelector;
import online.flowerinsnow.mappicture.plugin.MapPicturePlugin;
import online.flowerinsnow.mappicture.plugin.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class CommandMapPicture {
    @NotNull private final MapPicturePlugin plugin;
    public CommandMapPicture(@NotNull MapPicturePlugin plugin) {
        this.plugin = plugin;
    }
    
    @Command("mappicture give <player> <width> <height> <dithering> <url>")
    @Permission("mappicture.give")
    @CommandDescription("给予玩家地图像素画")
    public void give(
            CommandSender sender,
            @Argument(value = "width", description = "宽（地图个数）") @Range(from = 1, to = Integer.MAX_VALUE) int width,
            @Argument(value = "height", description = "高（地图个数）") @Range(from = 1, to = Integer.MAX_VALUE) int height,
            @Argument(value = "player", description = "要给予的玩家") Player player,
            @Argument(value = "dithering", description = "是否抖动处理") boolean dithering,
            @Argument(value = "url", description = "图片源") String paramURL
    ) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            // 读取图片数据
            URL url;
            BufferedImage image;
            try {
                url = new URL(paramURL);
            } catch (MalformedURLException e) {
                Messages.Command.Give.ILLEGAL_URL.send(sender, paramURL);
                return;
            }
            try {
                image = ImageIO.read(url);
            } catch (IOException e) {
                Messages.Command.Give.IMAGEIO_READ_EXCEPTION.send(sender, paramURL);
                return;
            }
            if (image == null) {
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
            Bukkit.getScheduler().runTask(CommandMapPicture.this.plugin, () -> {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        BufferedImage subImage = imageCopy.getSubimage(x * 128, y * 128, 128, 128);
                        FilledMapPicture filledMapPicture = plugin.getCore().createFilledMapPicture();
                        filledMapPicture.draw(subImage, colourSelector, null);
                        ItemStack itemStack = filledMapPicture.createItemStack(1);
                        if (!player.getInventory().addItem(itemStack).isEmpty()) {
                            player.getWorld().dropItem(player.getLocation(), itemStack);
                        }
                    }
                }
                Messages.Command.Give.SUCCESS.send(sender, player.getName(), height * width);
            });
        });
    }
}
