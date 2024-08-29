package org.example.utils;

import javax.swing.*;
import java.awt.*;

public class ImageIconComponent {
    private ImageIconComponent() {
    }

    public static ImageIcon fromAssets(String imagePath, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}
