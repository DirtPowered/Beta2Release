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

        PlayerAction playerAction;
        switch (actionId) {
            case 0:
                playerAction = PlayerAction.START_DIGGING;
                break;
            case 2:
                playerAction = PlayerAction.FINISH_DIGGING;
                break;
            case 4:
                playerAction = PlayerAction.DROP_ITEM;
                break;
            case 5:
                playerAction = PlayerAction.RELEASE_USE_ITEM;
                break;
            default:
                playerAction = null;
                break;
        }

        return playerAction;
    }
}
