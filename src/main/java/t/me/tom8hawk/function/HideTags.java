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

import java.util.Collections;
import java.util.Set;

public final class HideTags implements RpFunction {

    private static final String TEAM_NAME = "HideTags";

    private final RPplugin plugin;
    private final ConfigValues configValues;
    private final DatabaseManager dbManager;
    private final Team hiddenTeam;

    public HideTags(RPplugin plugin) {
        this.plugin = plugin;
        this.configValues = plugin.getConfigValues();
        this.dbManager = plugin.getDatabaseManager();
        this.hiddenTeam = this.setupTeam();
    }

    @Override
    public void init() {
        if (!this.isFunctionEnabled()) {
            return;
        }

        Bukkit.getOnlinePlayers().forEach(this::unhideName);
        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    @Override
    public boolean isFunctionEnabled() {
        return this.configValues.isHideTagsEnabled();
    }

    @Override
    public void disable() {
        Set<String> hiddenPlayers = Collections.unmodifiableSet(hiddenTeam.getEntries());
        hiddenPlayers.forEach(hiddenTeam::removeEntry);
    }

    private Team setupTeam() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        Team team = scoreboard.getTeam(TEAM_NAME);
        if (team == null) {
            team = scoreboard.registerNewTeam(TEAM_NAME);
        }

        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        return team;
    }

    private void updateNames(final Player target, boolean hide) {
        this.updateNames(target, hide, false);
    }

    private void updateNames(final Player target, boolean hide, boolean force) {
        if (!this.isFunctionEnabled()) {
            return;
        }

        if (hide) {
            if (!force) {
                if (this.hiddenTeam.hasEntry(target.getName())) {
                    return;
                }

                if (this.configValues.isDefaultHidden() && !this.dbManager.isNicknameVisible(target.getUniqueId())) {
                    return;
                }
            }

            this.hiddenTeam.addEntry(target.getName());
        } else {
            this.hiddenTeam.removeEntry(target.getName());
        }
    }

    public void hideName(final Player target, boolean force) {
        this.updateNames(target, true, force);
    }

    public void hideName(final Player target) {
        this.updateNames(target, true);
    }

    public void unhideName(final Player target) {
        this.updateNames(target, false);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(final PlayerInteractAtEntityEvent event) {
        if (!this.isFunctionEnabled()) {
            return;
        }

        if (event.getRightClicked() instanceof Player) {
            final String playerName = ((Player) event.getRightClicked()).getDisplayName();

            final Component message = Colorizer.parse(
                    StringUtils.fastReplace(this.configValues.getHideTagsActionbar(), "%name", playerName)
            );

            event.getPlayer().sendActionBar(message);
        }
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        if (!this.isFunctionEnabled()) {
            return;
        }

        this.hideName(event.getPlayer());
    }
}