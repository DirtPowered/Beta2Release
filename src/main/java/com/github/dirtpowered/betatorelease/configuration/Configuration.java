package com.github.dirtpowered.betatorelease.configuration;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.github.dirtpowered.betaprotocollib.data.version.MinecraftVersion;
import lombok.Getter;
import org.pmw.tinylog.Logger;

import java.util.Arrays;

@Getter
public class Configuration {
    private final String bindAddress;
    private final int bindPort;
    private final String remoteAddress;
    private final int remotePort;
    private MinecraftVersion version;

    public Configuration() {
        CommentedFileConfig config = CommentedFileConfig.builder("config.toml").build();
        if (!config.getFile().exists()) {

            config.load();

            config.set("bind-address", "127.0.0.1");
            config.set("bind-port", 25565);

            config.set("remote-address", "127.0.0.1");
            config.set("remote-port", 25566);
            config.set("version", MinecraftVersion.B_1_7_3.name());
            config.save();
        }

        config.load();

        this.bindAddress = config.get("bind-address");
        this.bindPort = config.get("bind-port");
        this.remoteAddress = config.get("remote-address");
        this.remotePort = config.get("remote-port");

        try {
            this.version = MinecraftVersion.valueOf(config.get("version"));
        } catch (Exception e) {
            Logger.warn("Invalid version, using default version B_1_7_3");
            Logger.warn("Valid versions: {}", Arrays.toString(MinecraftVersion.values()));
            this.version = MinecraftVersion.B_1_7_3;
        }
        config.close();
    }
}