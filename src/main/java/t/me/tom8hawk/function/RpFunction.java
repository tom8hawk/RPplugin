package t.me.tom8hawk.function;

import org.bukkit.event.Listener;

public abstract class RpFunction implements Listener {

    abstract void init();

    abstract boolean isFunctionEnabled();

    abstract void disable();

}
