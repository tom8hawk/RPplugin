package ru.siaw.free.rpplugin.data;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.siaw.free.rpplugin.RPplugin;
import ru.siaw.free.rpplugin.function.Me;
import ru.siaw.free.rpplugin.function.OnlineBook;
import ru.siaw.free.rpplugin.function.Try;

import java.io.File;
import java.util.Objects;

public class DataInitializer {
    private YamlConfiguration msg;
    private YamlConfiguration config;

    private String tryMsg, trySuccess, tryUnsuccess;
    private boolean tryEnabled, tryGlobal;
    private int tryRadius;

    private String meMsg;
    private boolean meEnabled, meGlobal;
    private int meRadius;

    public void initialize() {
        File dataFolder = RPplugin.getInst().getDataFolder();
        config = YamlConfiguration.loadConfiguration(new File(dataFolder, "config.yml"));
        msg = YamlConfiguration.loadConfiguration(new File(dataFolder, "message.yml"));

        tryEnabled = (boolean) getConfigValue("try.enabled");
        tryGlobal = (boolean) getConfigValue("try.globalChat");
        tryRadius = (int) getConfigValue("try.radius");
        tryMsg = getMsgValue("try.message");
        trySuccess = getMsgValue("try.successfulMessage");
        tryUnsuccess =  getMsgValue("try.unsuccessfulMessage");

        Me.setMeEnabled((boolean) getConfigValue("me.enabled"));
        Me.setMeGlobal((boolean) getConfigValue("me.globalChat"));
        Me.setMeRadius((int) getConfigValue("me.radius"));
        Me.setMeMsg(getMsgValue("me.message"));

        OnlineBook.setBookEnabled((boolean) getConfigValue("onlineBook.enabled"));
        OnlineBook.setBookReplace((boolean) getConfigValue("onlineBook.reset"));
        OnlineBook.setOnline(getMsgValue("onlineBook.online"));
        OnlineBook.setOffline(getMsgValue("onlineBook.offline"));
    }

    private Object getConfigValue(String path) { return config.get(path); }

    private String getMsgValue(String path) {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(msg.getString(path)));
    }
}
