package com.github.dirtpowered.betatorelease.network.session;

import com.github.dirtpowered.betaprotocollib.utils.Location;

public class BetaPlayer {

    private int entityId;
    private Session session;
    private Location location;
    private boolean onGround;
    private boolean inVehicle;
    private int vehicleEntityId;
    private int dimension;

    BetaPlayer(Session session) {
        this.session = session;
    }

    public boolean isInVehicle() {
        return inVehicle;
    }

    public void setInVehicle(boolean inVehicle, int vehicleEntityId) {
        this.inVehicle = inVehicle;
        this.vehicleEntityId = vehicleEntityId;
    }

    public Session getSession() {
        return session;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getVehicleEntityId() {
        return vehicleEntityId;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }
}
