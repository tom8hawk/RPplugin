package t.me.tom8hawk.function;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BukkitConverters;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import t.me.tom8hawk.RPplugin;
import t.me.tom8hawk.config.ConfigValues;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class OnlineBook implements Listener {

    private final RPplugin plugin;
    private final ConfigValues configValues;
    private final Set<String> online;

    public OnlineBook(final RPplugin plugin) {
        this.plugin = plugin;
        this.configValues = plugin.getConfigValues();
        this.online = new HashSet<>();
    }

    public void init() {
        if (!this.plugin.getConfigValues().isOnlineBookEnabled()) {
            return;
        }

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketAdapter(this.plugin,
                PacketType.Play.Server.SET_SLOT,
                PacketType.Play.Server.WINDOW_ITEMS) {

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

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.onEvent(event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event) {
        this.onEvent(event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.onEvent(event);
    }

    private void onEvent(PlayerEvent event) {
        this.online.add(event.getPlayer().getName());
    }

    private ItemStack handle(ItemStack item) {
        if (item != null && item.getType() == Material.WRITTEN_BOOK) {
            BookMeta book = (BookMeta) item.getItemMeta();

            if (book != null) {
                String author = book.getAuthor();

                if (author != null) {

                    author = author.substring(0, author.indexOf(' ') - 1);
                    item = item.clone();

                    String postfix = this.online.contains(author)
                            ? this.configValues.getOnlineBookOnline()
                            : this.configValues.getOnlineBookOffline();

                    book.setAuthor(author + postfix);
                    item.setItemMeta(book);

                    return item;
                }
            }
        }

        return item;
    }
}