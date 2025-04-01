package com.himiko.server.handler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.himiko.Main;
import com.himiko.game.Game;
import com.himiko.game.GameManager;
import com.himiko.game.utils.User;
import com.himiko.logger.Logger;
import com.himiko.server.manager.SessionManager;
import com.himiko.server.protocol.Package;
import com.himiko.server.protocol.PackageCategory;
import com.himiko.server.protocol.data.*;
import com.himiko.server.protocol.response.Response;
import com.himiko.server.protocol.response.ResponseType;
import com.himiko.server.utils.NetworkClient;
import com.himiko.server.protocol.request.Request;

import java.util.ArrayList;
import java.util.List;

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
       // this.logger.debug("Message Received:{}", data);
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
            this.sendResponse(new Response<>(null, ResponseType.ERROR), client);
            this.logger.error("Something went wrong while handling the package... Error:{}", e.getMessage());
        }
    }

    private void handleRequest(Package<JsonElement> rawPackage, NetworkClient client)
    {
        Request<?> request = this.parseDataToClass(rawPackage.getData(), Request.class);

        switch (request.getRequestType())
        {
            case USER_LOGIN ->
            {
                this.logger.debug("Received Login!");
                User userData = this.parseDataToClass(request.getData().toString(), User.class);

                if (SessionManager.getUser(client) != null || SessionManager.doesUsernameExist(userData.getName())) {
                    this.sendResponse(new Response<>("Sorry username already used!",ResponseType.ERROR), client);
                    return;
                }

                SessionManager.addSession(client, userData);
                this.sendResponse(new Response<>(true,ResponseType.SUCCESS), client);
                this.logger.debug("User data: Name:{} ID:{}", SessionManager.getUser(client).getName(), SessionManager.getUser(client).getId());
                break;
            }

            case USER_LOGOUT ->
            {
                this.logger.debug("Received Logout!");

                if (!SessionManager.doesUserExist(client)) {
                    this.sendResponse(new Response<>(null, ResponseType.ERROR), client);
                    return;
                }
                SessionManager.removeSession(client);
                Main.gameManager.removeUser(client);
                break;
            }

            case GET_GAMES ->
            {
                List<GameInfo> gameList = Main.gameManager.getGameInfos();
                this.sendResponse(new Response<>(gameList, ResponseType.GAMES), client);
                break;
            }

            case GAME_START ->
            {
                Long gameID = this.parseDataToClass(request.getData().toString(), Long.class);
                if(gameID == null) return;
                if(!Main.gameManager.isUserInGame(gameID, client))
                {
                    this.sendResponse(new Response<>("You are not in game", ResponseType.ERROR), client);
                    return;
                }else if(!Main.gameManager.isUserCreator(gameID, client))
                {
                    this.sendResponse(new Response<>("Only the game Creator can start the game!", ResponseType.ERROR), client);
                    return;
                }
                Main.gameManager.startGame(gameID);
                this.sendResponse(new Response<>(null, ResponseType.SUCCESS), client);
                break;
            }

            case GAME_JOIN ->
            {
                GameJoinData gameJoinData = this.parseDataToClass(request.getData().toString(), GameJoinData.class);
                //Checking for private game
                if(Main.gameManager.isPrivateGame(gameJoinData.getGameID()))
                {
                    if(gameJoinData.getCode() == null) return;
                    if(Main.gameManager.isCodeCorrect(gameJoinData.getGameID(), gameJoinData.getCode())) {
                        if(Main.gameManager.addUserToGame(gameJoinData.getGameID(), client)) this.sendResponse(new Response<>(null, ResponseType.SUCCESS), client);
                    }else {
                        this.sendResponse(new Response<>("Invalid code..", ResponseType.ERROR), client);
                    }
                }else
                {
                    if(Main.gameManager.addUserToGame(gameJoinData.getGameID(), client)) this.sendResponse(new Response<>(null, ResponseType.SUCCESS), client);
                }
                break;
            }

            case GAME_CREATE ->
            {
                GameCreateData createData = this.parseDataToClass(request.getData().toString(), GameCreateData.class);
                GameInfo info = null;
                if(createData == null || Main.gameManager.isUserInGame(client)) return;
                /* Das ist im Ternärer Operator drinnen
                if(createData.getQuestions() == null)
                {
                    info = Main.gameManager.createGame(client, createData.getMaxUserSize(), createData.isPrivateGame());
                }else
                {
                    info = Main.gameManager.createGame(client, createData.getMaxUserSize(), createData.isPrivateGame(), createData.getQuestions());
                }
                 */
                info = createData.getQuestions() == null ? Main.gameManager.createGame(client, createData.getMaxUserSize(), createData.isPrivateGame(), createData.isAutoStart()) : Main.gameManager.createGame(client, createData.getMaxUserSize(), createData.isPrivateGame(), createData.isAutoStart(),createData.getQuestions());
                this.logger.debug("User creator:{} Game Code:{}", info.getCreatorName(), info.getCode());
                //Add user to his own game
                Main.gameManager.addUserToGame(info.getGameID(), client);
                this.sendResponse(new Response<>(info, ResponseType.SUCCESS),client);
                break;
            }

            case GAME_LEAVE ->
            {
                Long gameID = this.parseDataToClass(request.getData().toString(), Long.class);
                if(gameID != null) {
                    if (Main.gameManager.isUserInGame(gameID, client)) {
                        Main.gameManager.removeUser(gameID, client);
                        this.logger.info("Removed user from game (User:{} GameID:{})!", client.getClient().getRemoteSocketAddress(), gameID);
                    }
                }else
                {
                    Main.gameManager.removeUser(client);
                    this.logger.info("Removed user from game (User:{})!", client.getClient().getRemoteSocketAddress());
                }
                break;
            }

            case GAME_EDIT ->
            {
                Long gameID = this.parseDataToClass(request.getData().toString(), Long.class);
                if(gameID == null) return;

                if(!Main.gameManager.isUserInGame(gameID, client) || Main.gameManager.isUserCreator(gameID, client))
                {
                    this.sendResponse(new Response<>("You are not in game or the creator of the game", ResponseType.ERROR), client);
                    return;
                }

            }

            case ANSWER ->
            {
                String answer = this.parseDataToClass(request.getData().toString(), String.class);
                if(!Main.gameManager.isUserInGame(client)) return;
                Game game = Main.gameManager.getUserGame(client);
                game.storeAnswer(client, answer);
                this.sendResponse(new Response<>("Answer received!", ResponseType.SUCCESS), client);
                break;
            }

            case GET_GAME_INFO ->
            {
                Long gameID = this.parseDataToClass(request.getData().toString(), Long.class);
                if(gameID == null) return;

                if(!Main.gameManager.isUserInGame(gameID, client))
                {
                    this.sendResponse(new Response<>("Not in game..", ResponseType.ERROR), client);
                    return;
                }

                GameInfo gameInfo = Main.gameManager.getGameInfo(gameID);
                this.sendResponse(new Response<>(gameInfo, ResponseType.GAME_INFO),client);
                break;
            }

            case SCORE_INFO ->
            {
                if(!Main.gameManager.isUserInGame(client))
                {
                    this.sendResponse(new Response<>("Not in game..", ResponseType.ERROR), client);
                    return;
                }

                ScoreData scoreData = Main.gameManager.getScoreData(client, Main.gameManager.getUserGame(client).getGameID());
                this.sendResponse(new Response<>(scoreData, ResponseType.SCORE_INFO), client);
            }

            case GET_QUESTION_INFO ->
            {
                if(!Main.gameManager.isUserInGame(client))
                {
                    this.sendResponse(new Response<>("Not in game..", ResponseType.ERROR), client);
                    return;
                }

                QuestionInfo questionInfo = Main.gameManager.getQuestionInfo(Main.gameManager.getUserGame(client).getGameID());
                this.sendResponse(new Response<>(questionInfo, ResponseType.QUESTION_INFO),client);
                break;
            }

            case SERVER_INFORMATION ->
            {
                ServerInformation serverInformation = new ServerInformation(SessionManager.getActiveSessionSize(), Main.gameManager.getPublicGames().size(), Main.version);
                this.sendResponse(new Response<>(serverInformation,ResponseType.SERVER_INFORMATION), client);
                break;
            }
        }
    }

    public <T> void sendResponse(Response<T> response, NetworkClient client)
    {
        this.sendPackage(new Package<>(response, PackageCategory.RESPONSE), client);
    }

    public <T> void sendRequest(Request<T> request, NetworkClient client)
    {
        this.sendPackage(new Package<>(request, PackageCategory.REQUEST), client);
    }

    public <T> void sendPackage(Package<T> data, NetworkClient client)
    {
        if(data == null || client == null) return;

        String json = new Gson().toJson(data);
        client.sendData(json);
        //this.logger.debug("Send Package to Client..\nData:{}", json);
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
