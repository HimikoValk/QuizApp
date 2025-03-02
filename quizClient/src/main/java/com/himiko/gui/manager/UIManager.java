package com.himiko.gui.manager;

import com.himiko.gui.GUI;
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

    public JLabel createStyledLabel(String text)
    {
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Arial", Font.BOLD, 12));
        textLabel.setForeground(GUI.uiManager.getCurrentTheme().textColor);
        textLabel.setSize(new Dimension(text.length() * 7, 30));
        return textLabel;
    }

    public JTextField createStyledTextField(String text) {
        JTextField field = new JTextField(text, 20);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(currentTheme.primaryColor),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setBackground(currentTheme.secondaryColor);
        field.setForeground(currentTheme.textColor);
        field.setCaretColor(currentTheme.textColor);
        field.setSize(new Dimension(250, 40));
        return field;
    }

    public JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
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

    public void setTheme(Theme theme)
    {
        this.currentTheme = theme;
    }

    public Theme getCurrentTheme() {
        return this.currentTheme;
    }
}
