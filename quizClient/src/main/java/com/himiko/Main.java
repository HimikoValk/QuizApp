package com.himiko;



import com.himiko.network.Connection;

import java.net.Socket;

/**
 * @author Valk on 14.02.2025
 * @project quizClient
 */
public class Main {
    public static void main(String[] args) {
        try {
            Connection connection = new Connection("127.0.0.1", 188);

            connection.send("HEELLLLOOOOOOO");
            while(connection.isConnected())
            {
                System.out.println(connection.receive());
            }
            connection.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
