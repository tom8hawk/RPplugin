package t.me.tom8hawk.function;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import t.me.tom8hawk.RPplugin;
import t.me.tom8hawk.config.ConfigValues;
import t.me.tom8hawk.utils.StringUtils;

public final class HideTags implements Listener {

    private static Scoreboard board;
    private static Team team;

    private final RPplugin plugin;
    private final ConfigValues configValues;

    public HideTags(RPplugin plugin) {
        this.plugin = plugin;
        this.configValues = plugin.getConfigValues();
    }

    public void init() {
        if (!this.configValues.isHideTagsEnabled()) {
            return;
        }

        board = Bukkit.getScoreboardManager().getNewScoreboard();
        team = board.registerNewTeam("HideTags");
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        team.setCanSeeFriendlyInvisibles(false);

        Bukkit.getOnlinePlayers().forEach(this::hideName);
        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(final PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof Player) {
            final String playerName = ((Player) event.getRightClicked()).getDisplayName();
            final BaseComponent[] text = TextComponent.fromLegacyText(
                    StringUtils.fastReplace(this.configValues.getHideTagsActionbar(), "%name", playerName)
            );

            event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, text);
        }
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        this.hideName(event.getPlayer());
    }

    private void hideName(Player p) {
        team.addEntry(p.getName());
        p.setScoreboard(board);
    }
}
