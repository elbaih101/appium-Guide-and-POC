package org.example.utils;

import java.io.File;

public class FileUtils {
    private FileUtils(){
        super();
    }
    public static File getLatestFile(String path) {
        File folder = new File(path);
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            LogUtils.logWarn("No files found in the directory: ", path);
            return null;
        }
        File latestFile = files[0];
        for (File file : files) {
            if (file.lastModified() > latestFile.lastModified()) {
                latestFile = file;
            }
        }
        return latestFile;
    }

    public static void cleanDirectory(String path){
        File directory = new File(path);
        if (!directory.exists() || !directory.isDirectory()) {
            LogUtils.logInfo("Directory does not exist or is not a directory: " + path);
            return;
        }
        File[] files =directory.listFiles();
        if (files == null) {
          LogUtils.logInfo("No files found or unable to list files in: " + path);
            return;
        }

        for (File file:files){
            try {

                 java.nio.file.Files.delete(file.toPath());
            } catch (Exception e) {
                LogUtils.logInfo("Error deleting file (probably in use): " + file.getName());
            }
        }
    }
    public static void createDirectoryIfNotExist(String dirPath){
        File directory = new File(dirPath);

        if (!directory.exists()) {
            boolean created = directory.mkdirs(); // creates directory and parents if needed
            if (created) {
                LogUtils.logInfo("✅ Directory created: " + dirPath);
            } else {
                LogUtils.logWarn("❌ Failed to create directory: " + dirPath);
            }
        } else {
            LogUtils.logInfo("ℹ️ Directory already exists: " + dirPath);
        }
    }
    }

