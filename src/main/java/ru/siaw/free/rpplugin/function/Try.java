package ru.siaw.free.rpplugin.function;

import org.bukkit.entity.Player;
import ru.siaw.free.rpplugin.utility.RpSender;

public class Try {
    private static final RpSender sendUtil = new RpSender();

    public static void send(Player sender, String msg) {
        sendUtil.use(sender, msg);
    }

    public static void setTrySuccessful(String value) {
        sendUtil.setSuccessful(value);
    }

    public static void setTryUnsuccessful(String value) {
        sendUtil.setUnsuccessful(value);
    }

    public static void setTryOriginal(String value) {
        sendUtil.setOriginal(value);
    }

    public static void setTryGlobal(boolean value) {
        sendUtil.setGlobal(value);
    }

    public static void setTryRadius(int value) {
        sendUtil.setRadius(value);
    }
}
