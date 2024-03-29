package ru.siaw.free.rpplugin;

import org.bukkit.plugin.java.JavaPlugin;
import ru.siaw.free.rpplugin.function.HideTags;
import ru.siaw.free.rpplugin.function.OnlineBook;

public class RPplugin extends JavaPlugin {
    public static RPplugin inst;

    public RPplugin() {
        inst = this;
    }

    @Override
    public void onEnable() {
        Config.load();

        HideTags.init();
        OnlineBook.init();

        getCommand("rppl").setExecutor(new Commands());
    }
}