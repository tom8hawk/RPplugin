package ru.siaw.free.rpplugin.utility;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class RpSender {
    private boolean global = false;
    private int radius;

    public void use(Player sender, String msg, String original) {
        String toSend = replace(sender.getDisplayName(), msg, original);
        if (global)
            Print.toPlayers(toSend);
        else
            for (Entity entity : sender.getWorld().getNearbyEntities(sender.getLocation(), radius, radius, radius))
                if (entity instanceof Player)
                    Print.toPlayer((Player) entity, toSend);
    }

    private String replace(String playerName, String playerMsg, String original) {
        StringBuilder newLine = new StringBuilder(original);

        String word = "%player";
        if (original.contains(word)) {
            int index = newLine.indexOf(word);
            newLine.replace(index, index + 7, playerName.trim());
        }

        word = "%message";
        if (original.contains(word)) {
            int index = newLine.indexOf(word);
            newLine.replace(index, index + 8, playerMsg.trim());
        }

        return newLine.toString();
    }

    public void setGlobal(Boolean value) {
        global = value;
    }

    public void setRadius(int value) {
        radius = value;
    }
}