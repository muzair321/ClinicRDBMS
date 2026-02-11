package com.jetbrains.uzair.app;

import javax.swing.*;
import java.io.*;
import java.nio.file.*;

public class SQLiteBackup {

    // Path to your original SQLite DB
    private static final String DB_PATH = System.getProperty("user.home") + "/ClinicData/clinicData.db";

    public static void backup(JFrame frame) {
        showBackupDialog(frame);
    }
    private static void showBackupDialog(JFrame frame) {
        JFileChooser fileChooser = new JFileChooser() {
            @Override
            protected JDialog createDialog(java.awt.Component parent)
                    throws java.awt.HeadlessException {
                JDialog dialog = super.createDialog(parent);
                dialog.setIconImage(new ImageIcon("src/main/resources/img/Clinic-Colored.png").getImage());
                return dialog;
            }
        };
        fileChooser.showOpenDialog(frame);
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
