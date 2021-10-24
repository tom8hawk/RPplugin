package ru.siaw.free.rpplugin.function;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class HideTags implements Listener {
    private static boolean enabled;
    private static String message;

    private static Scoreboard board;
    private static Team team;

    public static void initialize() {
        if (!enabled) return;

        board = Bukkit.getScoreboardManager().getNewScoreboard();
        board.registerNewTeam("HideTags");

        team = board.getTeam("HideTags");

        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        team.setCanSeeFriendlyInvisibles(false);

        if (!Bukkit.getOnlinePlayers().isEmpty())
            Bukkit.getOnlinePlayers().forEach(HideTags::hideName);
    }

    private static void hideName(Player p) {
        team.addEntry(p.getName());
        p.setScoreboard(board);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        hideName(e.getPlayer());
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e) {
        if (!(e.getRightClicked() instanceof Player)) return;

        e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(message.replace("%name", ((Player) e.getRightClicked()).getDisplayName())));
    }

    public static void setEnabled(boolean value) {
        enabled = value;
    }

    public static void setMessage(String value) {
        message = value;
    }
}
