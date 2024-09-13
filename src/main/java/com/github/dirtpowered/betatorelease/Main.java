package com.github.dirtpowered.betatorelease;

import com.github.dirtpowered.betatorelease.data.remap.BlockMappings;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Main {
    private static Server server;

    public static void main(String... arguments) {
        log.info("Welcome to BetaToRelease!");
        log.info("");
        log.info("This project is not finished. Expect bugs and weird behaviour");
        log.info("It may contain peanuts or gluten.");

        BlockMappings.init();
        server = new Server();
        addShutdownHook();
    }

    private static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down proxy...");
            Main.server.stop();
            log.info("Goodbye!");
        }));
    }
}