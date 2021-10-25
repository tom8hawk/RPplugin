package ru.siaw.free.rpplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.siaw.free.rpplugin.command.Commands;
import ru.siaw.free.rpplugin.data.FileManager;
import ru.siaw.free.rpplugin.function.HideTags;
import ru.siaw.free.rpplugin.function.OnlineBook;

public class RPplugin extends JavaPlugin {
    private static RPplugin inst;
    public RPplugin() { inst = this; }
    public static RPplugin getInst() { return inst; }

    @Override
    public void onEnable() {
        new FileManager().checkFiles();
        HideTags.initialize();

        getCommand("rppl").setExecutor(new Commands());
        getCommand("try").setExecutor(new Commands());
        getCommand("me").setExecutor(new Commands());

        Bukkit.getPluginManager().registerEvents(new HideTags(), this);
        Bukkit.getPluginManager().registerEvents(new OnlineBook(), this);
    }
}