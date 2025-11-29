package t.me.tom8hawk.function;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerCommon;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetCursorItem;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetSlot;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowItems;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import t.me.tom8hawk.RPplugin;
import t.me.tom8hawk.config.ConfigValues;

import java.util.HashSet;
import java.util.Set;

public final class OnlineBook implements RpFunction, PacketListener {

    private final RPplugin plugin;
    private final ConfigValues configValues;
    private final Set<String> online;

    private PacketListenerCommon packetListener;

    public OnlineBook(final RPplugin plugin) {
        this.plugin = plugin;
        this.configValues = plugin.getConfigValues();
        this.online = new HashSet<>();
    }

    @Override
    public void init() {
        if (!this.isFunctionEnabled()) {
            return;
        }

        Bukkit.getPluginManager().registerEvents(this, this.plugin);

        this.packetListener = PacketEvents.getAPI().getEventManager()
                .registerListener(this, PacketListenerPriority.HIGHEST);
    }

    @Override
    public void onPacketSend(@NotNull PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.SET_SLOT) {
            WrapperPlayServerSetSlot wrapper = new WrapperPlayServerSetSlot(event);
            setAuthor(wrapper.getItem());
        } else if (event.getPacketType() == PacketType.Play.Server.WINDOW_ITEMS) {
            WrapperPlayServerWindowItems wrapper = new WrapperPlayServerWindowItems(event);
            wrapper.getItems().forEach(this::setAuthor);
            wrapper.getCarriedItem().ifPresent(this::setAuthor);
        } else if (event.getPacketType() == PacketType.Play.Server.SET_CURSOR_ITEM) {
            WrapperPlayServerSetCursorItem wrapper = new WrapperPlayServerSetCursorItem(event);
            setAuthor(wrapper.getStack());
        }
    }

    @Override
    public boolean isFunctionEnabled() {
        return this.plugin.getConfigValues().isOnlineBookEnabled();
    }

    @Override
    public void disable() {
        if (this.packetListener != null) {
            PacketEvents.getAPI().getEventManager().unregisterListener(this.packetListener);
        }

        this.online.clear();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.online.add(event.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.online.remove(event.getPlayer().getName());
    }

    private void setAuthor(ItemStack item) {
        if (item != null && item.getType() == ItemTypes.WRITTEN_BOOK) {
            item.getComponent(ComponentTypes.WRITTEN_BOOK_CONTENT).ifPresent(book -> {
                String author = book.getAuthor();

                String postfix = this.online.contains(author)
                        ? this.configValues.getOnlineBookOnline()
                        : this.configValues.getOnlineBookOffline();

                book.setAuthor(author + postfix);
            });
        }
    }

}