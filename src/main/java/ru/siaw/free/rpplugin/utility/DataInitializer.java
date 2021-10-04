package ru.siaw.free.rpplugin.utility;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.siaw.free.rpplugin.RPplugin;
import ru.siaw.free.rpplugin.function.Me;
import ru.siaw.free.rpplugin.function.OnlineBook;
import ru.siaw.free.rpplugin.function.Try;

import java.io.File;
import java.util.Objects;

public class DataInitializer {
    private static YamlConfiguration msg;
    private static YamlConfiguration config;

    public static void initialize() {
        File dataFolder = RPplugin.getInst().getDataFolder();
        config = YamlConfiguration.loadConfiguration(new File(dataFolder, "config.yml"));
        msg = YamlConfiguration.loadConfiguration(new File(dataFolder, "message.yml"));

        Try.setTryEnabled((boolean) getConfigValue("try.enabled"));
        Try.setTryGlobal((boolean) getConfigValue("try.globalChat"));
        Try.setTryRadius((int) getConfigValue("try.radius"));
        Try.setTryMsg(getMsgValue("try.message"));
        Try.setSuccessful(getMsgValue("try.successfulMessage"));
        Try.setUnsuccessful(getMsgValue("try.unsuccessfulMessage"));

        Me.setMeEnabled((boolean) getConfigValue("me.enabled"));
        Me.setMeGlobal((boolean) getConfigValue("me.globalChat"));
        Me.setMeRadius((int) getConfigValue("me.radius"));
        Me.setMeMsg(getMsgValue("me.message"));

        OnlineBook.setBookEnabled((boolean) getConfigValue("onlineBook.enabled"));
        OnlineBook.setBookReplace((boolean) getConfigValue("onlineBook.reset"));
        OnlineBook.setOnline(getMsgValue("onlineBook.online"));
        OnlineBook.setOffline(getMsgValue("onlineBook.offline"));
    }

    public static Object getConfigValue(String path) { return config.get(path); }

    public static String getMsgValue(String path) {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(msg.getString(path)));
    }
}
