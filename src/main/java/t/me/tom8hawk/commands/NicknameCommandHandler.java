package t.me.tom8hawk.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import t.me.tom8hawk.DatabaseManager;
import t.me.tom8hawk.RPplugin;
import t.me.tom8hawk.config.ConfigValues;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class NicknameCommandHandler extends CommandHandler {

    private static final String SHOW_PERMISSION = "rppl.nick.show";

    private static final String HIDE_PERMISSION = "rppl.nick.hide";

    private final ConfigValues configValues;

    private final DatabaseManager databaseManager;

    public NicknameCommandHandler(RPplugin plugin) {
        super(plugin);
        this.configValues = plugin.getConfigValues();
        this.databaseManager = plugin.getDatabaseManager();
    }

    @Override
    protected List<String> getHandledCommands() {
        return Collections.singletonList("nickname");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(this.configValues.getOnlyPlayersMessage());
            return true;
        }

        if (!this.plugin.getHideTags().isFunctionEnabled()) {
            return true;
        }

        final Player playerSender = (Player) sender;

        if (args.length != 0) {
            if (args[0].equalsIgnoreCase("show")) {

                if (!sender.hasPermission(SHOW_PERMISSION)) {
                    sender.sendMessage(this.configValues.getNoPermissionMessage());
                    return true;
                }

                plugin.getHideTags().unhideName(playerSender);
                databaseManager.addVisibleNickname(playerSender.getUniqueId());

                sender.sendMessage(this.configValues.getShownTagMessage());
            } else if (args[0].equalsIgnoreCase("hide")) {

                if (!sender.hasPermission(HIDE_PERMISSION)) {
                    sender.sendMessage(this.configValues.getNoPermissionMessage());
                    return true;
                }

                plugin.getHideTags().hideName(playerSender, true);
                databaseManager.removeVisibleNickname(playerSender.getUniqueId());

                sender.sendMessage(this.configValues.getHiddenTagMessage());
            }

            return true;
        }

        sender.sendMessage("Используйте /nickname hide/show");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (sender.hasPermission(SHOW_PERMISSION)) {
            completions.add("show");
        }

        if (sender.hasPermission(HIDE_PERMISSION)) {
            completions.add("hide");
        }

        return completions;
    }
}