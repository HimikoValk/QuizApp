package com.himiko.network.handler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.himiko.Main;
import com.himiko.logger.Logger;
import com.himiko.network.utils.Connection;
import com.himiko.network.protocol.Package;
import com.himiko.network.protocol.PackageCategory;


public class PackageHandler extends Thread{
    private Logger logger;
    private Connection connection;
    private Gson gson;

    public PackageHandler(Connection connection)
    {
        this.logger = Main.logger;
        this.connection = connection;
        this.gson = new Gson();
    }

    public void handelPackage(String data)
    {
        this.logger.debug("Message Received:{}", data);
        //Don't u dare to remove the exp catch!
        // The Server will crash if an error will happen in here
        try {
            Package<JsonElement> rawPackage = gson.fromJson(data, new TypeToken<Package<JsonElement>>() {
            }.getType());
            switch (rawPackage.getCategory()) {
                case USER_DATA -> {
                    break;
                }
                case USER_LOGIN -> {
                    break;
                }
                case USER_REQUEST -> {
                    break;
                }
                default -> {
                    break;
                }
            }
        }catch (Exception e)
        {
            this.logger.error("Something went wrong while handling the package... Error:{}", e.getMessage());
        }
    }

    public <T> void sendData(T data, PackageCategory category)
    {
        if(data == null || category == null) return;

        Package<T> dataPackage = new Package<>(data, category);
        String rawJSON = this.gson.toJson(dataPackage);
        this.connection.send(rawJSON);
        this.logger.debug("Send data to server... Data:{}", rawJSON);
    }

    @Override
    public void run() {
        this.logger.info("Start listening to server...");
        while (this.connection.isConnected()) {
            String content = this.connection.receive();
            if(content != null)
            {
                this.handelPackage(content);
            }
        }
    }
}
