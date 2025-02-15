package t.me.tom8hawk.function;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BukkitConverters;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import t.me.tom8hawk.Config;
import t.me.tom8hawk.RPplugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OnlineBook implements Listener {

    private static final Cache<String, Boolean> onlinePlayersCache = CacheBuilder.newBuilder()
            .maximumSize(30)
            .expireAfterWrite(2, TimeUnit.HOURS)
            .build();

    private OnlineBook() {
    }

    public static void init() {
        if (Config.getBoolean("ONLINE-BOOK.enabled")) {
            ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
            protocolManager.addPacketListener(new PacketAdapter(RPplugin.inst,
                    PacketType.Play.Server.SET_SLOT, PacketType.Play.Server.WINDOW_ITEMS) {

                @Override
                public void onPacketSending(PacketEvent event) {
                    PacketContainer packet = event.getPacket();

                    if (packet.getType() == PacketType.Play.Server.SET_SLOT) {
                        ItemStack item = packet.getItemModifier().read(0).clone();
                        packet.getItemModifier().write(0, handle(item));
                    } else if (packet.getType() == PacketType.Play.Server.WINDOW_ITEMS) {
                        List<ItemStack> previousItems = packet.getLists(BukkitConverters.getItemStackConverter()).read(0);
                        List<ItemStack> newItems = new ArrayList<>();

                        for (ItemStack item : previousItems) {
                            newItems.add(handle(item));
                        }

                        packet.getLists(BukkitConverters.getItemStackConverter()).write(0, newItems);
                    }

                    event.setPacket(packet);
                }
            });
        }

        Bukkit.getPluginManager().registerEvents(new OnlineBook(), RPplugin.inst);
    }

    private static ItemStack handle(ItemStack item) {
        if (item != null && item.getType() == Material.WRITTEN_BOOK) {
            BookMeta book = (BookMeta) item.getItemMeta();

            if (book != null) {
                String author = book.getAuthor();

                if (author != null) {
                    item = item.clone();

                    boolean online = isPlayerOnline(author);
                    String postfix = Config.getMessage("ONLINE-BOOK." + (online ? "online" : "offline"));
                    book.setAuthor(author + " " + postfix);
                    item.setItemMeta(book);

                    return item;
                }
            }
        }

        return item;
    }

    private static boolean isPlayerOnline(String name) {
        Boolean cachedValue = onlinePlayersCache.getIfPresent(name);

        if (cachedValue != null) {
            return cachedValue;
        }

        boolean online = Bukkit.getOnlinePlayers().stream()
                .anyMatch(player -> player.getName().equals(name));

        onlinePlayersCache.put(name, online);
        return online;
    }

    private static void setPlayerOnlineIfPresent(String playerName, boolean online) {
        if (onlinePlayersCache.getIfPresent(playerName) != null) {
            onlinePlayersCache.put(playerName, online);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        setPlayerOnlineIfPresent(playerName, true);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        String playerName = event.getPlayer().getName();
        setPlayerOnlineIfPresent(playerName, false);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        setPlayerOnlineIfPresent(playerName, false);
    }

}