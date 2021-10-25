package ru.siaw.free.rpplugin.function;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.Arrays;
import java.util.List;

public class OnlineBook implements Listener {
    private static String online, offline;
    private static boolean enabled, reset;
    private static final List<OfflinePlayer> players = Arrays.asList(Bukkit.getOfflinePlayers());

    private void update(ItemStack item) {
        if (enabled && item != null && item.getType() == Material.WRITTEN_BOOK) { // проверяем подписанная ли это книга
            BookMeta book = (BookMeta) item.getItemMeta(); // получаем мету
            if (book != null) {
                String bookAuthor = book.getAuthor();
                if (bookAuthor != null) {
                    new Thread(new Runnable() {
                        public synchronized void run() {
                            players.forEach(p -> {
                                String name = p.getName();
                                if (bookAuthor.contains(name)) { // находим точный ник автора книги
                                    book.setAuthor(name + (!reset ? " " + (Bukkit.getPlayer(name) != null ? online : offline) : "")); // устанавливаем автора
                                    item.setItemMeta(book); // устанавливаем мету
                                    return;
                                }
                            });
                        }
                    }).start();
                }
            }
        }
    }

    // присоединение к серверу
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        for (ItemStack item : e.getPlayer().getInventory())
            update(item); // проходимся циклом по вещам и обновляем книги
    }

    // любые действия с книгами
    @EventHandler
    public void onEditBook(PlayerEditBookEvent e) {
        Player p = e.getPlayer();
        if (e.isSigning() && !players.contains(p)) players.add(p);
        for (ItemStack item : p.getInventory()) update(item);
    }

    // открытие инвентаря (не инвентаря игрока)
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        for (HumanEntity p : e.getViewers()) {
            for (ItemStack item : p.getInventory()) update(item);
        }
    }

    // клик в инвентаре
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        for (ItemStack item : e.getWhoClicked().getInventory()) update(item);
    }

    // shift + ПКМ
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (!e.getPlayer().isSneaking() && e.getAction() != Action.RIGHT_CLICK_AIR &&
                e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return; // проверяем, сидит ли игрок на шифте, и если "действие" - правый клик

        update(e.getItem());
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
