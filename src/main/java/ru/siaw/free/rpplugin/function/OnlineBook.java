package ru.siaw.free.rpplugin.function;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.Arrays;

import static ru.siaw.free.rpplugin.RPplugin.executor;

public class OnlineBook implements Listener {
    private static String online, offline;
    private static boolean enabled, reset;

    private void update(Inventory inventory) {
        executor.execute(() -> {
            if (enabled) {
                for (int index = 0; index < inventory.getContents().length; index++) {
                    ItemStack item = inventory.getItem(index);

                    if (item != null && item.getType() == Material.WRITTEN_BOOK) {
                        BookMeta book = (BookMeta) item.getItemMeta();

                        if (book != null) {
                            String bookAuthor = book.getAuthor();

                            if (bookAuthor != null) {
                                int finalIndex = index;

                                Arrays.stream(Bukkit.getOfflinePlayers())
                                        .filter(p -> p.getName() != null)
                                        .filter(p -> bookAuthor.contains(p.getName()))
                                        .findFirst().ifPresent(player -> {
                                            if (reset)
                                                book.setAuthor(player.getName());
                                            else
                                                book.setAuthor(player.getName() + " " + (player.isOnline() ? online : offline));

                                            item.setItemMeta(book);
                                            inventory.setItem(finalIndex, item);
                                        });
                            }
                        }
                    }
                }
            }
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        update(e.getPlayer().getInventory());
    }

    @EventHandler
    public void onEditBook(PlayerEditBookEvent e) {
        update(e.getPlayer().getInventory());
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        update(e.getPlayer().getInventory());
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        update(e.getWhoClicked().getInventory());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (!e.getPlayer().isSneaking() && e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        update(e.getPlayer().getInventory());
    }

    public static void setOnline(String value) {
        online = value;
    }

    public static void setOffline(String value) {
        offline = value;
    }

    public static void setBookEnabled(boolean value) {
        enabled = value;
    }

    public static void setBookReplace(boolean value) {
        reset = value;
    }
}
