package ru.siaw.free.rpplugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import ru.siaw.free.rpplugin.data.FileManager;
import ru.siaw.free.rpplugin.function.Me;
import ru.siaw.free.rpplugin.function.Try;
import ru.siaw.free.rpplugin.utility.Print;

public class Commands implements CommandExecutor {
    private static boolean usePerms;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String lowerCase = label.toLowerCase();

            if (lowerCase.equals("try") && (!usePerms || sender.hasPermission("try.use") || sender.isOp()))
                Try.send(player, extractMessage(args));
            else if (lowerCase.equals("me") && (!usePerms || sender.hasPermission("me.use") || sender.isOp()))
                Me.send(player, extractMessage(args));
        }
        boolean isConsole = sender instanceof ConsoleCommandSender;
        if (label.equals("rppl")) {
            if (args[0].equals("reload") && (sender.hasPermission("rppl.reload") || sender.isOp() || isConsole)) {
                new FileManager().checkFiles();
                Print.msgToConsole("The configuration has been reset.");
            }
        }
        return false;
    }

    private String extractMessage(String[] message) {
        StringBuilder changed = new StringBuilder();
        for (String word : message)
            changed.append(word).append(" ");
        return changed.toString();
    }

    public static void setUsePerms(boolean value) {
        usePerms = value;
    }
}
