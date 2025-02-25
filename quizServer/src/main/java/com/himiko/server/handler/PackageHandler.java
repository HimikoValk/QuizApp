package com.himiko.server.handler;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.himiko.Main;
import com.himiko.game.utils.User;
import com.himiko.logger.Logger;
import com.himiko.server.protocol.Package;
import com.himiko.server.protocol.enums.PackageCategory;
import com.himiko.server.utils.NetworkClient;

import java.net.Socket;

/**
 * @author Valk on 14.02.2025
 * @project quizServer
 */
public class PackageHandler {
    private Logger logger;
    private Gson gson;

    public PackageHandler()
    {
        this.logger = Main.logger;
        this.gson = new Gson();
    }

    public void handelPackage(String data, NetworkClient client)
    {
        this.logger.debug("Message Received:{}", data);
        // Don't u dare to remove the exp catch!
        // The Server will crash if an error will happen in here
        try {
            Package<JsonElement> rawPackage = gson.fromJson(data, new TypeToken<Package<JsonElement>>() {}.getType());
            switch (rawPackage.getAction()) {
                case USER_DATA -> {
                    this.logger.debug("Received USER_DATA!");
                    User userData = parseDataToClass(rawPackage.getData(), User.class);
                    this.logger.debug("User data: Name:{} ID:{}", userData.getName(), userData.getId());
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

    public <T> void sendPackage(Package<T> data, NetworkClient client)
    {
        if(data == null) return;

        String json = new Gson().toJson(data); // Transfers data to json-format

        client.sendData(json);
    }

    private <T> T parseDataToClass(JsonElement data, Class<T> type)
    {
        return new Gson().fromJson(data, type);
    }
}
