package ru.siaw.free.rpplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.siaw.free.rpplugin.command.Commands;
import ru.siaw.free.rpplugin.function.HideTags;
import ru.siaw.free.rpplugin.function.OnlineBook;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RPplugin extends JavaPlugin {
    public static final Executor executor = Executors.newWorkStealingPool();
    public static RPplugin inst;

    public RPplugin() {
        inst = this;
    }

    @Override
    public void onEnable() {
        DataInitializer.checkFiles();
        HideTags.initialize();

        Commands commands = new Commands();

        getCommand("rppl").setExecutor(commands);
        getCommand("try").setExecutor(commands);
        getCommand("me").setExecutor(commands);

        Bukkit.getPluginManager().registerEvents(new HideTags(), this);
        Bukkit.getPluginManager().registerEvents(new OnlineBook(), this);
    }
}