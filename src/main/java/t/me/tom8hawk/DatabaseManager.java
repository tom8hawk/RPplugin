package t.me.tom8hawk;

import java.io.File;
import java.sql.*;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseManager {

    private static final String ERROR_LOG_MSG = "An unexpected error occurred.";

    private final Logger logger;
    private final String databaseUrl;

    public DatabaseManager(RPplugin plugin) {
        this.logger = plugin.getLogger();

        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File dbFile = new File(dataFolder, "database.db");
        this.databaseUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();

        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS players (" +
                    "uuid TEXT PRIMARY KEY" +
                    ");";

            stmt.execute(sql);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, ERROR_LOG_MSG, e);
        }
    }

    public void addVisibleNickname(UUID uuid) {
        String sql = "INSERT OR IGNORE INTO players (uuid) VALUES (?);";

        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, uuid.toString());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, ERROR_LOG_MSG, e);
        }
    }

    public void removeVisibleNickname(UUID uuid) {
        String sql = "DELETE FROM players WHERE uuid = ?;";

        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, uuid.toString());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, ERROR_LOG_MSG, e);
        }
    }

    public boolean isNicknameVisible(UUID uuid) {
        String sql = "SELECT 1 FROM players WHERE uuid = ? LIMIT 1;";

        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, uuid.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, ERROR_LOG_MSG, e);
            return false;
        }
    }
}
