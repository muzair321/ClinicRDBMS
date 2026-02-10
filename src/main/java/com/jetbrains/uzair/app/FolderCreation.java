package com.jetbrains.uzair.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FolderCreation {
    public static String createDirectories() throws IOException {
        Path dir = Paths.get(
                System.getProperty("user.home"),
                "ClinicData"
        );
        Files.createDirectories(dir);
        return dir.toString();
    }
}