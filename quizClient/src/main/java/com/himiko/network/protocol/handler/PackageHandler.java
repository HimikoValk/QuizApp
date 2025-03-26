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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class PackageHandler extends Thread{
    private Logger logger;
    private Connection connection;
    private Gson gson;
    //BlockingQueue zur synchronen Uebergabe der Server Response
    private final BlockingQueue<Response<?>> responseQueue = new LinkedBlockingQueue<>();

    public PackageHandler(Connection connection)
    {
        this.logger = Main.logger;
        this.connection = connection;
        this.gson = new Gson();
    }

    public void handelPackage(String data)
    {
        this.logger.debug("Message Received:{}", data);
        // Mach das nicht weg
        // Der Server wird crashen wenn hier irgendwas schief laeuft
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

        this.responseQueue.offer(response);

        switch (response.getResponseType()) {
            case GAMES -> {
                Type gameInfoListType = new TypeToken<List<GameInfo>>() {}.getType();
                List<GameInfo> gameInfo = parseDataToClass(response.getData().toString(), gameInfoListType);
                gameInfo.forEach(GameManager::addGame);
                break;
            }
            case SERVER_INFORMATION ->  {
                ServerInformation serverInformation = parseDataToClass(response.getData().toString(), ServerInformation.class);
                GameManager.currentPlayerCount = serverInformation.getPlayerCount();
                this.logger.debug("PlayerCount:{}", serverInformation.getPlayerCount());
                break;
            }
            case ERROR -> {
                this.logger.error("Received a error from server!");
                String errorMessage = "";

                if(response.getData() != null)
                {
                    errorMessage = response.getData().toString();
                }

                JOptionPane.showMessageDialog(null, errorMessage.isEmpty() ? "Something went wrong on server side!" : "Received a error from server!\nError Message:" + errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
                break;
            }
            default ->{
                this.logger.warning("Unknown response type received...");
                break;
            }
        }
    }

    public Response<?> sendRequestWithCallBack(Request<?> request)
    {
        this.responseQueue.clear();

        this.sendRequest(request);
        try
        {
            return this.responseQueue.poll(5, TimeUnit.SECONDS);
        }catch (Exception e)
        {
            this.logger.error("Something went wrong while waiting for response:{}", e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        }
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

    public <T> T parseDataToClass(JsonElement data, Class<T> type)
    {
        return new Gson().fromJson(data, type);
    }

    public <T> T parseDataToClass(String data, Class<T> type)
    {
        return new Gson().fromJson(data, type);
    }

    public <T> T parseDataToClass(String data, Type type) {
        return gson.fromJson(data, type);
    }
}
