package t.me.tom8hawk.utils;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@UtilityClass
public final class PlaceholdersUtil {

    private final boolean PLACEHOLDER_API_ENABLED =
            Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");

    public static String setPlaceholders(Player player, String origin) {
        if (PLACEHOLDER_API_ENABLED) {
            return PlaceholderAPI.setPlaceholders(player, origin);
        }

        return origin;
    }

}