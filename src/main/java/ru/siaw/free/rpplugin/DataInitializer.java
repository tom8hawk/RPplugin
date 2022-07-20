package ru.siaw.free.rpplugin;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.siaw.free.rpplugin.command.Commands;
import ru.siaw.free.rpplugin.function.HideTags;
import ru.siaw.free.rpplugin.function.Me;
import ru.siaw.free.rpplugin.function.OnlineBook;
import ru.siaw.free.rpplugin.function.Try;

import java.io.File;
import java.util.Objects;

import static ru.siaw.free.rpplugin.RPplugin.inst;

public class DataInitializer {
    private static final File dataFolder = inst.getDataFolder();

    private static YamlConfiguration msg;
    private static YamlConfiguration config;

    public static void checkFiles() {
        checkFile("message.yml");
        checkFile("config.yml");

        initialize();
    }

    private static void checkFile(String fileName) {
        File file = new File(dataFolder, fileName);

        if (!dataFolder.exists())
            dataFolder.mkdir();

        if (!file.exists())
            inst.saveResource(fileName, false);
    }

    private static void initialize() {
        config = YamlConfiguration.loadConfiguration(new File(dataFolder, "config.yml"));
        msg = YamlConfiguration.loadConfiguration(new File(dataFolder, "message.yml"));

        Commands.setUsePerms((boolean) getConfigValue("perms.check"));
        Commands.setNoPerms(getMsgValue("system.noPerms"));
        Commands.setUnknown(getMsgValue("system.unknownCmd"));

        HideTags.setEnabled((boolean) getConfigValue("hideTags.enabled"));
        HideTags.setMessage(getMsgValue("hideTags.message"));

        Try.setTryGlobal((boolean) getConfigValue("try.globalChat"));
        Try.setTryRadius((int) getConfigValue("try.radius"));
        Try.setTryOriginal(getMsgValue("try.message"));
        Try.setTrySuccessful(getMsgValue("try.successfulMessage"));
        Try.setTryUnsuccessful(getMsgValue("try.unsuccessfulMessage"));

        Me.setMeGlobal((boolean) getConfigValue("me.globalChat"));
        Me.setMeRadius((int) getConfigValue("me.radius"));
        Me.setMeOriginal(getMsgValue("me.message"));

        OnlineBook.setBookEnabled((boolean) getConfigValue("onlineBook.enabled"));
        OnlineBook.setBookReplace((boolean) getConfigValue("onlineBook.reset"));
        OnlineBook.setOnline(getMsgValue("onlineBook.online"));
        OnlineBook.setOffline(getMsgValue("onlineBook.offline"));
    }

    private static Object getConfigValue(String path) {
        return config.get(path);
    }

    private static String getMsgValue(String path) {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(msg.getString(path)));
    }
}
