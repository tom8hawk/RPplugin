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

        if (original.contains("%player")) {
            int index = newLine.indexOf("%player");
            newLine.replace(index, index + 7, playerName.trim());
        }
        if (original.contains("%message")) {
            int index = newLine.indexOf("%message");
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