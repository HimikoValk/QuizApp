package com.himiko.gui.screen;

import com.himiko.Main;

import javax.swing.*;
import java.awt.*;

public abstract class Screen implements IScreenListener{
    private JComponent[] components;
    private final String name;

    public Screen(String name)
    {
        this.components = null;
        this.name = name;
    }

    public void setComponents(JComponent... components)
    {
        this.components = components;
    }

    public abstract void render(Graphics g);

    @Override
    public void onEnter() {
        if(this.components != null)
        {
            for(JComponent c : this.components)
            {
                Main.GUI.getRenderPanel().add(c);
            }
        }
    }

    @Override
    public void onLeft() {
        Main.GUI.getRenderPanel().removeAll();
    }

    public String getName() {
        return name;
    }
}
