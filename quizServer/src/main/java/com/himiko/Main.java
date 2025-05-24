package com.himiko;

import com.himiko.game.GameManager;
import com.himiko.logger.Logger;
import com.himiko.logger.LoggerBuilder;
import com.himiko.server.Server;

/**
 * @author Valk on 14.02.2025
 * @project quizServer
 */
public class Main
{
    public static int PORT = 8080; //default port
    public static final String version = "0.0";
    public static Server server = null;
    public static GameManager gameManager = null;
    public static Logger logger = LoggerBuilder.build("ServerLogs.txt").getLogger();

    public static void main(String[] args)
    {
        if(args.length > 0) {
            PORT = Integer.parseInt(args[0]);
        }

        try {
            server = new Server(PORT);
            gameManager = new GameManager();
        }catch (Exception e)
        {
            logger.error("Something went wrong while building server Error:{}", e.getMessage());
        }
    }

}
