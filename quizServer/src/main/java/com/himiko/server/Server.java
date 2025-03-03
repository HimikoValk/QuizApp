package com.himiko.server;


import com.himiko.Main;
import com.himiko.logger.Logger;
import com.himiko.server.handler.PackageHandler;
import com.himiko.server.manager.SessionManager;
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

        this.logger.info("Initializing server...");

        this.port = port;
        this.serverSocket = new ServerSocket(port);
        this.packageHandler = new PackageHandler();

        super.start(); // start server (run thread)
    }

    @Override
    public void run() {
        this.logger.debug("Thread:{} Server-Port:{} Server-IP:{}", super.getId(),this.port, this.serverSocket.getLocalSocketAddress().toString());
        this.logger.info("Successfully started server!");

        while(running) {
            try {
                //Connect client
                NetworkClient client = new NetworkClient(this.serverSocket.accept());
                this.clients.add(client);
                this.logger.debug("Client({}) connected..", client.getClient().getRemoteSocketAddress());
                String message = client.receive();

                if(message != null)
                {
                    packageHandler.handelPackage(message, client);
                }else
                {
                    this.closeConnection(client);
                }
            }catch (Exception e)
            {
                this.logger.error("Something went wrong... Error:{}", e.getMessage());
            }
        }
    }

    public void closeConnection(NetworkClient client) throws Exception
    {
        client.getClient().close();
        //Remove client from session
        SessionManager.removeSession(client);
        //Remove client from client list
        this.clients.remove(client);
        //what else should happen? -> User disconnect from room?
        this.logger.warning("Client({}) disconnected from Server", client.getClient().getRemoteSocketAddress().toString());
    }

    public boolean isClientConnected(NetworkClient client)
    {
        return this.clients.stream().anyMatch(client1 -> client1.getClient() == client.getClient());
    }

    public NetworkClient findClient(String clientIP, int clientPort)
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
