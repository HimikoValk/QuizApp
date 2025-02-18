package com.himiko;


import com.himiko.logger.Logger;
import com.himiko.logger.LoggerBuilder;
import com.himiko.server.Server;

/**
 * @author Valk on 14.02.2025
 * @project quizServer
 */
public class Main
{
    public static Server server = null;
    public static Logger logger = LoggerBuilder.build("ServerLogs.txt").getLogger();

    public static void main(String[] args)
    {
        //Default port
        int port = 188;

        if(args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        try {
            server = new Server(port);
        }catch (Exception e)
        {
            logger.error("Something went wrong while building server Error:{}", e.getMessage());
        }
    }
}
