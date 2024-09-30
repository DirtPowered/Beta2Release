package com.github.dirtpowered.betatorelease.command;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3ChatPacketData;
import com.github.dirtpowered.betatorelease.Main;
import com.github.dirtpowered.betatorelease.command.impl.BuildCommand;
import com.github.dirtpowered.betatorelease.command.model.AbstractCommand;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;
import com.github.dirtpowered.betatorelease.utils.LegacyTextWrapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private final Map<String, AbstractCommand> commands = new HashMap<>();

    public CommandManager() {
        registerCommand(new BuildCommand(this, "build"));
    }

    public AbstractCommand getCommand(String name) {
        return this.commands.get(name);
    }

    public void sendMessage(BetaPlayer player, String message) {
        message = LegacyTextWrapper.translateAlternateColorCodes('&', message);

        for (String s : LegacyTextWrapper.wrapText(message)) {
            player.getSession().sendPacket(new V1_7_3ChatPacketData(s));
        }
    }

    public boolean handleChatCommand(BetaPlayer player, String message) {
        int firstSlash = message.indexOf('/');
        if (firstSlash == -1) return false;

        String[] args = message.substring(firstSlash + 1).split(" ");
        if (args.length == 0) return false;

        AbstractCommand command = getCommand(args[0]);
        if (command == null) return false;

        try {
            command.execute(player, Arrays.copyOfRange(args, 1, args.length));
        } catch (Exception e) {
            String err = "An error occurred while executing proxy command";
            sendMessage(player, "&c" + err);
            Main.LOGGER.error(err, e);
            return false;
        }
        Main.LOGGER.info("{} executed command: {}", player.getSession().getPlayerName(), message);
        return true;
    }

    public void registerCommand(AbstractCommand command) {
        this.commands.put(command.getName(), command);
    }
}