package org.example.views;

import org.example.configs.ClientApplication;
import org.example.entities.RequestBuilder;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LoginFormView extends JFrame {
    private final ClientApplication clientApplication;

    public LoginFormView(ClientApplication clientApplication) {
        this.clientApplication = clientApplication;
        init();
        view();
        this.setVisible(true);
    }

    private void init() {
        this.setLayout(new BorderLayout());
        this.setTitle("Register username");
        this.setSize(250, 100);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void view() {
        JLabel label = new JLabel("Enter your username");
        label.setForeground(Color.black);
        label.setFont(new Font("Arial",Font.BOLD,15));
        JTextField input = new JTextField();
        input.setColumns(15);
        input.setFont(new Font("Arial",Font.BOLD,15));
        input.addActionListener(e -> {
            try {
                if (!input.getText().isEmpty()) {
                    this.clientApplication.send(RequestBuilder.builder()
                            .action("register-username")
                            .data(input.getText())
                            .build());
                    this.dispose();
                    new ChatView(this.clientApplication,input.getText());
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });
        this.add(label,BorderLayout.NORTH);
        this.add(input,BorderLayout.CENTER);
    }
}
