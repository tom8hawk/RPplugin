package t.me.tom8hawk.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import t.me.tom8hawk.DatabaseManager;
import t.me.tom8hawk.RPplugin;
import t.me.tom8hawk.config.ConfigValues;
import t.me.tom8hawk.function.HideTags;

import java.util.Collections;
import java.util.List;

public final class NicknameCommandHandler extends CommandHandler {

    private static final List<String> COMPLETIONS = List.of("hide", "show");

    private final ConfigValues configValues;

    private final HideTags hideTags;

    private final DatabaseManager databaseManager;

    public NicknameCommandHandler(RPplugin plugin) {
        super(plugin);
        this.configValues = plugin.getConfigValues();
        this.hideTags = plugin.getHideTags();
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

        final Player playerSender = (Player) sender;

        if (args.length != 0) {
            if (args[0].equalsIgnoreCase("hide")) {
                hideTags.hideName(playerSender);
                databaseManager.removeVisibleNickname(playerSender.getUniqueId());

                this.plugin.getHideTags().hideName(playerSender);
                sender.sendMessage(this.configValues.getHiddenTagMessage());
            } else if (args[0].equalsIgnoreCase("show")) {
                hideTags.unhideName(playerSender);
                databaseManager.addVisibleNickname(playerSender.getUniqueId());

                this.plugin.getHideTags().unhideName(playerSender);
                sender.sendMessage(this.configValues.getShownTagMessage());
            }

            return true;
        }

        sender.sendMessage("Используйте /nickname hide/show");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return COMPLETIONS;
    }
}