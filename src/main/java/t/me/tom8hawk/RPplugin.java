package t.me.tom8hawk;

import org.bukkit.plugin.java.JavaPlugin;
import t.me.tom8hawk.function.HideTags;
import t.me.tom8hawk.function.OnlineBook;

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