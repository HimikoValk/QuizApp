package com.himiko.server.handler;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.himiko.Main;
import com.himiko.game.GameManager;
import com.himiko.game.utils.User;
import com.himiko.logger.Logger;
import com.himiko.server.manager.SessionManager;
import com.himiko.server.protocol.Package;
import com.himiko.server.protocol.PackageCategory;
import com.himiko.server.protocol.data.ServerInformation;
import com.himiko.server.protocol.response.Response;
import com.himiko.server.protocol.response.ResponseType;
import com.himiko.server.utils.NetworkClient;
import com.himiko.server.protocol.request.Request;

/**
 * @author Valk on 14.02.2025
 * @project quizServer
 */
public class PackageHandler{
    private Logger logger;

    public PackageHandler()
    {
        this.logger = Main.logger;
    }

    public void handelPackage(String data, NetworkClient client)
    {
        this.logger.debug("Message Received:{}", data);
        // Don't u dare to remove the exp catch!
        // The Server will crash if an error will happen in here
        try {
            Package<JsonElement> rawPackage = new Gson().fromJson(data, new TypeToken<Package<JsonElement>>() {}.getType());
            switch (rawPackage.getAction()) {
                case REQUEST -> {
                    this.handleRequest(rawPackage, client);
                }
                default -> {
                    this.logger.warning("Unknown PackageType received!");
                    break;
                }
            }
        }catch (Exception e)
        {
            this.logger.error("Something went wrong while handling the package... Error:{}", e.getMessage());
        }
    }

    private void handleRequest(Package<JsonElement> rawPackage, NetworkClient client)
    {
        Request<?> request = this.parseDataToClass(rawPackage.getData(), Request.class);

        switch (request.getRequestType()) {
            case USER_LOGIN -> {
                this.logger.debug("Received Login!");
                User userData = this.parseDataToClass(request.getData().toString(), User.class);

                if (SessionManager.getUser(client) != null && SessionManager.doesUsernameExist(userData.getName())) {
                    this.sendResponse(new Response<>(ResponseType.LOGIN_FAILED, false), client);
                    return;
                }

                SessionManager.addSession(client, userData);
                this.sendResponse(new Response<>(ResponseType.LOGIN_SUCCESS, true), client);
                this.logger.debug("User data: Name:{} ID:{}", SessionManager.getUser(client).getName(), SessionManager.getUser(client).getId());
                break;
            }

            case USER_LOGOUT -> {
                this.logger.debug("Received Logout!");

                if (SessionManager.getUser(client) == null) {
                    this.sendResponse(new Response<>(ResponseType.ERROR, null), client);
                    return;
                }
                SessionManager.removeSession(client);
                break;
            }
            case USER_DATA -> {
                int playerCount = SessionManager.getActiveSessionSize();
                this.sendResponse(new Response<>(ResponseType.PLAYER_COUNT, playerCount), client);
                break;
            }


            case GAME_JOIN -> {
                this.logger.debug("Received join request");
                //TODO:Implement
                /*
                Main.gameManager.addUserToGame();
                */
                break;
            }

            case SERVER_INFORMATION -> {
                ServerInformation serverInformation = new ServerInformation(SessionManager.getActiveSessionSize(), Main.gameManager.getPublicGames().size(), Main.version);
                this.sendResponse(new Response<>(ResponseType.SERVER_INFORMATION, serverInformation), client);
            }
        }
    }

    public <T> void sendResponse(Response<T> data, NetworkClient client)
    {
        this.sendPackage(new Package<Response<T>>(data, PackageCategory.RESPONSE), client);
    }

    public <T> void sendPackage(Package<T> data, NetworkClient client)
    {
        if(data == null) return;

        String json = new Gson().toJson(data);

        client.sendData(json);
    }

    private <T> T parseDataToClass(JsonElement data, Class<T> type)
    {
        return new Gson().fromJson(data, type);
    }

    private <T> T parseDataToClass(String data, Class<T> type)
    {
        return new Gson().fromJson(data, type);
    }
}
