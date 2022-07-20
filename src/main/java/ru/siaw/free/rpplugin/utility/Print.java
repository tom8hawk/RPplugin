package ru.siaw.free.rpplugin.utility;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Print {
    private static final Logger logger = Bukkit.getLogger();

    public static void toConsole(String msg) {
        logger.log(Level.WARNING, () -> msg);
    }

    public static void toPlayer(Player p, String msg) {
        p.sendMessage(msg);
    }

    public static void toSender(CommandSender sender, String msg) {
        sender.sendMessage(msg);
    }

    public static void toPlayers(String msg) {
        Bukkit.getOnlinePlayers().forEach(p -> toPlayer(p, msg));
    }
}
