package com.github.dirtpowered.betatorelease.data.entity.model;

import com.github.dirtpowered.betaprotocollib.utils.Location;
import com.github.dirtpowered.betatorelease.utils.LegacyRelMovementUtil;
import com.github.steveice10.mc.protocol.data.game.entity.type.MobType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public abstract class Entity {
    private int entityId;
    private MobType mobType;
    private UUID uniqueId;

    @Setter
    private int passenger;

    @Setter
    private Location location;

    @Setter
    private LegacyRelMovementUtil.PosResidual residual;

    public Entity(int entityId) {
        this.entityId = entityId;
        this.residual = new LegacyRelMovementUtil.PosResidual();
    }

    public Entity(int entityId, MobType mobType) {
        this.entityId = entityId;
        this.mobType = mobType;
        this.residual = new LegacyRelMovementUtil.PosResidual();
    }

    public Entity(UUID uuid) {
        this.uniqueId = uuid;
    }
}