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
            Print.errToConsole("Найдено несоответсвие файла " + fileName + " обновлённому. Мы перезапишем этот файл с сохранение старых параметров.");

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
            Print.msgToConsole(fileName + " обновлён.");
        }
        Files.delete(tempPath);
    }

    private File inputStreamToFile(InputStream file, String tempFileName, Path tempPath) throws IOException {
        Files.copy(file, tempPath, StandardCopyOption.REPLACE_EXISTING);
        return new File(dataFolder, tempFileName);
    }

    private void storeOldFile(String fileName) throws IOException {
        Print.infoToConsole("Мы сохранили старый файл в папку `OutdatedFiles`.");

        File outdatedFolder = new File(dataFolder, "/OutdatedFiles");
        if (!outdatedFolder.exists())
            outdatedFolder.mkdir();

        File f = new File(dataFolder, fileName);
        File newF = new File(outdatedFolder, "OUTDATED_" + fileName);

        Files.copy(f.toPath(), newF.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}