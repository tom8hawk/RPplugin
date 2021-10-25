package ru.siaw.free.rpplugin.utility;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Print {
    public static void msgToConsole(String msg) {
        System.out.println(ChatColor.GREEN + msg);
    }

    public static void infoToConsole(String msg) {
        System.out.println(ChatColor.YELLOW + msg);
    }

    public static void errToConsole(String msg) {
        System.out.println(ChatColor.RED + msg);
    }

    public static void toPlayer(Player p, String msg) {
        p.sendMessage(msg);
    }

    public static void toSender(CommandSender sender, String msg) {
        if (sender instanceof ConsoleCommandSender)
            errToConsole(msg);
        else if (sender instanceof Player)
            toPlayer((Player) sender, msg);
    }

    public static void toPlayers(String msg) {
        Bukkit.getOnlinePlayers().forEach(p -> toPlayer(p, msg));
    }
}
