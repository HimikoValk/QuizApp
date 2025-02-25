package com.himiko.gui;

import com.himiko.gui.screen.ScreenHandler;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {
    public Panel() {
        this.setLayout(null);
    }


    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);


        g.setColor(Color.WHITE);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.fillRect(0, 0, WIDTH, HEIGHT);

        ScreenHandler.INSTANCE.getCurrentScreen().render(g);

        super.repaint();
    }
}
