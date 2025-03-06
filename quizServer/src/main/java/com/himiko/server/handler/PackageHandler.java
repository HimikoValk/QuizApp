package com.himiko.server.handler;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.himiko.Main;
import com.himiko.game.utils.User;
import com.himiko.logger.Logger;
import com.himiko.server.manager.SessionManager;
import com.himiko.server.protocol.Package;
import com.himiko.server.protocol.PackageCategory;
import com.himiko.server.utils.NetworkClient;
import com.himiko.server.protocol.request.UserRequest;
import com.himiko.server.protocol.request.UserRequestType;

import java.util.List;

/**
 * @author Valk on 14.02.2025
 * @project quizServer
 */
public class PackageHandler{
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
                    this.logger.debug("Received Login!");
                    User userData = parseDataToClass(rawPackage.getData(), User.class);

                    if(SessionManager.getUser(client) != null && SessionManager.doesUsernameExist(userData.getName()))
                    {
                        this.sendPackage(new Package<Boolean>(false, PackageCategory.USER_LOGIN), client);
                        return;
                    }

                    SessionManager.addSession(client, userData);
                    this.sendPackage(new Package<Boolean>(true, PackageCategory.USER_LOGIN), client);
                    this.logger.debug("User data: Name:{} ID:{}", SessionManager.getUser(client).getName(), SessionManager.getUser(client).getId());
                    break;
                }
                case USER_REQUEST -> {
                    this.logger.debug("Received request");
                    UserRequest request = parseDataToClass(rawPackage.getData(), UserRequest.class);

                    switch (request.getRequestType()) {
                        case GET_PLAYER_COUNT:
                            int playerCount = SessionManager.getActiveSessionSize();
                            this.sendPackage(new Package<>(playerCount, PackageCategory.USER_DATA), client);
                            break;

                        case GET_ACTIVE_GAMES:
                            //List<String> activeGames = getActiveGames();
                            //Package<List<String>> gameResponse = new Package<>(activeGames, PackageCategory.USER_DATA);
                            //sendResponse(gameResponse);
                            break;

                        default:
                            this.logger.warning("Unknown UserRequestType received!");
                            break;
                    }
                    break;
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
