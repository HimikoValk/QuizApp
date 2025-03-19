package com.himiko.network;

import com.himiko.Main;
import com.himiko.logger.Logger;
import com.himiko.network.protocol.handler.PackageHandler;
import com.himiko.network.utils.Connection;

public class NetworkWrapper {
    private Logger logger;
    private Connection connection;
    private PackageHandler packageHandler;

    public NetworkWrapper()
    {
        this.logger = Main.logger;
    }

    public void connect(String serverIP, int port) throws Exception
    {
        this.logger.info("Initializing Connection...");
        this.disconnect();

        this.connection = new Connection(serverIP, port);
        if(!this.connection.isConnected()) throw new Exception("No server connection");
        this.packageHandler = new PackageHandler(connection);

        this.logger.info("Successfully init connection!");
    }

    public void disconnect()
    {
        if(this.connection != null && this.connection.isConnected())
        {
            this.connection.close();
            this.logger.warning("Disconnected from Server");
        }else{
            this.logger.warning("There was no connection to Server");
        }

    }

    public void start() throws Exception
    {
        if(this.connection == null || !this.connection.isConnected()) throw new Exception("No connection initialized or no server connection");
        this.packageHandler.start();
    }

    public Logger getLogger() {
        return logger;
    }
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Connection getConnection() {
        return connection;
    }

    public PackageHandler getPackageHandler() {
        return packageHandler;
    }
}
