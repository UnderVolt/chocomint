package io.undervolt.gui.splash;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class Splash {

    public static final JFrame splash = new JFrame("chocomint loader");

    public static void launchSplashWindow(IResourceManager resourceManager) throws IOException {

        splash.setType(Window.Type.UTILITY);

        splash.setSize(260, 260);

        splash.setLocationRelativeTo(null);
        splash.setAlwaysOnTop(true);
        splash.setUndecorated(true);


        splash.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Logo
        final ResourceLocation uvLogoResLoc = new ResourceLocation("chocomint/ui/undervolt.png");
        InputStream uvLogoIn = resourceManager.getResource(uvLogoResLoc).getInputStream();
        Image uvLogo = ImageIO.read(uvLogoIn);
        uvLogoIn.close();
        Image uvLogoSized = uvLogo.getScaledInstance(100, 120,  java.awt.Image.SCALE_SMOOTH);

        JPanel contentPane = (JPanel) splash.getContentPane();
        contentPane.setBackground(new Color(255, 248, 151,255));

        JLabel logoContainerLabel = new JLabel("", SwingConstants.CENTER);
        logoContainerLabel.setIcon(new ImageIcon(uvLogoSized));

        contentPane.add(logoContainerLabel);

        splash.setVisible(true);
    }

    public static void removeSplashWindow() {
        splash.setVisible(false);
    }
}
