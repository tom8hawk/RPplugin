package t.me.tom8hawk;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class Commands implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equals("rppl") && hasPermission(sender, "rppl.reload")) {
            if (args[0].equalsIgnoreCase("reload")) {
                Config.load();
                RPplugin.inst.getLogger().info(() -> "Настройки перезагружены.");
            } else {
                sender.sendMessage(Config.getMessage("commands.unknown"));
            }
        } else if (sender instanceof Player) {
            Player player = (Player) sender;
            RpMessage rpMessage = RpMessage.get(label.toUpperCase());

            if (rpMessage != null) {
                rpMessage.handle(player, String.join(" ", args));
            }
        }

        return true;
    }

    private static boolean hasPermission(CommandSender sender, String perm) {
        boolean hasPermission = !Config.getBoolean("commands.check-permission") || sender.hasPermission(perm);

        if (!hasPermission) {
            sender.sendMessage(Config.getMessage("commands.no-permission"));
        }

        return hasPermission;
    }

    private enum RpMessage {
        ME {

            @Override
            String replacePlaceholders(String replaceable, String message, Player player) {
                return replaceable.replace("%player", player.getDisplayName()).replace("%message", message);
            }
        }, TRY {

            @Override
            String replacePlaceholders(String replaceable, String message, Player player) {
                String result = Config.getMessage(name() + "." + (random.nextBoolean() ? "successful" : "unsuccessful"));
                return replaceable.replace("%player", player.getDisplayName())
                        .replace("%message", message).replace("%result", result);
            }
        };

        private static final Random random = new Random();
        private static final Map<String, RpMessage> values = Arrays.stream(values())
                .collect(Collectors.toMap(Enum::name, e -> e));

        public static RpMessage get(String name) {
            return values.get(name);
        }

        public void handle(Player player, String message) {
            if (!message.isBlank()) {
                String replaceable = Config.getMessage(name() + ".message");
                String sent = replacePlaceholders(replaceable, message, player);

                if (Config.getBoolean(name() + ".global")) {
                    Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(sent));
                } else {
                    double distance = Config.getDouble(name() + ".distance");
                    Location location = player.getLocation();
                    Area area = new Area(location, distance);

                    for (Player target : location.getWorld().getPlayers()) {
                        if (area.contains(target.getLocation())) {
                            target.sendMessage(sent);
                        }
                    }
                }
            }
        }

        abstract String replacePlaceholders(String replaceable, String message, Player player);
    }

    private static class Area {
        private final World world;
        private final Vector min;
        private final Vector max;

        private Area(Location center, double distance) {
            world = center.getWorld();

            int x = center.getBlockX();
            int y = center.getBlockY();
            int z = center.getBlockZ();

            double increased = distance + 1;
            min = new Vector(x - distance, y - distance, z - distance);
            max = new Vector(x + increased, y + distance, z + increased);
        }

        public boolean contains(Location other) {
            return Objects.equals(world, other.getWorld()) && other.toVector().isInAABB(min, max);
        }
    }
}