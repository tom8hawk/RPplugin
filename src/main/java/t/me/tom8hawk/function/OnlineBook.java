package t.me.tom8hawk.function;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BukkitConverters;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import t.me.tom8hawk.Config;
import t.me.tom8hawk.RPplugin;

import java.util.ArrayList;
import java.util.List;

import static com.comphenix.protocol.PacketType.Play.Server.SET_SLOT;
import static com.comphenix.protocol.PacketType.Play.Server.WINDOW_ITEMS;

public class OnlineBook {

    private OnlineBook() {
    }

    public static void init() {
        if (Config.getBoolean("ONLINE-BOOK.enabled")) {
            if (!Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
                RPplugin.inst.getLogger().info(() -> ChatColor.RED + "ProtocolLib должен быть включен для работы модуля ONLINE-BOOK");
                return;
            }

            ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
            protocolManager.addPacketListener(new PacketAdapter(RPplugin.inst, SET_SLOT, WINDOW_ITEMS) {

                @Override
                public void onPacketSending(PacketEvent event) {
                    PacketContainer packet = event.getPacket();

                    if (packet.getType() == SET_SLOT) {
                        ItemStack item = packet.getItemModifier().read(0).clone();
                        packet.getItemModifier().write(0, handle(item));
                    } else if (packet.getType() == WINDOW_ITEMS) {
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
    }

    private static ItemStack handle(ItemStack item) {
        if (item != null && item.getType() == Material.WRITTEN_BOOK) {
            BookMeta book = (BookMeta) item.getItemMeta();

            if (book != null) {
                String author = book.getAuthor();

                if (author != null) {
                    for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                        String name = player.getName();

                        if (name != null && author.contains(player.getName())) {
                            item = item.clone();

                            String postfix = Config.getMessage("ONLINE-BOOK." + (player.isOnline() ? "online" : "offline"));
                            book.setAuthor(player.getName() + " " + postfix);
                            item.setItemMeta(book);

                            return item;
                        }
                    }
                }
            }
        }

        return item;
    }
}