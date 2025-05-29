package com.himiko.server.manager;

import com.himiko.game.utils.User;
import com.himiko.server.utils.NetworkClient;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final ConcurrentHashMap<WebSocketSession, User> sessions = new ConcurrentHashMap<>();

    public static void addSession(WebSocketSession clientSession, User user)
    {
        user.setId(sessions.size() + 1);
        sessions.put(clientSession, user);
    }

    public static void removeSession(WebSocketSession session)
    {
        sessions.remove(session);
    }

    public static User getUser(WebSocketSession client)
    {
        return sessions.get(client);
    }

    public static boolean doesSessionExist(WebSocketSession client)
    {
        return sessions.containsKey(client);
    }

    public static boolean doesUsernameExist(String username)
    {
        return sessions.values().stream().anyMatch(u -> u.getName().equals(username));
    }

    public static boolean doesUserExist(WebSocketSession client)
    {
        return sessions.containsKey(client);
    }

    public static int getActiveSessionSize()
    {
        return sessions.size();
    }
}
