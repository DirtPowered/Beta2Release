package com.github.dirtpowered.betatorelease.configuration;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.github.dirtpowered.betatorelease.data.lang.data.Locale;
import lombok.Getter;

import java.nio.file.Files;
import java.nio.file.Paths;

@Getter
public class Configuration {
    private final String bindAddress;
    private final int bindPort;
    private final String remoteAddress;
    private final int remotePort;
    private final String worldSeed;
    private final boolean strictVersionCheck;
    private final Locale locale;
    private final int renderDistance;
    private final boolean haproxySupport;
    private final boolean onlineMode;
    private final boolean skipTitleMessages;

    private static final String CONFIG_FILE_PATH = "config.toml";

    public Configuration() {
        CommentedFileConfig config = CommentedFileConfig.builder(CONFIG_FILE_PATH).build();
        if (!Files.exists(Paths.get(CONFIG_FILE_PATH))) {
            try {
                config.load();
                config.setComment("world-seed", "Seed from beta 1.7.3 world generator, if applicable");
                config.setComment("strict-version-check", "If true, the proxy will only accept connections from beta 1.7.3 clients");
                config.setComment("locale", "Language to use for client translations (en_US, de_DE, etc.)");
                config.setComment("render-distance", "Number of chunks to render around the player (4-15)");
                config.setComment("online-mode", "If true, the proxy will authenticate with Mojang servers");
                config.setComment("skip-title-messages", "If true, actionbar, title messages won't be sent to the beta client");

                config.set("bind-address", "127.0.0.1");
                config.set("bind-port", 25565);
                config.set("remote-address", "127.0.0.1");
                config.set("remote-port", 25566);
                config.set("world-seed", "-1849830396072973239");
                config.set("strict-version-check", false);
                config.set("locale", "en_US");
                config.set("render-distance", 4);
                config.set("haproxy-support", false);
                config.set("online-mode", false);
                config.set("skip-title-messages", false);
                config.save();
            } catch (Exception e) {
                throw new RuntimeException("Failed to create or initialize the config file", e);
            }
        } else {
            config.load();
        }

        this.bindAddress = config.get("bind-address");
        this.bindPort = config.get("bind-port");
        this.remoteAddress = config.get("remote-address");
        this.remotePort = config.get("remote-port");
        this.worldSeed = config.get("world-seed");
        this.strictVersionCheck = config.getOrElse("strict-version-check", false);
        this.locale = Locale.fromCode(config.getOrElse("locale", "en_US"));
        this.renderDistance = Math.max(4, Math.min(config.getOrElse("render-distance", 4), 15)) - 1;
        this.haproxySupport = config.getOrElse("haproxy-support", false);
        this.onlineMode = config.getOrElse("online-mode", false);
        this.skipTitleMessages = config.getOrElse("skip-title-messages", false);
        config.close();
    }

    public long getWorldSeed() {
        try {
            return Long.parseLong(worldSeed);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}