package com.github.dirtpowered.betatorelease.network.handler.connection;

import com.github.dirtpowered.betaprotocollib.packet.data.WindowClickPacketData;
import com.github.dirtpowered.betatorelease.Utils.Utils;
import com.github.dirtpowered.betatorelease.network.handler.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.window.ClickItemParam;
import com.github.steveice10.mc.protocol.data.game.window.WindowAction;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import org.pmw.tinylog.Logger;

public class WindowClickPacketHandler implements BetaToModernHandler<WindowClickPacketData> {

    @Override
    public void handlePacket(Session session, WindowClickPacketData packetClass) {

        //TODO: Inventory transactions

        //Don't ask me why. Running it on main thread causing 'timed out'.
        session.getServer().getScheduledExecutorService().execute(() -> {
            int windowId = packetClass.getWindowId();
            int action = packetClass.getAction();
            int inventorySlot = packetClass.getInventorySlot();
            ItemStack itemStack = Utils.betaItemStackToItemStack(packetClass.getItemStack());

            WindowAction windowAction = WindowAction.CLICK_ITEM;

            boolean rightClick = packetClass.getMouseClick() == 1;
            boolean shiftPressed = packetClass.isShiftPressed();

            if (shiftPressed && inventorySlot != -999)
                windowAction = WindowAction.SHIFT_CLICK_ITEM;

            if (inventorySlot == -1) {
                return;
            }

            session.getModernClient().sendModernPacket(new ClientWindowActionPacket(
                    windowId,
                    action,
                    inventorySlot,
                    itemStack,
                    windowAction,
                    rightClick ? ClickItemParam.RIGHT_CLICK : ClickItemParam.LEFT_CLICK
            ));

            Logger.info("WindowAction: {}", windowAction);
        });

        Utils.debug(packetClass);
    }
}
