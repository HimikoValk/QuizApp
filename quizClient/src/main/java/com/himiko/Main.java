package com.himiko;


import com.himiko.gui.GUI;
import com.himiko.logger.Logger;
import com.himiko.logger.LoggerBuilder;
import com.himiko.network.NetworkWrapper;

/**
 * @author Valk on 14.02.2025
 * @project quizClient
 */
public class Main{
    public static Logger logger = LoggerBuilder.build("logs.txt").getLogger();
    public static NetworkWrapper NETWORK;
    public static GUI GUI;

    public static void main(String[] args) {
        try {
            GUI = new GUI("Quizapp", "0.0", 500, 800);
            NETWORK = new NetworkWrapper();
            NETWORK.start();
        }catch (Exception e)
        {
            logger.error("Something went wrong... Error:{}", e.getMessage());
            e.printStackTrace();
        }
    }

}

