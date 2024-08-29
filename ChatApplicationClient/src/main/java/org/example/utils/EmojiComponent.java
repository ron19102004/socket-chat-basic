package org.example.utils;

import org.example.interfaces.IEmojiAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class EmojiComponent extends JFrame {
    private final IEmojiAction action;

    public EmojiComponent(IEmojiAction action) {
        this.action = action;
        this.setLayout(new BorderLayout());
        this.setSize(380, 400);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setTitle("Emoji");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
            }
        });
        this.view();
        this.setVisible(false);
    }

    private void view() {
        Path path = Paths.get("");
        String currentDir = path.toAbsolutePath().toString();
        File folder = new File(currentDir+"/emoji/");
        if (folder.exists() && folder.isDirectory()) {
            if (!folder.canRead()) {
                SwingUtilities.invokeLater(() -> {
                    new ToastMessage("Not permission read file emoji", 10);
                });
                return;
            }
            File[] files = folder.listFiles();
            if (files != null) {
                JPanel body = new JPanel(new GridLayout((files.length / 3) + 1, 3, 10, 10));
                JScrollPane scrollPane = new JScrollPane(body);
                scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                for (File file : files) {
                    if (file.isFile()) {
                        ImageIcon imageIcon = ImageIconComponent.fromAssets(file.getAbsolutePath(), 100, 100);
                        JButton btn = new JButton(imageIcon);
                        btn.setBorderPainted(false);
                        btn.setHideActionText(true);
                        btn.setContentAreaFilled(false);
                        btn.addActionListener(e -> {
                            action.action(file.getAbsolutePath());
                            setVisible(false);
                        });
                        body.add(btn);
                    }
                }
                this.add(scrollPane, BorderLayout.CENTER);
            }
        } else {
            SwingUtilities.invokeLater(() -> {
                new ToastMessage("Error load folder emoji", 10);
            });
        }
    }
}
