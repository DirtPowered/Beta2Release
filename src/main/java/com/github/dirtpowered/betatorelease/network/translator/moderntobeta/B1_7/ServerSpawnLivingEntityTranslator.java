/*
 * Copyright (c) 2020 Dirt Powered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.MobSpawnPacketData;
import com.github.dirtpowered.betatorelease.BetaToRelease;
import com.github.dirtpowered.betatorelease.data.entity.Entity;
import com.github.dirtpowered.betatorelease.data.entity.EntityArmorStand;
import com.github.dirtpowered.betatorelease.data.magicvalues.MagicValues;
import com.github.dirtpowered.betatorelease.network.client.ModernClient;
import com.github.dirtpowered.betatorelease.network.session.ServerSession;
import com.github.dirtpowered.betatorelease.network.translator.model.ModernToBeta;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnLivingEntityPacket;

public class ServerSpawnLivingEntityTranslator implements ModernToBeta<ServerSpawnLivingEntityPacket> {

    @Override
    public void translate(BetaToRelease main, ServerSpawnLivingEntityPacket packet, ServerSession session, ModernClient modernClient) {
        int entityId = packet.getEntityId();
        Entity e = new EntityArmorStand(packet.getEntityId());
        e.setX(packet.getX());
        e.setY(packet.getY());
        e.setZ(packet.getZ());

        session.getEntityCache().addEntity(packet.getEntityId(), e);

        byte entityTypeId = (byte) MagicValues.getEntityTypeId(packet.getType());
        if (entityTypeId == -1) return;

        int x = Utils.toAbsolutePos(packet.getX());
        int y = Utils.toAbsolutePos(packet.getY());
        int z = Utils.toAbsolutePos(packet.getZ());
        byte yaw = (byte) Utils.toAbsoluteRotation((int) packet.getYaw());
        byte pitch = (byte) Utils.toAbsoluteRotation((int) packet.getPitch());


        session.sendPacket(new MobSpawnPacketData(entityId, entityTypeId, x, y, z, yaw, pitch, null));
    }
}
