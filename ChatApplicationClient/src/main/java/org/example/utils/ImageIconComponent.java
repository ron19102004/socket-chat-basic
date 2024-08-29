package org.example.utils;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageIconComponent {
    private ImageIconComponent() {
    }

    public static ImageIcon fromAssets(String imagePath, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    public static String getPathWithNameFile(String name) {
        Path path = Paths.get("");
        String currentDir = path.toAbsolutePath().toString();
        return currentDir + "/emoji/" + name;
    }
}
