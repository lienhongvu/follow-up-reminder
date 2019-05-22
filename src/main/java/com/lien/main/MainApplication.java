package com.lien.main;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainApplication {

    static private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) {
        Utilities.createAppInformationFiles();
        if(Objects.isNull(Utilities.getAppInfoFromFile().getName())) {
            EventQueue.invokeLater(() -> {
                try {
                    Font font = new Font("Verdana", Font.BOLD, 20);
                    JFrame mainFrame = new JFrame();
                    mainFrame.setTitle("Follow Up Reminder Set Up");
                    mainFrame.setVisible(true);
                    mainFrame.setBounds(200, 200, 800, 170);
                    mainFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                    mainFrame.getContentPane().setLayout(null);

                    JTextField nameInputField = new JTextField();
                    nameInputField.setBounds(128, 28, 450, 35);
                    mainFrame.getContentPane().add(nameInputField);
                    nameInputField.setHorizontalAlignment(SwingConstants.RIGHT);
                    nameInputField.setColumns(10);
                    nameInputField.setFont(font);
                    nameInputField.setForeground(Color.BLUE);

                    JLabel nameLabel = new JLabel("Your Name:");
                    nameLabel.setBounds(60, 31, 90, 30);
                    mainFrame.getContentPane().add(nameLabel);

                    JLabel errorLabel = new JLabel("Please input your name which used in followUp document!");
                    errorLabel.setBounds(126, 61, 400, 30);
                    mainFrame.getContentPane().add(errorLabel);
                    errorLabel.setForeground(Color.blue);

                    JButton doneBtn = new JButton("Done");
                    doneBtn.setBounds(590, 31, 90, 30);
                    doneBtn.addActionListener(e -> {
                        if(!"".equals(nameInputField.getText())){
                            mainFrame.setVisible(false);
                            Utilities.writeContentToFile(nameInputField.getText());
                        } else {
                            errorLabel.setForeground(Color.RED);
                        }
                    });
                    mainFrame.getContentPane().add(doneBtn);

                    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                    mainFrame.setLocation(dim.width / 2 - mainFrame.getSize().width / 2, dim.height / 2 - mainFrame.getSize().height / 2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        scheduleJob();
    }

    private static void scheduleJob() {
        scheduler.scheduleAtFixedRate(new ReminderService(), 1, 60, TimeUnit.MINUTES);
    }
}
