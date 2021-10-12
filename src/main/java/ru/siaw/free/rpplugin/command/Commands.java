package ru.siaw.free.rpplugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import ru.siaw.free.rpplugin.function.Me;
import ru.siaw.free.rpplugin.function.Try;
import ru.siaw.free.rpplugin.data.FileManager;
import ru.siaw.free.rpplugin.utility.Print;

public class Commands implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        boolean isConsole = sender instanceof ConsoleCommandSender;
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                switch (label.toLowerCase()) {
                    case "try":
                        if ((sender.hasPermission("try.use") || sender.isOp()))
                            new Try().send(player, args[0]);
                        return false;
                    case "me":
                        if ((sender.hasPermission("me.use") || sender.isOp()))
                            new Me().send(player, args[0]);
                        return false;
                }
            }
        }
        if (label.equals("rppl")) {
            if (args.length == 1) {
                if (args[0].equals("reload") && (sender.hasPermission("rppl.reload") || sender.isOp() || isConsole)) {
                    new FileManager().checkFiles();
                    Print.msgToConsole("The configuration has been reset.");
                    return false;
                }
            }
        }
        return false;
    }
}
