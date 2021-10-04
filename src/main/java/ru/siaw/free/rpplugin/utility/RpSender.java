package ru.siaw.free.rpplugin.utility;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public abstract class RpSender {
    protected static boolean enabled, global;
    protected static int radius;
    protected static String message;

    protected static void use(Player sender, String msg) {
        if (enabled) {
            String toSend = replace(sender.getDisplayName(), msg).trim();
            if (global)
                Print.toPlayers(toSend);
            else
                for (Entity entity : sender.getWorld().getNearbyEntities(sender.getLocation(), radius, radius, radius))
                    if (entity instanceof Player)
                        Print.toPlayer((Player) entity, toSend);
        }
    }

    private static String replace(String playerName, String playerMsg) {
        StringBuilder newLine = new StringBuilder(message);

        String word = "<player>";
        if (message.contains(word)) {
            int index = newLine.indexOf(word);
            newLine.replace(index, index + 8, playerName);
        }

        word = "<message>";
        if (message.contains(word)) {
            int index = newLine.indexOf(word);
            newLine.replace(index, index + 9, playerMsg);
        }

        return newLine.toString();
    }
}
