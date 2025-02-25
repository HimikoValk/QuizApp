package com.himiko.gui;


import javax.swing.*;

public class GUI extends JFrame
{
    private Panel renderPanel = null;

    public GUI(String title, String version,int width, int height)
    {
        this.renderPanel = new Panel();

        super.setTitle(title + "|" + version);
        super.getContentPane().add(this.renderPanel);
        super.setSize(width, height);
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.setVisible(true);
        super.setResizable(false);
        super.setLocationRelativeTo(null);
    }

    public Panel getRenderPanel() {
        return renderPanel;
    }
}
