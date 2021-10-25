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
    private static String noPermsMsg, unknown;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String lowerCase = label.toLowerCase();

        if (label.equals("rppl") && args[0].equals("reload") && (sender instanceof ConsoleCommandSender || checkPerm(sender, "rppl.reload"))) {
            new FileManager().checkFiles();
            Print.msgToConsole("The configuration has been reset.");
        } else if (sender instanceof Player) {
            Player player = (Player) sender;
            switch (lowerCase) {
                case "try":
                    if (checkPerm(sender, "try.use"))
                        Try.send(player, extractMessage(args));
                    break;
                case "me":
                    if (checkPerm(sender, "me.use"))
                        Me.send(player, extractMessage(args));
                    break;
                default:
                    Print.toPlayer(player, unknown);
            }
        }
        return false;
    }

    private boolean checkPerm(CommandSender sender, String perm) {
        boolean hasPerm = !usePerms || sender.hasPermission(perm) || sender.isOp();
        if (!hasPerm && sender instanceof Player) Print.toPlayer((Player) sender, noPermsMsg);

        return hasPerm;
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

    public static void setNoPermsMsg(String value) {
        noPermsMsg = value;
    }

    public static void setUnknown(String value) {
        unknown = value;
    }
}
