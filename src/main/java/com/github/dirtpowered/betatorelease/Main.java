package com.github.dirtpowered.betatorelease;

import com.github.dirtpowered.betatorelease.data.remap.BlockMappings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    public static final Logger LOGGER = LogManager.getLogger("BetaToRelease");
    private static Server server;

    public static void main(String... arguments) {
        LOGGER.info("Welcome to BetaToRelease!");
        LOGGER.info("");
        LOGGER.info("This project is not finished. Expect bugs and weird behaviour");
        LOGGER.info("It may contain peanuts or gluten.");

        BlockMappings.init();
        server = new Server();
        addShutdownHook();
    }

    private static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Shutting down proxy...");
            Main.server.stop();
            LOGGER.info("Goodbye!");
        }));
    }
}