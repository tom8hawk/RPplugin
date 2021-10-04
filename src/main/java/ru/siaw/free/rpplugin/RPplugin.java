package ru.siaw.free.rpplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.siaw.free.rpplugin.function.OnlineBook;
import ru.siaw.free.rpplugin.listener.Commands;
import ru.siaw.free.rpplugin.utility.FileManager;

public class RPplugin extends JavaPlugin {
    private static RPplugin inst;
    public RPplugin() { inst = this; }
    public static RPplugin getInst() { return inst; }

    @Override
    public void onEnable() {
        FileManager.checkFiles();
        getCommand("rppl").setExecutor(new Commands());
        getCommand("try").setExecutor(new Commands());
        getCommand("me").setExecutor(new Commands());

        Bukkit.getPluginManager().registerEvents(new OnlineBook(), this);
    }
}
