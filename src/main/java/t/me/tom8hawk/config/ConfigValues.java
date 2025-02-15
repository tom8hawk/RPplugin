package t.me.tom8hawk.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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

    private int tryDistance;
    private String trySuccess;
    private String tryFailed;

    private int meDistance;
    private String meMessage;

    private boolean onlineBookEnabled;
    private String onlineBookOnline;
    private String onlineBookOffline;

    private boolean hideTagsEnabled;
    private String hideTagsActionbar;

    private String noPermissionMessage;
    private String unknownCommandMessage;

    public void setup() {
        final FileConfiguration config = this.getConfig();
        this.setupTry(config);
        this.setupMe(config);
        this.setupOnlineBook(config);
        this.setupHideTags(config);
    }

    private void setupTry(final FileConfiguration config) {
        final ConfigurationSection trySection = config.getConfigurationSection("TRY");
        this.tryDistance = trySection.getInt("distance");
        this.trySuccess = this.getColorizer(trySection, "success");
        this.tryFailed = this.getColorizer(trySection, "failed");
    }

    private void setupMe(final FileConfiguration config) {
        final ConfigurationSection meSection = config.getConfigurationSection("ME");
        this.meDistance = meSection.getInt("distance");
        this.meMessage = this.getColorizer(meSection, "message");
    }

    private void setupOnlineBook(final FileConfiguration config) {
        final ConfigurationSection onlineBookSection = config.getConfigurationSection("ONLINE-BOOK");
        this.onlineBookEnabled = onlineBookSection.getBoolean("enabled");
        this.onlineBookOnline = this.onlineBookEnabled ? this.getColorizer(onlineBookSection, "online") : null;
        this.onlineBookOffline = this.onlineBookEnabled ? this.getColorizer(onlineBookSection, "offline") : null;
    }

    private void setupHideTags(final FileConfiguration config) {
        final ConfigurationSection onlineBookSection = config.getConfigurationSection("HIDE-TAGS");
        this.hideTagsEnabled = onlineBookSection.getBoolean("enabled");
        this.hideTagsActionbar = this.hideTagsEnabled ? this.getColorizer(onlineBookSection, "actionbar") : null;
    }

    private void setupMessages(final FileConfiguration config) {
        final ConfigurationSection messagesSection = config.getConfigurationSection("messages");
        this.noPermissionMessage = this.getColorizer(messagesSection, "no-permission");
        this.unknownCommandMessage = this.getColorizer(messagesSection, "unknown");
    }

    private FileConfiguration getConfig() {
        final File file = new File(this.plugin.getDataFolder(), "config.yml");
        if (!file.exists()) {
            this.plugin.saveResource("config.yml", false);
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    private String getColorizer(final ConfigurationSection section, final String path) {
        return Colorizer.colorize(section.getString(path));
    }
}
