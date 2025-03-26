package com.himiko.gui.manager;

import com.himiko.gui.GUI;
import com.himiko.gui.Panel;
import com.himiko.gui.theme.Theme;
import com.himiko.gui.theme.themes.DarkTheme;

import javax.swing.*;
import java.awt.*;

public class UIManager {
    private Theme currentTheme = null;

    public UIManager()
    {
        this.currentTheme = new DarkTheme(); //Default theme
    }

    public JPanel createStyledPanel(LayoutManager layoutStyle)
    {
        JPanel panel = new JPanel(layoutStyle);
        panel.setBackground(currentTheme.backgroundColor);
        return panel;
    }

    public JLabel createStyledLabel(String text)
    {
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(this.currentTheme.font);
        textLabel.setForeground(GUI.uiManager.getCurrentTheme().textColor);
        textLabel.setSize(new Dimension(text.length() * 7, 30));
        return textLabel;
    }

    public JTextField createStyledTextField(String text) {
        JTextField field = new JTextField(text, 20);
        field.setFont(this.currentTheme.font);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(currentTheme.primaryColor),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setBackground(this.currentTheme.secondaryColor);
        field.setForeground(this.currentTheme.textColor);
        field.setCaretColor(this.currentTheme.textColor);
        field.setSize(new Dimension(250, 40));
        return field;
    }

    public JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(this.currentTheme.font);
        button.setBackground(currentTheme.primaryColor);
        button.setForeground(currentTheme.textColor);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(currentTheme.hoverColor);
                button.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(currentTheme.primaryColor);
                button.repaint();
            }
        });
        return button;
    }

    public JCheckBox createStyledCheckBox(String checkBoxText)
    {
        JCheckBox checkBox = new JCheckBox(checkBoxText);
        checkBox.setFont(this.currentTheme.font);
        checkBox.setBackground(this.currentTheme.backgroundColor);
        checkBox.setForeground(this.currentTheme.textColor);
        checkBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                checkBox.setBackground(currentTheme.hoverColor);
                checkBox.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                checkBox.setBackground(currentTheme.primaryColor);
                checkBox.repaint();
            }
        });;
        return checkBox;
    }

    public void setTheme(Theme theme)
    {
        this.currentTheme = theme;
    }

    public Theme getCurrentTheme() {
        return this.currentTheme;
    }
}
