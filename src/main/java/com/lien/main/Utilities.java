package com.lien.main;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Utilities {

    private static final String directoryPath = System.getenv("LOCALAPPDATA") + File.separator + "ReminderFollowUp";
    private static final String filePath = directoryPath + File.separator + "app_info.json";
    private static TrayIcon trayIcon;

    private Utilities() {
    }

    static {
        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().createImage(Utilities.class.getClassLoader().getResource("images/icon.jpg"));

        PopupMenu menu = new PopupMenu();
        MenuItem done = new MenuItem("Has Finished");
        done.setEnabled(false);
        MenuItem exit = new MenuItem("Quit App");
        done.addActionListener(e -> {
            AppInfo appInfo = getAppInfoFromFile();
            writeContentToFile(appInfo.getName(), appInfo.getLastNotify(), "done", new ArrayList<>(), "");
            enableFinishMenuItem(false);
        });
        exit.addActionListener(e -> {
            System.exit(0);
        });

        menu.add(done);
        menu.add(exit);

        trayIcon = new TrayIcon(image, "Tray Demo", menu);
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("Follow-up Reminder");
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    static void enableFinishMenuItem(boolean enable){
        trayIcon.getPopupMenu().getItem(0).setEnabled(enable);
    }

    static boolean createAppInformationFiles() {
        File directory = new File(directoryPath);
        File file = new File(filePath);

        if (!directory.exists() && directory.mkdirs()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            System.out.println("File created");
        }
        return true;
    }

    static boolean writeContentToFile(String mail, String lastNotify, String status, List<String> patients, String lastFollowUp) {
        try {
            File file = new File(filePath);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new FileOutputStream(file), new AppInfo(mail, lastNotify, status, patients, lastFollowUp));
        } catch (IOException io) {
            io.printStackTrace();
            return false;
        }
        return true;
    }

    static boolean writeContentToFile(String mail) {
        return writeContentToFile(mail, "", "not confirmed", new ArrayList<>(), null);
    }

    static AppInfo getAppInfoFromFile() {
        AppInfo appInfo = new AppInfo();
        try {
            File file = new File(filePath);
            ObjectMapper objectMapper = new ObjectMapper();
            appInfo = objectMapper.readValue(file, AppInfo.class);
        } catch (IOException io) {
            io.printStackTrace();
        }
        return appInfo;
    }

    static void displayNotification() {
        String message = "You have some follow-up need to be made today!";
        trayIcon.displayMessage("Follow-up Reminder", message, TrayIcon.MessageType.INFO);
        enableFinishMenuItem(true);
    }
}
