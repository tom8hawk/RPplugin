package t.me.tom8hawk;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import t.me.tom8hawk.commands.MainCommandHandler;
import t.me.tom8hawk.commands.RpCommandHandler;
import t.me.tom8hawk.config.ConfigValues;
import t.me.tom8hawk.function.HideTags;
import t.me.tom8hawk.function.OnlineBook;

import java.util.List;

public final class RPplugin extends JavaPlugin {

    @Getter
    private ConfigValues configValues;

    @Override
    public void onEnable() {
        this.configValues.setup();
        this.setupFunctions();
        this.setupCommands();
    }

    private void setupFunctions() {
        new HideTags(this).init();
        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            new OnlineBook(this).init();
        } else {
            super.getLogger().info(() -> "ProtocolLib не найден!");
        }
    }

    private void setupCommands() {
        final TabCompleter emptyCompleter = (sender, command, alias, args) -> List.of();

        final PluginCommand pluginCommand = super.getCommand("rppl");
        pluginCommand.setExecutor(new MainCommandHandler(this));
        pluginCommand.setTabCompleter(emptyCompleter);

        final PluginCommand rpCommand = super.getCommand("try");
        rpCommand.setExecutor(new RpCommandHandler(this));
        rpCommand.setTabCompleter(emptyCompleter);
    }
}