package ru.siaw.free.rpplugin.function;

import org.bukkit.entity.Player;
import ru.siaw.free.rpplugin.utility.RpSender;

public class Me {
    private static String original;
    private static boolean enabled = false;
    private static final RpSender sendUtil = new RpSender();

    public static void send(Player sender, String msg) {
        if (enabled) sendUtil.use(sender, msg, original);
    }

    public static void setMeEnabled(boolean value) { enabled = value; }
    public static void setMeOriginal(String value) { original = value; }

    public static void setMeGlobal(boolean value) { sendUtil.setGlobal(value); }
    public static void setMeRadius(int value) { sendUtil.setRadius(value); }
}
