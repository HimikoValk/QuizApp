package com.himiko.gui.screen;

import com.himiko.Main;
import com.himiko.gui.screen.screens.ConnectionScreen;
import com.himiko.gui.screen.screens.GameSelectionScreen;

public class ScreenHandler {
    public final static ScreenHandler INSTANCE = new ScreenHandler();
    private Screen currentScreen;

    //Screens
    public static ConnectionScreen CONNECTION_SCREEN;
    public static GameSelectionScreen GAME_SELECTION_SCREEN;
    static
    {
        CONNECTION_SCREEN = new ConnectionScreen();
        GAME_SELECTION_SCREEN = new GameSelectionScreen();
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
    }


    public Screen getCurrentScreen()
    {
        return this.currentScreen;
    }
}
