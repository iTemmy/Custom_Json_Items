package com.temmy.json_items_test_1.file;

import com.temmy.json_items_test_1.Main;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class PluginFiles {
    private PluginFiles(){}

    private static final File dataFolder = Main.getPlugin().getDataFolder();
    private static final String itemPath = dataFolder + "/item/";

    public static void init(){
        try (ZipInputStream inputStream = new ZipInputStream(PluginFiles.class.getProtectionDomain().getCodeSource().getLocation().openStream())){
            createDirs();
            createItemFiles(inputStream);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File[] getItemFiles(){
        File itemDir = new File(itemPath);
        File[] items;
        if(!itemDir.isDirectory() || (items = itemDir.listFiles()) == null)
            return new File[0];
        return items;
    }

    public static File getItemFile(String fileName){
        return new File(itemPath + fileName + ".json");
    }

    private static void createDirs() throws IOException {
        Files.createDirectories(Paths.get(itemPath));
    }

    private static void createItemFiles(ZipInputStream inputStream) throws IOException {
        ZipEntry entry;
        String entryPath;
        File targetFile;
        OutputStream outputStream;

        while(( entry = inputStream.getNextEntry() ) != null){
            entryPath = String.format("/%s", entry.getName());
            targetFile = new File(dataFolder + entryPath);
            if(targetFile.isFile()) continue;

            if(entryPath.startsWith("/item/") &&  entryPath.endsWith(".json")) {
                InputStream initialStream = Main.class.getResourceAsStream(entryPath);
                if(initialStream == null) continue;
                byte[] buffer = new byte[initialStream.available()];
                if(initialStream.read(buffer) < 1) continue;

                outputStream = new FileOutputStream(targetFile);
                outputStream.write(buffer);
            }
        }
    }
}
