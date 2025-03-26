package com.himiko.gui.theme;

import java.awt.*;

public abstract class Theme {
    public Color primaryColor;
    public Color secondaryColor;
    public Color hoverColor;
    public Color textColor;
    public Color backgroundColor;
    public Font font;

    public Theme(Color primary, Color secondary, Color hover, Color text, Color background) {
        this.primaryColor = primary;
        this.secondaryColor = secondary;
        this.hoverColor = hover;
        this.textColor = text;
        this.backgroundColor = background;
    }


    public Theme(Color primary, Color secondary, Color hover, Color text, Color background, String fontName,int fontType,int defaultFontSize) {
        this.primaryColor = primary;
        this.secondaryColor = secondary;
        this.hoverColor = hover;
        this.textColor = text;
        this.backgroundColor = background;
        this.font = new Font(fontName, fontType, defaultFontSize);
    }
}
