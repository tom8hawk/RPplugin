package ru.siaw.free.rpplugin.function;

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
import ru.siaw.free.rpplugin.Config;
import ru.siaw.free.rpplugin.RPplugin;

public class HideTags implements Listener {
    private static Scoreboard board;
    private static Team team;

    private HideTags() {
    }

    public static void init() {
        if (Config.getBoolean("HIDE-TAGS.enabled")) {
            board = Bukkit.getScoreboardManager().getNewScoreboard();
            team = board.registerNewTeam("HideTags");
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
            team.setCanSeeFriendlyInvisibles(false);

            Bukkit.getOnlinePlayers().forEach(HideTags::hideName);
            Bukkit.getPluginManager().registerEvents(new HideTags(), RPplugin.inst);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked() instanceof Player) {
            String playerName = ((Player) e.getRightClicked()).getDisplayName();
            BaseComponent[] text = TextComponent.fromLegacyText(
                    Config.getMessage("HIDE-TAGS.actionbar").replace("%name", playerName));

            e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, text);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        hideName(e.getPlayer());
    }

    private static void hideName(Player p) {
        team.addEntry(p.getName());
        p.setScoreboard(board);
    }
}
