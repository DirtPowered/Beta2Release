package com.github.dirtpowered.betatorelease.data.utils;

import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction;

public class OldPlayerAction {

    public static PlayerAction getPlayerAction(int actionId) {
        /*
         * Started digging 	 0
         * Finished digging  2
         * Drop item 	     4
         * Shoot arrow 	     5
         */
        return switch (actionId) {
            case 0 -> PlayerAction.START_DIGGING;
            case 2 -> PlayerAction.FINISH_DIGGING;
            case 4 -> PlayerAction.DROP_ITEM;
            case 5 -> PlayerAction.RELEASE_USE_ITEM;
            default -> null;
        };
    }
}