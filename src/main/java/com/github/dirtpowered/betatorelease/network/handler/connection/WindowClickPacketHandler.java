package com.github.dirtpowered.betatorelease.network.handler.connection;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.WindowClickPacketData;
import com.github.dirtpowered.betatorelease.network.handler.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.window.ClickItemParam;
import com.github.steveice10.mc.protocol.data.game.window.WindowAction;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;

public class WindowClickPacketHandler implements BetaToModernHandler<WindowClickPacketData> {

    @Override
    public void handlePacket(Session session, WindowClickPacketData packetClass) {
        int windowId = packetClass.getWindowId();
        int action = packetClass.getAction();
        int inventorySlot = packetClass.getInventorySlot();

        ItemStack itemStack;

        if (packetClass.getItemStack() != null) {
            itemStack = Utils.betaItemStackToItemStack(packetClass.getItemStack());
        } else {
            itemStack = null;
        }

        WindowAction windowAction = WindowAction.CLICK_ITEM;

        boolean rightClick = packetClass.getMouseClick() == 1;
        boolean shiftPressed = packetClass.isShiftPressed();

        if (shiftPressed && inventorySlot != -999)
            windowAction = WindowAction.SHIFT_CLICK_ITEM;

        if (inventorySlot == -1)
            return;

        session.getModernClient().sendModernPacket(new ClientWindowActionPacket(windowId, action, inventorySlot, itemStack, windowAction, rightClick ? ClickItemParam.RIGHT_CLICK : ClickItemParam.LEFT_CLICK));
    }
}