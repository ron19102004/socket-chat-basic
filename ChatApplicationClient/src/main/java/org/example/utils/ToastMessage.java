package org.example.utils;

import javax.swing.*;
import java.awt.*;
import java.util.TimerTask;
import java.util.concurrent.*;

public class ToastMessage extends JDialog {
    private static final int TOAST_WIDTH = 300;
    private static final int TOAST_HEIGHT = 50;

    public ToastMessage(String message, int duration) {
        setUndecorated(true);
        setLayout(new GridBagLayout());
        setBackground(new Color(0, 0, 0, 0)); // Transparent background

        JLabel label = new JLabel(message);
        label.setOpaque(true);
        label.setBackground(new Color(0, 0, 0, 170));
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial Unicode MS", Font.BOLD, 16));
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(label);

        pack();
        setSize(TOAST_WIDTH, TOAST_HEIGHT);

        // Center the toast on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = screenSize.height - getHeight() - 50;
        setLocation(x, y);

        // Set timer to automatically close the toast
        Executors.newSingleThreadScheduledExecutor().schedule(this::dispose, duration, TimeUnit.SECONDS);
        Toolkit.getDefaultToolkit().beep();
        setVisible(true);
    }
}