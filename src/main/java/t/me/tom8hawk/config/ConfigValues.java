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
        final ConfigurationSection trySection = config.getConfigurationSection("TRY");
        this.tryDistance = trySection.getInt("distance");
        this.trySuccess = this.getColorized(trySection, "success");
        this.tryFailed = this.getColorized(trySection, "failed");
    }

    private void setupMe(final FileConfiguration config) {
        final ConfigurationSection meSection = config.getConfigurationSection("ME");
        this.meDistance = meSection.getInt("distance");
        this.meFormat = this.getColorized(meSection, "format");
    }

    private void setupOnlineBook(final FileConfiguration config) {
        final ConfigurationSection onlineBookSection = config.getConfigurationSection("ONLINE-BOOK");
        this.onlineBookEnabled = onlineBookSection.getBoolean("enabled");
        this.onlineBookOnline = this.onlineBookEnabled ? this.getColorizedString(onlineBookSection, "online") : null;
        this.onlineBookOffline = this.onlineBookEnabled ? this.getColorizedString(onlineBookSection, "offline") : null;
    }

    private void setupHideTags(final FileConfiguration config) {
        final ConfigurationSection onlineBookSection = config.getConfigurationSection("HIDE-TAGS");
        this.hideTagsEnabled = onlineBookSection.getBoolean("enabled");
        this.hideTagsActionbar = this.hideTagsEnabled ? onlineBookSection.getString("actionbar") : null;
        this.hiddenTagMessage = this.getColorized(onlineBookSection, "messages.hidden");
        this.shownTagMessage = this.getColorized(onlineBookSection, "messages.shown");
    }

    private void setupMessages(final FileConfiguration config) {
        final ConfigurationSection messagesSection = config.getConfigurationSection("messages");
        this.noPermissionMessage = this.getColorized(messagesSection, "no-permission");
        this.onlyPlayersMessage = this.getColorized(messagesSection, "only-players");
        this.unknownCommandMessage = this.getColorized(messagesSection, "unknown");
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
