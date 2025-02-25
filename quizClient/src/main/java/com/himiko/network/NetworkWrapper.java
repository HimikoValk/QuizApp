package com.himiko.network;

import com.himiko.Main;
import com.himiko.logger.Logger;
import com.himiko.network.handler.PackageHandler;
import com.himiko.network.utils.Connection;

public class NetworkWrapper {
    private Logger logger;
    private final Connection connection;
    private final PackageHandler packageHandler;

    public NetworkWrapper() throws Exception
    {
        this.logger = Main.logger;

        this.logger.info("Initializing Connection...");
        this.connection = new Connection("127.0.0.1", 188);
        if(!this.connection.isConnected()) throw new Exception("No server connection");
        this.packageHandler = new PackageHandler(connection);
        this.logger.info("Successfully init connection!");
        //this.packageHandler.run();
    }

    public void start()
    {
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
