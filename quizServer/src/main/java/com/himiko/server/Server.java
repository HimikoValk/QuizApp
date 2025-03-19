package com.himiko.server;


import com.himiko.Main;
import com.himiko.logger.Logger;
import com.himiko.server.handler.PackageHandler;
import com.himiko.server.manager.SessionManager;
import com.himiko.server.utils.NetworkClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Valk on 14.02.2025
 * @project quizServer
 */
public class Server extends Thread{
    private List<NetworkClient> clients = new ArrayList<>(); //Connected Clients
    private HashMap<NetworkClient, Thread> clientThreads = new HashMap<>(); //Handel each client separated
    public PackageHandler packageHandler;
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
                Thread clientThread = createHandleClientThread(client);
                this.clientThreads.put(client, clientThread);
                clientThread.start();
            }catch (Exception e)
            {
                this.logger.error("Something went wrong... Error:{}", e.getMessage());
            }
        }
        this.shutdown();
    }

    public void shutdown() {
        this.running = false;
        try {
            this.serverSocket.close();
            for (NetworkClient client : clients) {
                this.closeConnection(client);
            }
        } catch (IOException e) {
            logger.error("Error while shutting down the server", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create thread for client to handel message (packages)
     * @param client
     * @return
     */
    public Thread createHandleClientThread(NetworkClient client)
    {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    logger.info("Client({}) connected to server and Thread successfully created...", client.getClient().getRemoteSocketAddress().toString());
                    while (running && isClientConnected(client)) {
                        String message = client.receive();

                        if (message != null) {
                            packageHandler.handelPackage(message, client);
                        } else {
                            closeConnection(client);
                            Main.gameManager.removeUser(client);
                        }
                    }
                    logger.warning("Client disconnected, closing client connection and thread");
                }catch(Exception e)
                {
                    logger.error("Something went wrong... Error:{}", e.getMessage());
                }
            }
        });
    }

    /**
     * Close client connection
     * @param client
     * @throws Exception
     */
    public void closeConnection(NetworkClient client) throws Exception
    {
        client.getClient().close();
        //Remove client from session
        SessionManager.removeSession(client);
        //Remove client from client list
        this.clients.remove(client);
        //Remove client Thread
        Thread clientThread = this.clientThreads.remove(client);
        if (clientThread != null) {
            logger.debug("Waiting for client thread to terminate...");
            clientThread.interrupt(); // Waiting for thread until terminated
            logger.debug("Thread terminated");
        }
        this.logger.warning("Client({}) disconnected from Server", client.getClient().getRemoteSocketAddress().toString());
    }

    public boolean isClientConnected(NetworkClient client)
    {
        return this.clients.contains(client);
    }

    // FOR WHAT???????????????????
    /*
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
     */
}
