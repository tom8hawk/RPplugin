package t.me.tom8hawk.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import t.me.tom8hawk.RPplugin;
import t.me.tom8hawk.config.ConfigValues;
import t.me.tom8hawk.utils.StringUtils;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public final class RpCommandHandler implements CommandExecutor {

    private final ConfigValues configValues;
    private final ThreadLocalRandom random;

    public RpCommandHandler(RPplugin plugin) {
        this.configValues = plugin.getConfigValues();
        this.random = ThreadLocalRandom.current();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Только для игроков!");
            return true;
        }

        final Player playerSender = (Player) sender;

        final int distance;
        String message;

        if (label.equalsIgnoreCase("try")) {
            distance = this.configValues.getTryDistance();
            message = this.random.nextBoolean()
                    ? this.configValues.getTrySuccess()
                    : this.configValues.getTryFailed();
        } else {
            distance = this.configValues.getMeDistance();
            message = this.configValues.getMeMessage();
        }

        message = StringUtils.fastReplace(message, "%player", playerSender.getDisplayName());
        message = StringUtils.fastReplace(message, "%message", String.join(" ", args));

        if (distance == -1) {
            for (final Player target : Bukkit.getOnlinePlayers()) {
                target.sendMessage(message);
            }
        } else {
            final Location location = playerSender.getLocation();
            final Area area = new Area(location, distance);

            for (final Player target : location.getWorld().getPlayers()) {
                if (area.contains(target.getLocation())) {
                    target.sendMessage(message);
                }
            }
        }

        return false;
    }

    private static class Area {
        private final World world;
        private final Vector min;
        private final Vector max;

        private Area(Location center, double distance) {
            this.world = center.getWorld();

            int x = center.getBlockX();
            int y = center.getBlockY();
            int z = center.getBlockZ();

            double increased = distance + 1;
            this.min = new Vector(x - distance, y - distance, z - distance);
            this.max = new Vector(x + increased, y + distance, z + increased);
        }

        public boolean contains(Location other) {
            return Objects.equals(this.world, other.getWorld()) && other.toVector().isInAABB(this.min, this.max);
        }
    }
}
