package t.me.tom8hawk.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import t.me.tom8hawk.RPplugin;
import t.me.tom8hawk.config.ConfigValues;

import java.util.Collections;
import java.util.List;

public final class MainCommandHandler extends CommandHandler {

    private final ConfigValues configValues;

    public MainCommandHandler(RPplugin plugin) {
        super(plugin);
        this.configValues = plugin.getConfigValues();
    }

    @Override
    protected List<String> getHandledCommands() {
        return Collections.singletonList("rppl");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("rppl.reload")) {
            return true;
        }

        this.plugin.disableFunctions();

        this.configValues.setup();
        this.plugin.setupFunctions();

        sender.sendMessage("Плагин перезагружен!");
        return true;
    }

}