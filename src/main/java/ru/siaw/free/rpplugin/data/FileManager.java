package ru.siaw.free.rpplugin.data;

import org.bukkit.configuration.file.YamlConfiguration;
import ru.siaw.free.rpplugin.RPplugin;
import ru.siaw.free.rpplugin.utility.Print;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;

public class FileManager {
    private final RPplugin plugin = RPplugin.getInst();
    private final File dataFolder = plugin.getDataFolder();

    public void checkFiles() {
        for (byte fileNumber = 0; fileNumber < 2; fileNumber++) {
            String fileName = fileNumber == 0 ? "message.yml" : "config.yml";
            File file = new File(dataFolder, fileName);

            if (!dataFolder.exists())
                dataFolder.mkdir();

            if (!file.exists())
                plugin.saveResource(fileName, false);
            else
                try {
                    integrityCheck(file, fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        new DataInitializer().initialize();
    }

    private void integrityCheck(File file, String fileName) throws IOException {
        YamlConfiguration oldYMLFile = YamlConfiguration.loadConfiguration(file);
        Set<String> oldFileKeys = oldYMLFile.getKeys(true);

        String tempFileName = "TEMP_" + fileName;
        Path tempPath = Paths.get(dataFolder + "/" + tempFileName);
        File temp = inputStreamToFile(plugin.getResource(fileName), tempFileName, tempPath);
        YamlConfiguration newYMLFile = YamlConfiguration.loadConfiguration(temp);
        Set<String> newFileKeys = newYMLFile.getKeys(true);

        if (!newFileKeys.equals(oldFileKeys)) {
            Print.errToConsole("A mismatch of your file " + fileName + " with the current one was found. Your file will be overwritten with your parameters saved.");

            storeOldFile(fileName);
            newFileKeys.forEach(key -> {
                if (!oldFileKeys.contains(key) && key.contains(".")) {
                    oldYMLFile.set(key, newYMLFile.get(key));
                }
            });
            oldFileKeys.forEach(key -> {
                if (!newFileKeys.contains(key))
                    oldYMLFile.set(key, null);
            });
            oldYMLFile.save(file);
            Print.msgToConsole(fileName + " updated and now contains new keys.");
        }
        Files.delete(tempPath);
    }

    private File inputStreamToFile(InputStream file, String tempFileName, Path tempPath) throws IOException {
        Files.copy(file, tempPath, StandardCopyOption.REPLACE_EXISTING);
        return new File(dataFolder, tempFileName);
    }

    private void storeOldFile(String fileName) throws IOException {
        Print.infoToConsole("We saved the file in the `OutdatedFiles` folder before making changes to the file.");

        File outdatedFolder = new File(dataFolder, "/OutdatedFiles");
        if (!outdatedFolder.exists())
            outdatedFolder.mkdir();

        File f = new File(dataFolder, fileName);
        File newF = new File(outdatedFolder, "OUTDATED_" + fileName);

        Files.copy(f.toPath(), newF.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}