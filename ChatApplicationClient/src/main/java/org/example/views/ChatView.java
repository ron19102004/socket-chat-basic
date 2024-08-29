package org.example.views;

import org.example.configs.ClientApplication;
import org.example.entities.RequestBuilder;
import org.example.utils.EmojiComponent;
import org.example.utils.ImageIconComponent;
import org.example.utils.ToastMessage;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChatView extends JFrame {
    private final ClientApplication clientApplication;
    private final String username;
    private Color primaryColor;
    private boolean isBoldUsername;
    private EmojiComponent emojiComponent;

    public ChatView(ClientApplication clientApplication, String username) {
        this.clientApplication = clientApplication;
        this.username = username;
        init();
        view();
        this.setVisible(true);
    }

    private void init() {
        this.primaryColor = new Color(0, 168, 39);
        this.isBoldUsername = true;
        this.setLayout(new BorderLayout());
        this.setTitle(this.username + " chat group");
        this.setSize(400, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    public void view() {
        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setContentType("text/html");  // Đặt kiểu nội dung là HTML
        textPane.setFont(new Font("Arial Unicode MS", Font.PLAIN, 15));
        // Tạo StyleDocument để có thể chèn ảnh
        StyledDocument doc = textPane.getStyledDocument();

        new Thread(() -> {
            emojiComponent = new EmojiComponent(pathEmoji -> {
                try {
                    clientApplication.send(RequestBuilder.builder()
                            .action("send-chat-emoji")
                            .data(pathEmoji)
                            .build());
                    ImageIcon img = ImageIconComponent.fromAssets(pathEmoji, 100, 100);
                    try {
                        Style usernameStyle = doc.addStyle("usernameStyle", null);
                        StyleConstants.setForeground(usernameStyle, primaryColor);
                        StyleConstants.setBold(usernameStyle, isBoldUsername);
                        doc.insertString(doc.getLength(), "\n$[me]:\n", usernameStyle);
                        Style style = doc.addStyle("imageStyle", null);
                        StyleConstants.setIcon(style, img);
                        doc.insertString(doc.getLength(), " ", style);
                    } catch (BadLocationException ex) {
                        System.out.println(ex.getMessage());
                    }
                } catch (IOException ex) {
                    SwingUtilities.invokeLater(() -> {
                        new ToastMessage("Send emoji error", 3);
                    });
                }
            });
        }).start();

        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.clientApplication.listen(response -> {
            if (response.isStatus()) {
                LocalTime now = LocalTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                String formattedTime = now.format(formatter);
                String space = "\n";
                if (response.getAction().equalsIgnoreCase("system")) {
                    space = "\n[**************************" + formattedTime + "**************************]\n";
                }
                Style usernameStyle = doc.addStyle("usernameStyle", null);
                StyleConstants.setForeground(usernameStyle, primaryColor);
                StyleConstants.setBold(usernameStyle, isBoldUsername);
                switch (response.getAction()) {
                    case "system" -> {
                        try {
                            doc.insertString(doc.getLength(), space + response.getMessage(), null);
                            SwingUtilities.invokeLater(() -> {
                                new ToastMessage(response.getMessage(), 3);
                            });
                        } catch (BadLocationException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    case "send-chat-messages" -> {
                        try {
                            String[] messRes = response.getMessage().split("%");
                            doc.insertString(doc.getLength(), space + messRes[0], usernameStyle);
                            doc.insertString(doc.getLength(), messRes[1], null);
                            SwingUtilities.invokeLater(() -> {
                                new ToastMessage(response.getMessage(), 3);
                            });
                        } catch (BadLocationException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    case "send-chat-emoji" -> {
                        String[] messRes = response.getMessage().split("&");
                        ImageIcon emoji = ImageIconComponent.fromAssets(messRes[1], 100, 100);
                        try {
                            doc.insertString(doc.getLength(), space + messRes[0] + "\n", usernameStyle);
                            Style style = doc.addStyle("imageStyle", null);
                            StyleConstants.setIcon(style, emoji);
                            doc.insertString(doc.getLength(), " ", style);
                        } catch (BadLocationException e) {
                            System.out.println(e.getMessage());
                        }
                        SwingUtilities.invokeLater(() -> {
                            new ToastMessage("Emoji", 3);
                        });
                    }
                }
                textPane.setCaretPosition(doc.getLength());
            }
        });
        JPanel boxPanel = new JPanel(new BorderLayout());

        JTextField input = getInput(doc);
        JScrollPane iconPanel = getIconPanel(input, doc);

        boxPanel.add(input, BorderLayout.SOUTH);
        boxPanel.add(iconPanel, BorderLayout.NORTH);

        this.add(scrollPane, BorderLayout.CENTER);
        this.add(boxPanel, BorderLayout.SOUTH);
    }

    private JTextField getInput(StyledDocument doc) {
        JTextField input = new JTextField(20);
        input.setText("Chat here :3");
        input.setBackground(primaryColor);
        input.setForeground(Color.WHITE);
        input.setFont(new Font("Arial Unicode MS", Font.BOLD, 15));
        input.setPreferredSize(new Dimension(400, 40));
        input.addActionListener(e -> {
            try {
                if (!input.getText().isEmpty()) {
                    this.clientApplication.send(RequestBuilder.builder()
                            .action("send-chat-messages")
                            .data(input.getText())
                            .build());
                    try {
                        Style usernameStyle = doc.addStyle("usernameStyle", null);
                        StyleConstants.setForeground(usernameStyle, primaryColor);
                        StyleConstants.setBold(usernameStyle, isBoldUsername);
                        doc.insertString(doc.getLength(), "\n$[me]:", usernameStyle);
                        doc.insertString(doc.getLength(), input.getText(), null);
                    } catch (BadLocationException ex) {
                        System.out.println(ex.getMessage());
                    }
                    input.setText("");
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });
        return input;
    }

    private JScrollPane getIconPanel(JTextField input, StyledDocument doc) {
        List<String> icons = List.of("🤞", "😢", "😘", "😒", "🤣", "😊", "😏", "😗", "🙂", "😶", "😛", "😰", "😨", "😱", "😡");
        JPanel iconPanel = new JPanel(new FlowLayout());
        JButton emoji = new JButton("Emoji");
        emoji.addActionListener(e -> {
            if (this.emojiComponent != null) {
                this.emojiComponent.setVisible(true);
            } else {
                SwingUtilities.invokeLater(() -> {
                    new ToastMessage("Emoji loading...", 3);
                });
            }
        });
        emoji.setFont(new Font("Arial Unicode MS", Font.BOLD, 12));
        iconPanel.add(emoji);
        icons.forEach(icon -> {
            JButton btn = new JButton(icon);
            btn.setContentAreaFilled(false);
            btn.setForeground(new Color(2, 0, 0));
            btn.setFont(new Font("Arial Unicode MS", Font.BOLD, 22));
            btn.addActionListener(e -> {
                String mess = input.getText();
                input.setText(mess + " " + icon);
            });
            iconPanel.add(btn);
        });
        return new JScrollPane(iconPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }
}
