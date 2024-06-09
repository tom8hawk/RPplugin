package t.me.tom8hawk;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static t.me.tom8hawk.RPplugin.inst;

public class Config {
    private static final Pattern HEX_PATTERN = Pattern.compile("&#[a-fA-F0-9]{6}");
    private static final File dataFolder = inst.getDataFolder();
    private static YamlConfiguration yaml;

    private Config() {
    }

    public static void load() {
        File file = new File(dataFolder, "config.yml");

        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        if (!file.exists()) {
            inst.saveResource("config.yml", false);
        }

        yaml = YamlConfiguration.loadConfiguration(new File(dataFolder, "config.yml"));
    }

    public static double getDouble(String path) {
        return yaml.getDouble(path);
    }

    public static boolean getBoolean(String path) {
        return yaml.getBoolean(path);
    }

    public static String getMessage(String path) {
        return translateColors(yaml.getString(path, ""));
    }

    public static String translateColors(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color,
                    net.md_5.bungee.api.ChatColor.of(color.replace("&","")) + "");
            matcher = HEX_PATTERN.matcher(message);
        }

        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);
    }
}
