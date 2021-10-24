package ru.siaw.free.rpplugin.function;

import org.bukkit.entity.Player;
import ru.siaw.free.rpplugin.utility.RpSender;

public class Me {
    private static final RpSender sendUtil = new RpSender();
    private static String original;

    public static void send(Player sender, String msg) {
        sendUtil.use(sender, msg, original);
    }

    public static void setMeOriginal(String value) {
        original = value;
    }

    public static void setMeGlobal(boolean value) {
        sendUtil.setGlobal(value);
    }

    public static void setMeRadius(int value) {
        sendUtil.setRadius(value);
    }
}
