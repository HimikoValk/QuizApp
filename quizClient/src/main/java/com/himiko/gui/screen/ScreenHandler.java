package com.himiko.gui.screen;

import com.himiko.Main;
import com.himiko.gui.screen.screens.ConnectionScreen;

public class ScreenHandler {
    public final static ScreenHandler INSTANCE = new ScreenHandler();
    private Screen currentScreen;

    //Screens
    public static ConnectionScreen CONNECTION_SCREEN;
    static
    {
        CONNECTION_SCREEN = new ConnectionScreen();
    }

    public ScreenHandler()
    {

    }

    public void changeScreen(Screen screen)
    {
        if(this.currentScreen != null)
        {
            this.currentScreen.onLeft();
        }

        this.currentScreen = screen;
        this.currentScreen.onEnter();
        /*
        Main.GUI.getRenderPanel().revalidate();
        Main.GUI.getRenderPanel().repaint();
    */
    }


    public Screen getCurrentScreen()
    {
        return this.currentScreen;
    }
}
