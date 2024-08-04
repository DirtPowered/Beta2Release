package com.github.dirtpowered.betatorelease.configuration;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import lombok.Getter;

@Getter
public class Configuration {
    private final String bindAddress;
    private final int bindPort;
    private final String remoteAddress;
    private final int remotePort;

    public Configuration() {
        CommentedFileConfig config = CommentedFileConfig.builder("config.toml").build();
        if (!config.getFile().exists()) {

            config.load();

            config.set("bind-address", "127.0.0.1");
            config.set("bind-port", 25565);

            config.set("remote-address", "127.0.0.1");
            config.set("remote-port", 25566);
            config.save();
        }

        config.load();

        this.bindAddress = config.get("bind-address");
        this.bindPort = config.get("bind-port");
        this.remoteAddress = config.get("remote-address");
        this.remotePort = config.get("remote-port");
        config.close();
    }
}