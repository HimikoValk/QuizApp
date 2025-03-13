package com.himiko.game;


/**
 * @author Valk on 13.03.2025
 * @project quizClient
 */
public class Alien {
    private int posX;
    private int posY;

    public Alien(int posX, int posY)
    {
        /*
        x = posX;
        y = posY;

         */
    }

    public void move()
    {
        this.posX = this.posX - 2;
    }
}
