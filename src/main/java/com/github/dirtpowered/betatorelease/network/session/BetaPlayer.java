package com.github.dirtpowered.betatorelease.network.session;

import com.github.dirtpowered.betaprotocollib.utils.Location;
import com.github.steveice10.mc.protocol.data.game.window.WindowType;
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

    private final Map<Integer, WindowType> windowTypeMap;

    protected BetaPlayer(Session session) {
        this.session = session;
        this.windowTypeMap = new HashMap<>();
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