package com.himiko.server.handler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.himiko.game.utils.User;
import com.himiko.server.manager.SessionManager;
import com.himiko.server.protocol.Package;
import com.himiko.server.protocol.PackageCategory;
import com.himiko.server.protocol.response.Response;
import com.himiko.server.protocol.response.ResponseType;
import com.himiko.server.protocol.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author Valk on 14.02.2025
 * @project quizServer
 */
public class PackageHandler {
    private final Logger logger = LoggerFactory.getLogger(PackageHandler.class);
    private final Gson gson = new Gson();

    public PackageHandler() {

    }

    public void handlePackage(String data, WebSocketSession session) {
        try {
            Package<JsonElement> rawPackage = gson.fromJson(data,
                    new TypeToken<Package<JsonElement>>() {}.getType());

            if (rawPackage.getAction() == PackageCategory.REQUEST) {
                this.handleRequest(rawPackage, session);
            } else {
                logger.warn("Unknown PackageType received: {}", rawPackage.getAction());
                this.sendResponse(new Response<>("Unknown PackageType", ResponseType.ERROR), session);
            }
        } catch (Exception e) {
            this.sendResponse(new Response<>(null, ResponseType.ERROR), session);
            logger.error("Error handling package: {}", e.getMessage());
        }
    }

    private void handleRequest(Package<JsonElement> rawPackage, WebSocketSession session) {
        Request<?> request = this.parseDataToClass(rawPackage.getData(), Request.class);
        switch (request.getRequestType()) {
            case USER_LOGIN -> this.handleLogin(request, session);
      /*
            case USER_LOGOUT -> handleLogout(session);
            case GET_GAMES -> sendResponse(
                    new Response<>(Main.gameManager.getGameInfos(), ResponseType.GAMES), session);
            case GAME_START -> handleGameStart(request, session);
            case GAME_JOIN -> handleGameJoin(request, session);
            case GAME_CREATE -> handleGameCreate(request, session);
            case GAME_LEAVE -> handleGameLeave(request, session);
            case ANSWER -> handleAnswer(request, session);
            case GET_GAME_INFO -> handleGetGameInfo(request, session);
            case SCORE_INFO -> handleScoreInfo(session);
            case GET_QUESTION_INFO -> handleQuestionInfo(session);
            case SERVER_INFORMATION -> sendResponse(
                    new Response<>(new ServerInformation(
                            SessionManager.getActiveSessionSize(),
                            Main.gameManager.getPublicGames().size(),
                            Main.version),
                            ResponseType.SERVER_INFORMATION), session);


       */
            default -> logger.warn("Unhandled request type: {}", request.getRequestType());
        }
    }

    private void handleLogin(Request<?> request, WebSocketSession session) {
        logger.debug("Received Login from session {}", session.getId());
        User userData = this.parseDataToClass(request.getData().toString(), User.class);

        if(SessionManager.doesUsernameExist(userData.getName())) {
            this.sendResponse(new Response<>(false, ResponseType.FAILURE), session);
            return;
        }

        SessionManager.addSession(session, userData);
        this.sendResponse(new Response<>(true, ResponseType.SUCCESS), session);
    }

    private void handleLogout(WebSocketSession session) {
        logger.debug("Received Logout for session {}", session.getId());
        SessionManager.removeSession(session);
    }

/*

    private void handleGameStart(Request<?> request, WebSocketSession session) {
        Long gameID = parseDataToClass(request.getData().toString(), Long.class);
        Main.gameManager.startGame(gameID);
        sendResponse(new Response<>(null, ResponseType.SUCCESS), session);
    }

    private void handleGameJoin(Request<?> request, WebSocketSession session) {
        GameJoinData data = parseDataToClass(request.getData().toString(), GameJoinData.class);
        boolean joined = Main.gameManager.addUserToGame(
                data.getGameID(), session, data.getCode());
        sendResponse(new Response<>(joined, joined ? ResponseType.SUCCESS : ResponseType.ERROR), session);
    }

    private void handleGameCreate(Request<?> request, WebSocketSession session) {
        GameCreateData data = parseDataToClass(request.getData().toString(), GameCreateData.class);
        GameInfo info = data.getQuestions() == null
                ? Main.gameManager.createGame(session, data.getMaxUserSize(), data.isPrivateGame(), data.isAutoStart())
                : Main.gameManager.createGame(session, data.getMaxUserSize(), data.isPrivateGame(), data.isAutoStart(), data.getQuestions());
        Main.gameManager.addUserToGame(info.getGameID(), session);
        sendResponse(new Response<>(info, ResponseType.SUCCESS), session);
    }

    private void handleGameLeave(Request<?> request, WebSocketSession session) {
        Long gameID = parseDataToClass(request.getData().toString(), Long.class);
        if (gameID != null && Main.gameManager.isUserInGame(gameID, session)) {
            Main.gameManager.removeUser(gameID, session);
        } else {
            Main.gameManager.removeUser(session);
        }
    }

    private void handleAnswer(Request<?> request, WebSocketSession session) {
        String answer = parseDataToClass(request.getData().toString(), String.class);
        Game game = Main.gameManager.getUserGame(session);
        game.storeAnswer(session, answer);
        sendResponse(new Response<>("Answer received!", ResponseType.SUCCESS), session);
    }

    private void handleGetGameInfo(Request<?> request, WebSocketSession session) {
        Long gameID = parseDataToClass(request.getData().toString(), Long.class);
        if (!Main.gameManager.isUserInGame(gameID, session)) {
            sendResponse(new Response<>("Not in game", ResponseType.ERROR), session);
            return;
        }
        GameInfo info = Main.gameManager.getGameInfo(gameID);
        sendResponse(new Response<>(info, ResponseType.GAME_INFO), session);
    }

    private void handleScoreInfo(WebSocketSession session) {
        if (!Main.gameManager.isUserInGame(session)) {
            sendResponse(new Response<>("Not in game", ResponseType.ERROR), session);
            return;
        }
        ScoreData data = Main.gameManager.getScoreData(session,
                Main.gameManager.getUserGame(session).getGameID());
        sendResponse(new Response<>(data, ResponseType.SCORE_INFO), session);
    }

    private void handleQuestionInfo(WebSocketSession session) {
        if (!Main.gameManager.isUserInGame(session)) {
            sendResponse(new Response<>("Not in game", ResponseType.ERROR), session);
            return;
        }
        QuestionInfo info = Main.gameManager.getQuestionInfo(
                Main.gameManager.getUserGame(session).getGameID());
        sendResponse(new Response<>(info, ResponseType.QUESTION_INFO), session);
    }
 */
    private <T> void sendResponse(Response<T> response, WebSocketSession session) {
        this.sendPackage(new Package<>(response, PackageCategory.RESPONSE), session);
    }

    private <T> void sendPackage(Package<T> pkg, WebSocketSession session) {
        try {
            String json = gson.toJson(pkg);
            session.sendMessage(new TextMessage(json));
        } catch (Exception e) {
            logger.error("Failed to send package to session {}: {}",
                    session.getId(), e.getMessage());
        }
    }

    private <T> T parseDataToClass(JsonElement data, Class<T> type) {
        return gson.fromJson(data, type);
    }

    private <T> T parseDataToClass(String data, Class<T> type) {
        return gson.fromJson(data, type);
    }
}
