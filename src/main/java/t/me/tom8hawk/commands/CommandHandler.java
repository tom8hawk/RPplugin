package t.me.tom8hawk.commands;

import org.bukkit.command.*;
import t.me.tom8hawk.RPplugin;

import java.util.Collections;
import java.util.List;

public abstract class CommandHandler implements CommandExecutor, TabCompleter {

    private static final List<String> EMPTY_COMPLETIONS = Collections.emptyList();

    protected final RPplugin plugin;

    protected CommandHandler(RPplugin plugin) {
        this.plugin = plugin;
    }

    protected abstract List<String> getHandledCommands();

    public void init() {
        for (final String command : getHandledCommands()) {
            final PluginCommand pluginCommand = plugin.getCommand(
                    command.toLowerCase().replace("/", "")
            );

            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return EMPTY_COMPLETIONS;
    }

}
