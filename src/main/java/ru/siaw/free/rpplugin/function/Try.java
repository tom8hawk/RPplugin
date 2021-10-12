package ru.siaw.free.rpplugin.function;

import org.bukkit.entity.Player;
import ru.siaw.free.rpplugin.utility.RpSender;

import java.util.Random;

public class Try {
    private boolean tryEnabled, tryGlobal;
    private int tryRadius;
    private String successful, unsuccessful, tryMsg;

    public void send(Player sender, String msg) {
        
        StringBuilder newMsg = new StringBuilder(tryMsg);
        String word = "%luckmsg";
        if (tryMsg.contains(word)) {
            int index = tryMsg.indexOf(word);
            newMsg.replace(index, index + 8, luckMsg());
        }
        message = newMsg.toString();

        use(sender, msg);
    }

    private String luckMsg() {
        return new Random().nextInt(2) == 1 ? successful : unsuccessful;
    }

    public void setTryEnabled(boolean value) { tryEnabled = value; }
    public void setTryGlobal(boolean value) { tryGlobal = value; }
    public void setTryRadius(int value) { tryRadius = value; }
    public void setTryMsg(String value) { tryMsg = value; }
    public void setSuccessful(String value) { successful = value; }
    public void setUnsuccessful(String value) { unsuccessful = value; }
}