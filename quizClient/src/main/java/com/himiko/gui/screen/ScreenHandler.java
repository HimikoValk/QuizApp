package com.himiko.gui.screen;

public class ScreenHandler {
    public final static ScreenHandler INSTANCE = new ScreenHandler();
    private Screen currentScreen;


    //Screens

    static
    {

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
