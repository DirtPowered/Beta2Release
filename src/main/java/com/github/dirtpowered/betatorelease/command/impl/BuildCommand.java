package com.github.dirtpowered.betatorelease.command.impl;

import com.github.dirtpowered.betatorelease.command.CommandManager;
import com.github.dirtpowered.betatorelease.command.model.AbstractCommand;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;

public class BuildCommand extends AbstractCommand {

    public BuildCommand(CommandManager commandManager, String name) {
        super(commandManager, name);
    }

    @Override
    public void execute(BetaPlayer player, String[] args) {
        Package pkg = getClass().getPackage();

        String versionString = (pkg != null) ? pkg.getImplementationVersion() : "unknown";
        String buildDate = (pkg != null) ? pkg.getImplementationVendor() : "unknown";

        if (buildDate != null && buildDate.length() > 10)
            buildDate = buildDate.substring(0, 10); // only date

        commandManager.sendMessage(player, "&7BetaToRelease &6" + versionString + "&7 built on &6" + buildDate);
    }
}