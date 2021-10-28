package ru.siaw.free.rpplugin.utility;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Random;

public class RpSender {
    private boolean global;
    private String original;
    private String successful, unsuccessful;
    private int radius;

    public void use(Player sender, String msg) {
        String toSend = replace(sender.getDisplayName(), msg);
        if (global)
            Print.toPlayers(toSend);
        else
            Bukkit.getOnlinePlayers().stream().filter(p -> p.getWorld().equals(sender.getWorld())).filter(p -> p.getLocation().distance(sender.getLocation()) <= radius).forEach(p -> Print.toPlayer(p, toSend));
    }

    private String replace(String playerName, String playerMsg) {
        String changed = original;

        String word = "%player";
        if (changed.contains(word)) changed = changed.replace(word, playerName.trim());

        word = "%message";
        if (changed.contains(word)) changed = changed.replace(word, playerMsg.trim());

        word = "%luckmsg";
        if (changed.contains(word)) changed = changed.replace(word, luckMsg());

        return changed;
    }

    private String luckMsg() {
        return new Random().nextBoolean() ? successful : unsuccessful;
    }

    public void setSuccessful(String value) {
        successful = value;
    }

    public void setUnsuccessful(String value) {
        unsuccessful = value;
    }

    public void setOriginal(String value) {
        original = value;
    }

    public void setGlobal(Boolean value) {
        global = value;
    }

    public void setRadius(int value) {
        radius = value;
    }
}