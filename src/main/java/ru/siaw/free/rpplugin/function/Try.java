package ru.siaw.free.rpplugin.function;

import org.bukkit.entity.Player;
import ru.siaw.free.rpplugin.utility.RpSender;

import java.util.Random;

public class Try extends RpSender {
    private static boolean tryEnabled, tryGlobal;
    private static int tryRadius;
    private static String successful, unsuccessful, tryMsg;

    public static void send(Player sender, String msg) {
        enabled = tryEnabled;
        global = tryGlobal;
        radius = tryRadius;

        StringBuilder newMsg = new StringBuilder(tryMsg);
        String word = "<luckmsg>";
        if (tryMsg.contains(word)) {
            int index = tryMsg.indexOf(word);
            newMsg.replace(index, index + 9, luckMsg());
        }
        message = newMsg.toString();

        use(sender, msg);
    }

    private static String luckMsg() {
        return new Random().nextInt(2) == 1 ? successful : unsuccessful;
    }

    public static void setTryEnabled(boolean value) { tryEnabled = value; }
    public static void setTryGlobal(boolean value) { tryGlobal = value; }
    public static void setTryRadius(int value) { tryRadius = value; }
    public static void setTryMsg(String value) { tryMsg = value; }
    public static void setSuccessful(String value) { successful = value; }
    public static void setUnsuccessful(String value) { unsuccessful = value; }
}