package ru.siaw.free.rpplugin.function;

import org.bukkit.entity.Player;
import ru.siaw.free.rpplugin.utility.RpSender;

public class Me extends RpSender {
    private static boolean meEnabled, meGlobal;
    private static int meRadius;
    private static String meMsg;

    public static void send(Player sender, String msg) {
        enabled = meEnabled;
        global = meGlobal;
        radius = meRadius;
        message = meMsg;

        use(sender, msg);
    }

    public static void setMeEnabled(boolean value) { meEnabled = value; }
    public static void setMeGlobal(boolean value) { meGlobal = value; }
    public static void setMeRadius(int value) { meRadius = value; }
    public static void setMeMsg(String value) { meMsg = value; }
}
