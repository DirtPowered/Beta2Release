package com.github.dirtpowered.betatorelease.utils;

import com.github.dirtpowered.betatorelease.Main;
import org.apache.commons.lang3.tuple.MutablePair;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class MojangAuthUtil {
    public static final String SESSION_SERVER = "https://sessionserver.mojang.com/session/minecraft/hasJoined";
    private static final Random random = new SecureRandom();

    /**
     * Checks if the specified player has joined the server asynchronously.
     *
     * @param username The player's username
     * @param serverId The generated server ID
     * @return A CompletableFuture containing a pair with the HTTP response code and the body
     */
    public static CompletableFuture<MutablePair<Integer, String>> hasJoined(String username, String serverId) {
        MutablePair<Integer, String> pair = new MutablePair<>(-1, "");
        HttpClient client = HttpClient.newHttpClient();
        String requestUrl = SESSION_SERVER + "?username=" + username + "&serverId=" + serverId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .header("Content-Type", "application/json")
                .header("Cache-Control", "no-cache")
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(response -> {
            pair.setLeft(response.statusCode());
            pair.setRight(response.body());
            return pair;
        }).exceptionally(ex -> {
            Main.LOGGER.warn("Failed to authenticate session for '{}': {}", username, ex.getMessage());
            return pair;
        });
    }

    public static String getServerId() {
        return Long.toHexString(random.nextLong());
    }
}