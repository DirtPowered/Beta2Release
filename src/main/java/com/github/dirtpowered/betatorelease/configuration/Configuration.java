package com.github.dirtpowered.betatorelease.configuration;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
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

    private static final String CONFIG_FILE_PATH = "config.toml";

    public Configuration() {
        CommentedFileConfig config = CommentedFileConfig.builder(CONFIG_FILE_PATH).build();
        if (!Files.exists(Paths.get(CONFIG_FILE_PATH))) {
            try {
                config.load();
                config.setComment("world-seed", "Seed from beta 1.7.3 world generator, if applicable");

                config.set("bind-address", "127.0.0.1");
                config.set("bind-port", 25565);
                config.set("remote-address", "127.0.0.1");
                config.set("remote-port", 25566);
                config.set("world-seed", "-1849830396072973239");
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