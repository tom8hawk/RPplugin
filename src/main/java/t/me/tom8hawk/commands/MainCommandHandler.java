package t.me.tom8hawk.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import t.me.tom8hawk.RPplugin;
import t.me.tom8hawk.config.ConfigValues;

public final class MainCommandHandler implements CommandExecutor {

    private final ConfigValues configValues;

    public MainCommandHandler(RPplugin plugin) {
        this.configValues = plugin.getConfigValues();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        this.configValues.setup();
        sender.sendMessage("Настройки перезагружены.");
        return true;
    }

    private boolean testPermission(CommandSender sender) {
        if (!this.configValues.getNoPermissionMessage().isEmpty() && !sender.hasPermission("rppl.reload")) {
            sender.sendMessage(this.configValues.getNoPermissionMessage());
            return false;
        }

        return true;
    }
}