package com.himiko.server.manager;

import com.himiko.game.utils.User;
import com.himiko.server.utils.NetworkClient;

import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final ConcurrentHashMap<NetworkClient, User> sessions = new ConcurrentHashMap<>();

    public static void addSession(NetworkClient client, User user)
    {
        user.setId(createID(999999L));
        sessions.put(client, user);
    }

    public static void removeSession(NetworkClient client)
    {
        sessions.remove(client);
    }

    public static User getUser(NetworkClient client)
    {
        return sessions.get(client);
    }

    public static boolean doesSessionExist(NetworkClient client)
    {
        return sessions.containsKey(client);
    }

    public static boolean doesUsernameExist(String username)
    {
        return sessions.values().stream().anyMatch(u -> u.getName().equals(username));
    }

    private static long createID(long maxID)
    {
        long id = (long) (Math.random() * maxID);

        if(sessions.values().stream().anyMatch(user -> user.getId() == id)) {
            return createID(maxID);
        }
        return id;
    }
}
