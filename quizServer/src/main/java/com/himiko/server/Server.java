package com.himiko.server;


import com.himiko.Main;
import com.himiko.logger.Logger;
import com.himiko.server.handler.PackageHandler;
import com.himiko.server.utils.NetworkClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Valk on 14.02.2025
 * @project quizServer
 */
public class Server extends Thread{
    private List<NetworkClient> clients = new ArrayList<>(); //Connected Clients
    private PackageHandler packageHandler;
    private ServerSocket serverSocket;
    private boolean running = true;
    private final int port;

    private Logger logger;

    public Server(final int port) throws IOException
    {

        this.logger = Main.logger;

        this.logger.debug("Initializing Server...");

        this.port = port;
        this.serverSocket = new ServerSocket(port);
        this.packageHandler = new PackageHandler();

        super.start(); // start server (run thread)
    }

    @Override
    public void run() {
        this.logger.debug("Thread:{} Server-Port:{} Server-IP:{}", super.getId(),this.port, this.serverSocket.getLocalSocketAddress().toString());
        this.logger.info("Successfully Started Server!");

        while(running) {
            try {
                //Connect client
                NetworkClient client = new NetworkClient(this.serverSocket.accept());
                this.clients.add(client);
                this.logger.debug("Client({}) connected..", client.getClient().getInetAddress().getHostAddress());
                String message = client.receive();

                if(message != null)
                {
                    packageHandler.handelPackage(message, client);
                }else
                {
                    if(isClientConnected(client)) {
                        closeConnection(client);
                    }
                }

            }catch (Exception e)
            {
                this.logger.error("Something went wrong... Error:{}", e.getMessage());
            }
        }
    }

    private void closeConnection(NetworkClient client) throws Exception
    {
        client.getClient().close();
        //what else should happen? -> User disconnect from room?
        this.logger.warning("Client({}) disconnected from Server", client.getClient().getInetAddress().getHostAddress());
    }

    private boolean isClientConnected(NetworkClient client)
    {
        return this.clients.stream().filter(client1 -> {return client1.getClient() == client.getClient();}).findFirst().get() != null;
    }

    private NetworkClient findClient(String clientIP, int clientPort)
    {
        NetworkClient[] tmp = {null};

        this.clients.forEach(client -> {
            String ip = client.getClient().getInetAddress().getHostAddress();
            int port = client.getClient().getPort();

            if(clientIP.equals(ip) && clientPort == port)
                tmp[0] = client;
        });
        return tmp[0];
    }
}
