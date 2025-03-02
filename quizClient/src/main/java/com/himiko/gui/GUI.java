package com.himiko.gui;


import com.himiko.gui.screen.ScreenHandler;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame
{
    public ScreenHandler screenHandler = ScreenHandler.INSTANCE;
    private Panel renderPanel = null;

    public GUI(String title, String version,int width, int height)
    {
        this.renderPanel = new Panel();

        super.setTitle(title + "|" + version);
        super.getContentPane().add(renderPanel);
        super.setBounds(0,0, width, height);
        super.setLayout(new GridLayout());
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setVisible(true);
        super.setLocationRelativeTo(null);

    }

    public Panel getRenderPanel() {
        return renderPanel;
    }
}
