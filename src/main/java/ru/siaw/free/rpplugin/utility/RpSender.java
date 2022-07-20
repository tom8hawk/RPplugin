package ru.siaw.free.rpplugin.utility;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.siaw.free.rpplugin.RPplugin;

import java.util.Random;
import java.util.concurrent.ExecutionException;

public class RpSender {
    private boolean global;
    private String original;
    private String successful, unsuccessful;
    private int radius;

    private static final String PLAYER = "%player";
    private static final String MESSAGE = "%message";
    private static final String LUCK_MSG = "%luckmsg";

    public void use(Player sender, String msg) {
        String toSend = replace(sender.getDisplayName(), msg);

        if (global) {
            Print.toPlayers(toSend);
        } else try {
            Bukkit.getScheduler().callSyncMethod(RPplugin.inst, () -> sender.getNearbyEntities(radius, radius, radius)).get().stream()
                    .filter(p -> p instanceof Player)
                    .forEach(p -> Print.toPlayer((Player) p, toSend));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private String replace(String playerName, String playerMsg) {
        String changed = original;

        if (changed.contains(PLAYER))
            changed = changed.replace(PLAYER, playerName.trim());

        if (changed.contains(MESSAGE))
            changed = changed.replace(MESSAGE, playerMsg.trim());

        if (changed.contains(LUCK_MSG))
            changed = changed.replace(LUCK_MSG, luckMsg());

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