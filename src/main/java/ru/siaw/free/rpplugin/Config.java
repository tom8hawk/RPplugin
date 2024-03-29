package ru.siaw.free.rpplugin;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Objects;

import static ru.siaw.free.rpplugin.RPplugin.inst;

public class Config {
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
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(yaml.getString(path)));
    }
}
