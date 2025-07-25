package t.me.tom8hawk;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import t.me.tom8hawk.commands.MainCommandHandler;
import t.me.tom8hawk.commands.NicknameCommandHandler;
import t.me.tom8hawk.commands.RpCommandHandler;
import t.me.tom8hawk.config.ConfigValues;
import t.me.tom8hawk.function.HideTags;
import t.me.tom8hawk.function.OnlineBook;

@Getter
public final class RPplugin extends JavaPlugin {

    private ConfigValues configValues;
    private DatabaseManager databaseManager;

    private HideTags hideTags;
    private OnlineBook onlineBook;

    @Override
    public void onEnable() {
        this.configValues = new ConfigValues(this);
        this.configValues.setup();

        this.databaseManager = new DatabaseManager(this);

        this.setupFunctions();
        this.setupCommands();
    }

    public void setupFunctions() {
        hideTags = new HideTags(this);
        hideTags.init();

        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            onlineBook = new OnlineBook(this);
            onlineBook.init();
        } else {
            super.getLogger().info(() -> "ProtocolLib не найден!");
        }
    }

    public void disableFunctions() {
        hideTags.disable();

        if (onlineBook != null) {
            onlineBook.disable();
        }
    }

    private void setupCommands() {
        new MainCommandHandler(this).init();
        new RpCommandHandler(this).init();
        new NicknameCommandHandler(this).init();
    }

    @Override
    public void onDisable() {
        disableFunctions();
    }
}