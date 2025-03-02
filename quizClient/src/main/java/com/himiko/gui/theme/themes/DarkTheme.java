package com.himiko.gui.theme.themes;

import com.himiko.gui.theme.Theme;

import java.awt.*;

public class DarkTheme extends Theme {
     public DarkTheme()
     {
         super(
                 new Color(45, 45, 45), // Primary Color (Dark Gray)
                 new Color(30, 30, 30), // Secondary Color (Darker Gray)
                 new Color(80, 80, 80), // Hover Color (Lighter Gray)
                 new Color(220, 220, 220), // Text Color (Light Gray)
                 new Color(20, 20, 20) // Background Color (Almost Black)
         );
     }
}
