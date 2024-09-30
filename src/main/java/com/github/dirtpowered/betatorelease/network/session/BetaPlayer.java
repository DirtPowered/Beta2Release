package com.github.dirtpowered.betatorelease.network.session;

import com.github.dirtpowered.betaprotocollib.utils.Location;
import com.github.dirtpowered.betatorelease.data.chunk.BlockDigEntry;
import com.github.dirtpowered.betatorelease.data.tile.FurnaceFuelCache;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction;
import com.github.steveice10.mc.protocol.data.game.window.WindowType;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BetaPlayer {

    @Setter
    private int entityId;

    private final Session session;

    @Setter
    private Location location;

    @Setter
    private boolean onGround;

    private boolean inVehicle;
    private int vehicleEntityId;

    @Setter
    private int dimension;

    @Setter
    private long lastAnimationPacket;

    private final Map<Integer, WindowType> windowTypeMap;

    @Setter
    private BlockDigEntry diggingEntry;

    @Setter
    private FurnaceFuelCache furnaceFuelCache;

    protected BetaPlayer(Session session) {
        this.session = session;
        this.furnaceFuelCache = new FurnaceFuelCache(1600, 200);
        this.windowTypeMap = new HashMap<>();
    }

    public void tick() {
        tickBlockDigging();
    }

    private void tickBlockDigging() {
        if (!isDigging() && System.currentTimeMillis() - lastAnimationPacket > 250L && diggingEntry != null) {
            if (diggingEntry.getPlayerAction() == PlayerAction.FINISH_DIGGING)
                return;

            ClientPlayerActionPacket packet = new ClientPlayerActionPacket(PlayerAction.CANCEL_DIGGING,
                    diggingEntry.getPosition(), diggingEntry.getBlockFace());

            this.session.getModernClient().sendModernPacket(packet);
            setDiggingEntry(null);
        }
    }

    public boolean isDigging() {
        if (diggingEntry == null)
            return false;

        if (diggingEntry.getPlayerAction() == PlayerAction.START_DIGGING)
            return System.currentTimeMillis() - lastAnimationPacket < 250L;

        return false;
    }

    public void setInVehicle(boolean inVehicle, int vehicleEntityId) {
        this.inVehicle = inVehicle;
        this.vehicleEntityId = vehicleEntityId;
    }

    public void cacheWindowType(int windowId, WindowType windowType) {
        this.windowTypeMap.put(windowId, windowType);
    }

    public WindowType getWindowType(int windowId) {
        return windowTypeMap.getOrDefault(windowId, WindowType.GENERIC_INVENTORY);
    }
}