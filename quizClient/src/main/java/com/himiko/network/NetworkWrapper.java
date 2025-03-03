package com.himiko.network;

import com.himiko.Main;
import com.himiko.logger.Logger;
import com.himiko.network.handler.PackageHandler;
import com.himiko.network.utils.Connection;

import java.util.concurrent.ExecutionException;

public class NetworkWrapper {
    private Logger logger;
    private Connection connection;
    private PackageHandler packageHandler;
    private boolean access = false; //Access for quiz

    public NetworkWrapper()
    {
        this.logger = Main.logger;
    }

    public void connect(String serverIP, int port) throws Exception
    {
        this.logger.info("Initializing Connection...");

        this.connection = new Connection(serverIP, port);
        if(!this.connection.isConnected()) throw new Exception("No server connection");
        this.packageHandler = new PackageHandler(connection);

        this.logger.info("Successfully init connection!");
    }

    public void start() throws Exception
    {
        if(this.connection == null || !this.connection.isConnected()) throw new Exception("No connection initialized or no server connection");
        this.packageHandler.start();
    }

    public Logger getLogger() {
        return logger;
    }
    public void setAccess(boolean access) {this.access = access;}
    public boolean hasAccess()
    {
        return this.access;
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
