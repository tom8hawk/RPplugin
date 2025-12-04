package t.me.tom8hawk.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import t.me.tom8hawk.RPplugin;
import t.me.tom8hawk.utils.Colorizer;

import java.io.File;

@RequiredArgsConstructor @Getter
public final class ConfigValues {

    @Getter(AccessLevel.NONE)
    private final RPplugin plugin;

    private boolean tryPermission;
    private int tryDistance;
    private Component trySuccess;
    private Component tryFailed;

    private boolean mePermission;
    private int meDistance;
    private Component meFormat;

    private boolean onlineBookEnabled;
    private String onlineBookOnline;
    private String onlineBookOffline;

    private boolean hideTagsEnabled;
    private boolean defaultHidden;
    private String hideTagsActionbar;

    private Component hiddenTagMessage;
    private Component shownTagMessage;

    private Component noPermissionMessage;
    private Component onlyPlayersMessage;
    private Component unknownCommandMessage;

    public void setup() {
        final FileConfiguration config = this.getConfig();
        this.setupTry(config);
        this.setupMe(config);
        this.setupOnlineBook(config);
        this.setupHideTags(config);
        this.setupMessages(config);
    }

    private void setupTry(final FileConfiguration config) {
        final ConfigurationSection section = config.getConfigurationSection("TRY");
        this.tryPermission = section.getBoolean("permission");
        this.tryDistance = section.getInt("distance");
        this.trySuccess = this.getColorized(section, "success");
        this.tryFailed = this.getColorized(section, "failed");
    }

    private void setupMe(final FileConfiguration config) {
        final ConfigurationSection section = config.getConfigurationSection("ME");
        this.mePermission = section.getBoolean("permission");
        this.meDistance = section.getInt("distance");
        this.meFormat = this.getColorized(section, "format");
    }

    private void setupOnlineBook(final FileConfiguration config) {
        final ConfigurationSection section = config.getConfigurationSection("ONLINE-BOOK");
        this.onlineBookEnabled = section.getBoolean("enabled");
        this.onlineBookOnline = this.onlineBookEnabled ? this.getColorizedString(section, "online") : null;
        this.onlineBookOffline = this.onlineBookEnabled ? this.getColorizedString(section, "offline") : null;
    }

    private void setupHideTags(final FileConfiguration config) {
        final ConfigurationSection section = config.getConfigurationSection("HIDE-TAGS");
        this.hideTagsEnabled = section.getBoolean("enabled");
        this.defaultHidden = section.getBoolean("default-hidden");
        this.hideTagsActionbar = this.hideTagsEnabled ? section.getString("actionbar") : null;
        this.hiddenTagMessage = this.getColorized(section, "messages.hidden");
        this.shownTagMessage = this.getColorized(section, "messages.shown");
    }

    private void setupMessages(final FileConfiguration config) {
        final ConfigurationSection section = config.getConfigurationSection("messages");
        this.noPermissionMessage = this.getColorized(section, "no-permission");
        this.onlyPlayersMessage = this.getColorized(section, "only-players");
        this.unknownCommandMessage = this.getColorized(section, "unknown");
    }

    private FileConfiguration getConfig() {
        final File file = new File(this.plugin.getDataFolder(), "config.yml");

        if (!file.exists()) {
            this.plugin.saveResource("config.yml", false);
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    private Component getColorized(final ConfigurationSection section, final String path) {
        return Colorizer.parse(section.getString(path));
    }

    private String getColorizedString(final ConfigurationSection section, final String path) {
        return Colorizer.colorize(section.getString(path));
    }
}
