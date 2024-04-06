# MapPicture
直接以 data 的方法绘制地图像素画，这样实现的好处就是即使插件卸载，也可以继续显示地图像素画

# 游戏地图机制
> [https://zh.minecraft.wiki/w/地图物品格式](https://zh.minecraft.wiki/w/%E5%9C%B0%E5%9B%BE%E7%89%A9%E5%93%81%E6%A0%BC%E5%BC%8F)

游戏的每一张地图数据都会保存在`world/data/map_x.dat`下，最大地图ID会保存在`world/data/idcounts.dat`下

地图数据的`colors`是一个大小为16384的字节数组，代表了128*128内每个点的颜色数据

游戏原版有这些颜色可供选择

<details>

```java
public static final MapColor CLEAR = new MapColor(0, 0);
public static final MapColor PALE_GREEN = new MapColor(1, 8368696);
public static final MapColor PALE_YELLOW = new MapColor(2, 16247203);
public static final MapColor WHITE_GRAY = new MapColor(3, 0xC7C7C7);
public static final MapColor BRIGHT_RED = new MapColor(4, 0xFF0000);
public static final MapColor PALE_PURPLE = new MapColor(5, 0xA0A0FF);
public static final MapColor IRON_GRAY = new MapColor(6, 0xA7A7A7);
public static final MapColor DARK_GREEN = new MapColor(7, 31744);
public static final MapColor WHITE = new MapColor(8, 0xFFFFFF);
public static final MapColor LIGHT_BLUE_GRAY = new MapColor(9, 10791096);
public static final MapColor DIRT_BROWN = new MapColor(10, 9923917);
public static final MapColor STONE_GRAY = new MapColor(11, 0x707070);
public static final MapColor WATER_BLUE = new MapColor(12, 0x4040FF);
public static final MapColor OAK_TAN = new MapColor(13, 9402184);
public static final MapColor OFF_WHITE = new MapColor(14, 0xFFFCF5);
public static final MapColor ORANGE = new MapColor(15, 14188339);
public static final MapColor MAGENTA = new MapColor(16, 11685080);
public static final MapColor LIGHT_BLUE = new MapColor(17, 6724056);
public static final MapColor YELLOW = new MapColor(18, 0xE5E533);
public static final MapColor LIME = new MapColor(19, 8375321);
public static final MapColor PINK = new MapColor(20, 15892389);
public static final MapColor GRAY = new MapColor(21, 0x4C4C4C);
public static final MapColor LIGHT_GRAY = new MapColor(22, 0x999999);
public static final MapColor CYAN = new MapColor(23, 5013401);
public static final MapColor PURPLE = new MapColor(24, 8339378);
public static final MapColor BLUE = new MapColor(25, 3361970);
public static final MapColor BROWN = new MapColor(26, 6704179);
public static final MapColor GREEN = new MapColor(27, 6717235);
public static final MapColor RED = new MapColor(28, 0x993333);
public static final MapColor BLACK = new MapColor(29, 0x191919);
public static final MapColor GOLD = new MapColor(30, 16445005);
public static final MapColor DIAMOND_BLUE = new MapColor(31, 6085589);
public static final MapColor LAPIS_BLUE = new MapColor(32, 4882687);
public static final MapColor EMERALD_GREEN = new MapColor(33, 55610);
public static final MapColor SPRUCE_BROWN = new MapColor(34, 8476209);
public static final MapColor DARK_RED = new MapColor(35, 0x700200);
public static final MapColor TERRACOTTA_WHITE = new MapColor(36, 13742497);
public static final MapColor TERRACOTTA_ORANGE = new MapColor(37, 10441252);
public static final MapColor TERRACOTTA_MAGENTA = new MapColor(38, 9787244);
public static final MapColor TERRACOTTA_LIGHT_BLUE = new MapColor(39, 7367818);
public static final MapColor TERRACOTTA_YELLOW = new MapColor(40, 12223780);
public static final MapColor TERRACOTTA_LIME = new MapColor(41, 6780213);
public static final MapColor TERRACOTTA_PINK = new MapColor(42, 10505550);
public static final MapColor TERRACOTTA_GRAY = new MapColor(43, 0x392923);
public static final MapColor TERRACOTTA_LIGHT_GRAY = new MapColor(44, 8874850);
public static final MapColor TERRACOTTA_CYAN = new MapColor(45, 0x575C5C);
public static final MapColor TERRACOTTA_PURPLE = new MapColor(46, 8014168);
public static final MapColor TERRACOTTA_BLUE = new MapColor(47, 4996700);
public static final MapColor TERRACOTTA_BROWN = new MapColor(48, 4993571);
public static final MapColor TERRACOTTA_GREEN = new MapColor(49, 5001770);
public static final MapColor TERRACOTTA_RED = new MapColor(50, 9321518);
public static final MapColor TERRACOTTA_BLACK = new MapColor(51, 2430480);
public static final MapColor DULL_RED = new MapColor(52, 12398641);
public static final MapColor DULL_PINK = new MapColor(53, 9715553);
public static final MapColor DARK_CRIMSON = new MapColor(54, 6035741);
public static final MapColor TEAL = new MapColor(55, 1474182);
public static final MapColor DARK_AQUA = new MapColor(56, 3837580);
public static final MapColor DARK_DULL_PINK = new MapColor(57, 5647422);
public static final MapColor BRIGHT_TEAL = new MapColor(58, 1356933);
public static final MapColor DEEPSLATE_GRAY = new MapColor(59, 0x646464);
public static final MapColor RAW_IRON_PINK = new MapColor(60, 14200723);
public static final MapColor LICHEN_GREEN = new MapColor(61, 8365974);
```

</details>

其中第一个参数是 id，用于存储在数组中，第二个参数是基础颜色

每一种基础颜色还有4种不同的阴影，可以让基础颜色的 RGB 分别乘以一个数，然后除以 255 向下取整，得到一个新的颜色

<details>

```java
LOW(0, 180),
NORMAL(1, 220),
HIGH(2, 255),
LOWEST(3, 135);
```

</details>

而数组中存储颜色的方式就是

```
(颜色ID << 2) | (阴影ID & 3)
```

使用上面的阶梯型，就可以有241种不同的颜色（60*4加一个完全透明色）

# Floyd–Steinberg dithering
> https://en.wikipedia.org/wiki/Floyd%E2%80%93Steinberg_dithering

由于我们只有240色，必然导致颜色精度丢失，从而导致一些色块颜色不均

因此我们可以使用一种叫作 Floyd–Steinberg dithering 的图像抖动算法，来让图像更有渐变感

本项目的算法实现来自 [Regarrzo/Java-Floyd-Steinberg-Dithering](https://github.com/Regarrzo/Java-Floyd-Steinberg-Dithering)

# API 使用方法
配置仓库

```groovy
maven() {
    url = 'https://repo.flowerinsnow.online/repository/maven-public/'
}
```

配置依赖

```groovy
compileOnly('online.flowerinsnow.mappicture:api:1.0.0')
```

配置前置

```yml
depend:
  - MapPicture
```

绘制地图像素画

```java
MapPicture mp = MapPictureAPI.getInstance(); // 获取 API 对象
FilledMapPicture fmp = mp.createFilledMapPicture(); // 创建一副地图像素画
BufferedImage image = ...; // 从某种方式得到一张图片
fmp.draw(image, mp.getColourSelector(), mp.getColourSelector().getDitherer()); // 绘制此地图像素画
ItemStack itemStack = fmp.createItemStack(64); // 创建64个此地图像素画的物品
```

# 插件使用方法
插件目前只适用于 1.20.4-paper

```
/mappicture give <player> <width> <height> <dithering> <url>
```

此命令需要`mappicture.give`权限

例如，抖动绘制一张2格*2格的本地图片，给予flowerinsnow：

```
/mappicture give flowerinsnow 2 2 true file:///var/picture/cat.png
```

# 使用的依赖
- [flowerinsnowdh/ReflectionUtils](https://github.com/flowerinsnowdh/ReflectionUtils) ([Apache-2.0 license](https://github.com/flowerinsnowdh/ReflectionUtils/blob/master/LICENSE))
- [CarmJos/MineConfiguration](https://github.com/CarmJos/MineConfiguration) ([LGPL-3.0 license](https://github.com/CarmJos/MineConfiguration/blob/master/LICENSE))
- [Incendo/cloud](https://github.com/Incendo/cloud) ([MIT license](https://github.com/Incendo/cloud/blob/master/LICENSE))
- [Regarrzo/Java-Floyd-Steinberg-Dithering](https://github.com/Regarrzo/Java-Floyd-Steinberg-Dithering) ([Unlicense license](https://github.com/Regarrzo/Java-Floyd-Steinberg-Dithering/blob/master/LICENSE))

# 开源许可
本项目 [plugin-r1-20-4/](plugin-r1-20-4/) 目录下的文件采用 [GPL-3.0 license](plugin-r1-20-4/LICENSE) 开源

其余文件采用 [Apache-2.0 license](LICENSE) 开源
