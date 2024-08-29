package org.example;

import com.formdev.flatlaf.FlatLightLaf;
import org.example.configs.ClientApplication;
import org.example.utils.EmojiComponent;
import org.example.views.LoginFormView;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args){
        try {
            ClientApplication client = ClientApplication.newClient("103.172.79.198",2004);
//            ClientApplication client = ClientApplication.newClient("localhost",2004);
            UIManager.setLookAndFeel(new FlatLightLaf());
            new LoginFormView(client);
        }catch (UnsupportedLookAndFeelException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}