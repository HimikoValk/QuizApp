package com.himiko.gui.theme;

import java.awt.*;

public abstract class Theme {
    public Color primaryColor;
    public Color secondaryColor;
    public Color hoverColor;
    public Color textColor;
    public Color backgroundColor;

    public Theme(Color primary, Color secondary, Color hover, Color text, Color background) {
        this.primaryColor = primary;
        this.secondaryColor = secondary;
        this.hoverColor = hover;
        this.textColor = text;
        this.backgroundColor = background;
    }
}
