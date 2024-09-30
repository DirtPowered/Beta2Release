package com.github.dirtpowered.betatorelease.command.model;

import com.github.dirtpowered.betatorelease.command.CommandManager;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;
import lombok.Data;

@Data
public abstract class AbstractCommand {
    protected CommandManager commandManager;
    protected String name;

    public AbstractCommand(CommandManager commandManager, String name) {
        this.commandManager = commandManager;
        this.name = name;
    }

    public abstract void execute(BetaPlayer player, String[] args);
}