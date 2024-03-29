package t.me.tom8hawk;

import org.bukkit.Bukkit;
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
        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            OnlineBook.init();
        } else {
            getLogger().info(() -> "Protocollib не найден!");
        }

        getCommand("rppl").setExecutor(new Commands());
    }
}