package com.github.dirtpowered.betatorelease;

import com.github.dirtpowered.betatorelease.data.remap.BlockMappings;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Logger;

public class Main {


    public static void main(String... arguments) {
        Configurator.currentConfig().formatPattern("[{level} {date:HH:mm:ss}] {message}").activate();

        Logger.info("Welcome to BetaToRelease!");
        Logger.info("");
        Logger.info("This project is not finished. Expect bugs and weird behaviour");
        Logger.info("It may contain peanuts or gluten.");

        BlockMappings.init();
        new Server();

        //TODO: add ShutdownHook
    }
}