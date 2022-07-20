package ru.siaw.free.rpplugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.siaw.free.rpplugin.DataInitializer;
import ru.siaw.free.rpplugin.function.Me;
import ru.siaw.free.rpplugin.function.Try;
import ru.siaw.free.rpplugin.utility.Print;

import static ru.siaw.free.rpplugin.RPplugin.executor;

public class Commands implements CommandExecutor {
    private static boolean usePerms;
    private static String noPermsMsg, unknown;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        executor.execute(() -> {
            String lowerCase = label.toLowerCase();

            if (label.equals("rppl") && hasPerm(sender, "rppl.reload")) {
                if (args[0].equals("reload")) {
                    DataInitializer.checkFiles();
                    Print.toConsole("Все настройки перезагружены!");
                } else {
                    Print.toSender(sender, unknown);
                }
            } else if (sender instanceof Player) {
                Player player = (Player) sender;

                switch (lowerCase) {
                    case "try":
                        if (hasPerm(sender, "try.use"))
                            Try.send(player, String.join(" ", args));

                        break;
                    case "me":
                        if (hasPerm(sender, "me.use"))
                            Me.send(player, String.join(" ", args));

                        break;
                    default:
                        Print.toSender(sender, unknown);
                }
            }
        });

        return true;
    }

    private static boolean hasPerm(CommandSender sender, String perm) {
        boolean hasPerm = !usePerms || sender.hasPermission(perm);

        if (!hasPerm)
            Print.toSender(sender, noPermsMsg);

        return hasPerm;
    }

    public static void setUsePerms(boolean value) {
        usePerms = value;
    }

    public static void setNoPerms(String value) {
        noPermsMsg = value;
    }

    public static void setUnknown(String value) {
        unknown = value;
    }
}
