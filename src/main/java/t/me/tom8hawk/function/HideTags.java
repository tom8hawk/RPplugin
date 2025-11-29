package t.me.tom8hawk.function;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import t.me.tom8hawk.DatabaseManager;
import t.me.tom8hawk.RPplugin;
import t.me.tom8hawk.config.ConfigValues;
import t.me.tom8hawk.utils.Colorizer;
import t.me.tom8hawk.utils.StringUtils;

public final class HideTags implements RpFunction {

    private static final String TEAM_NAME = "HideTags";

    private final RPplugin plugin;
    private final ConfigValues configValues;
    private final DatabaseManager dbManager;

    public HideTags(RPplugin plugin) {
        this.plugin = plugin;
        this.configValues = plugin.getConfigValues();
        this.dbManager = plugin.getDatabaseManager();
    }

    @Override
    public void init() {
        if (!this.isFunctionEnabled()) {
            return;
        }

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (this.isNicknameHidden(player)) {
                hideName(player);
            }
        });

        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    @Override
    public boolean isFunctionEnabled() {
        return this.configValues.isHideTagsEnabled();
    }

    @Override
    public void disable() {
        Bukkit.getOnlinePlayers().forEach(this::unhideName);
    }

    private Team getOrCreateTeam(Scoreboard scoreboard) {
        Team team = scoreboard.getTeam(TEAM_NAME);
        if (team == null) {
            team = scoreboard.registerNewTeam(TEAM_NAME);
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
            team.setCanSeeFriendlyInvisibles(false);
        }
        return team;
    }

    private void updateNames(Player target, boolean hide) {
        Scoreboard scoreboard = target.getScoreboard();
        Team team = getOrCreateTeam(scoreboard);

        if (hide) {
            team.addEntry(target.getName());
        } else {
            team.removeEntry(target.getName());
        }
    }

    public boolean isNicknameHidden(Player target) {
        return this.configValues.isDefaultHidden() && !dbManager.isNicknameVisible(target.getUniqueId());
    }

    public void hideName(final Player target) {
        if (!this.isFunctionEnabled()) {
            return;
        }
        updateNames(target, true);
    }

    public void unhideName(final Player target) {
        if (!this.isFunctionEnabled()) {
            return;
        }
        updateNames(target, false);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(final PlayerInteractAtEntityEvent event) {
        if (!this.isFunctionEnabled()) {
            return;
        }

        if (event.getRightClicked() instanceof Player) {
            final String playerName = ((Player) event.getRightClicked()).getDisplayName();

            final Component message = Colorizer.parse(
                    StringUtils.fastReplace(this.configValues.getHideTagsActionbar(), "%name", playerName));

            event.getPlayer().sendActionBar(message);
        }
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        if (!this.isFunctionEnabled()) {
            return;
        }

        Player joined = event.getPlayer();

        if (this.isNicknameHidden(joined)) {
            hideName(joined);
        }
    }
}