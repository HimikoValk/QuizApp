package com.himiko.server.handler;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.himiko.Main;
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
        //Don't u dare to remove the exp catch!
        // The Server will crash if an error will happen in here
        try {
            Package<JsonElement> rawPackage = gson.fromJson(data, new TypeToken<Package<JsonElement>>() {
            }.getType());
            switch (rawPackage.getAction()) {
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
                    client.sendData("Hello!");
                    break;
                }
            }
        }catch (Exception e)
        {
            client.sendData("FUCK UR PACKAGE!");
            this.logger.error("Something went wrong while handling the package... Error:{}", e.getMessage());
        }
    }

}
