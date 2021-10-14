package ru.siaw.free.rpplugin.function;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityPortalExitEvent;
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
    private static final List<OfflinePlayer> players = Arrays.asList(Bukkit.getOfflinePlayers());
    private static boolean enabled, reset;
    private static String online, offline;

    private static void update(ItemStack item) {
        if (item != null && item.getType() == Material.WRITTEN_BOOK) { // проверяем подписанная ли это книга
            BookMeta bookMeta = (BookMeta) item.getItemMeta(); // Получаем мету
            if (bookMeta != null && bookMeta.getAuthor() != null) {
                String authorName = extractAuthor(bookMeta).trim(); // достаем имя автора
                if (enabled)
                    bookMeta.setAuthor(authorName + " " + (Bukkit.getPlayer(authorName) != null ? online : offline)); // устанавливаем автора
                else if (reset)
                    bookMeta.setAuthor(authorName); // сбрасываем к обычному значению (просто автор книги)
                item.setItemMeta(bookMeta); // устанавливаем мету
            }
        }
    }

    private static String extractAuthor(BookMeta meta) {
        String author = meta.getAuthor();
        for (OfflinePlayer p : players) {
            String name = p.getName();
            if (author.contains(name)) {
                author = name;
                break;
            }
        }
        return author;
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
        if (e.isSigning() && !e.isCancelled() && !players.contains(p)) players.add(p);
        for (ItemStack item : e.getPlayer().getInventory()) update(item);
    }

    // выход с портала
    @EventHandler
    public void onPortalExit(EntityPortalExitEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            for (ItemStack item : player.getInventory()) update(item);
        }
    }

    // подбор предмета
    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            for (ItemStack item : player.getInventory()) update(item);
        }
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
