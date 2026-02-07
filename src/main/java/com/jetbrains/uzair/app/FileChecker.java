package com.jetbrains.uzair.app;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileChecker {
    public static boolean exists() {
        String userHome = System.getProperty("user.home");
        Path path = Path.of(userHome + "/clinicData.db");
        return Files.exists(path);
    }
}