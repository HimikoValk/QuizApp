package com.himiko.network.protocol.handler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.himiko.Main;
import com.himiko.game.manager.GameManager;
import com.himiko.logger.Logger;
import com.himiko.network.protocol.data.GameInfo;
import com.himiko.network.protocol.data.ServerInformation;
import com.himiko.network.protocol.request.Request;
import com.himiko.network.protocol.response.Response;
import com.himiko.network.utils.Connection;
import com.himiko.network.protocol.Package;
import com.himiko.network.protocol.PackageCategory;

import javax.swing.*;
import java.lang.reflect.Type;
import java.util.List;

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
                case RESPONSE ->{
                    this.handelResponse(rawPackage);
                    break;
                }

                case REQUEST -> {
                    break;
                }
                default -> {
                    this.logger.warning("Unknown PackageCategory received!");
                    break;
                }
            }
        }catch (Exception e)
        {
            this.logger.error("Something went wrong while handling the package... Error:{}", e.getMessage());
        }
    }

    public void handelResponse(Package<JsonElement> rawPackage)
    {
        Response<?> response = gson.fromJson(rawPackage.getData(), new TypeToken<Response<?>>() {}.getType());
        this.logger.debug("Handling response: {}", response.getResponseType());

        switch (response.getResponseType()) {
            case GAMES -> {
                Type gameInfoListType = new TypeToken<List<GameInfo>>() {}.getType();
                List<GameInfo> gameInfo = parseDataToClass(response.getData().toString(), gameInfoListType);
                //TODO Implement usage of gameinfo in GameManager
                gameInfo.forEach(GameManager::addGame);
                break;
            }
            case SERVER_INFORMATION ->  {
                ServerInformation serverInformation = parseDataToClass(response.getData().toString(), ServerInformation.class);
                GameManager.currentPlayerCount = serverInformation.getPlayerCount();
                this.logger.debug("PlayerCount:{}", serverInformation.getPlayerCount());
                break;
            }
            case LOGIN_SUCCESS -> {
                Main.NETWORK.setAccess(true);
                this.logger.info("Successfully passed auth!");
                break;
            }
            case LOGIN_FAILED -> {
                Main.NETWORK.setAccess(false);
                this.logger.info("Login attempt was a failure...");
            }
            case ERROR -> {
                this.logger.error("Received a error from server!");
                JOptionPane.showMessageDialog(null, "Something went wrong on server side!", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            }
            default ->{
                this.logger.warning("Unknown response type received...");
                break;
            }
        }
    }

    public void handelRequest(Package<JsonElement> rawPackage)
    {
        Request<?> request = gson.fromJson(rawPackage.getData(), new TypeToken<Request<?>>() {}.getType());
        //TODO:
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

    public <T> void sendRequest(Request<T> request)
    {
        this.sendData(request, PackageCategory.REQUEST);
    }

    public <T> void sendData(T data, PackageCategory category)
    {
        if(data == null || category == null) return;

        Package<T> dataPackage = new Package<>(data, category);
        String rawJSON = this.gson.toJson(dataPackage);
        this.connection.send(rawJSON);
        this.logger.debug("Send data to server... Data:{}", rawJSON);
    }

    private <T> T parseDataToClass(JsonElement data, Class<T> type)
    {
        return new Gson().fromJson(data, type);
    }

    private <T> T parseDataToClass(String data, Class<T> type)
    {
        return new Gson().fromJson(data, type);
    }

    private <T> T parseDataToClass(String data, Type type) {
        return gson.fromJson(data, type);
    }
}
