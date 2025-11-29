package t.me.tom8hawk.function;

import org.bukkit.event.Listener;

public interface RpFunction extends Listener {

    void init();

    boolean isFunctionEnabled();

    void disable();

}
