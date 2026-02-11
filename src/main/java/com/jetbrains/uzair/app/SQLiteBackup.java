package com.jetbrains.uzair.app;

import javax.swing.*;
import java.io.*;
import java.nio.file.*;

public class SQLiteBackup {

    // Path to your original SQLite DB
    private static final String DB_PATH = System.getProperty("user.home") + "/ClinicData/clinicData.db";

    public static void backup() {
        SwingUtilities.invokeLater(SQLiteBackup::showBackupDialog);
    }

    private static void showBackupDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Backup Location");
        fileChooser.setSelectedFile(new File("clinicData.db"));
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File backupFile = fileChooser.getSelectedFile();
            try {
                backupDatabase(backupFile);
                JOptionPane.showMessageDialog(null, "Backup successful to: " + backupFile.getAbsolutePath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Backup failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void backupDatabase(File backupFile) throws IOException {
        Path source = Paths.get(DB_PATH);
        Path destination = backupFile.toPath();
        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
    }
}
