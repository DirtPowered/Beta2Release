package com.github.dirtpowered.betatorelease.proxy.translator.registry.b17;

import com.github.dirtpowered.betaprotocollib.BetaLib;
import com.github.dirtpowered.betaprotocollib.data.version.MinecraftVersion;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.*;
import com.github.dirtpowered.betatorelease.proxy.translator.clientbound.b17.*;
import com.github.dirtpowered.betatorelease.proxy.translator.registry.BetaToModernRegistry;
import com.github.dirtpowered.betatorelease.proxy.translator.registry.ModernToBetaRegistry;
import com.github.dirtpowered.betatorelease.proxy.translator.serverbound.b17.*;
import com.github.steveice10.mc.protocol.packet.ingame.server.*;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.*;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerChangeHeldItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerHealthPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerUseBedPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.*;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.*;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.*;
import com.github.steveice10.mc.protocol.packet.login.server.LoginDisconnectPacket;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class B17Registry {
    private final BetaToModernRegistry betaToModernRegistry;
    private final ModernToBetaRegistry modernToBetaRegistry;

    public void register() {
        BetaLib.inject(MinecraftVersion.B_1_6_6);

        betaToModernRegistry.registerTranslator(HandshakePacketData.class, new HandshakePacketHandler());
        betaToModernRegistry.registerTranslator(LoginPacketData.class, new LoginPacketHandler());
        betaToModernRegistry.registerTranslator(PlayerLookMovePacketData.class, new PlayerLookMovePacketHandler());
        betaToModernRegistry.registerTranslator(PlayerPositionPacketData.class, new PlayerPositionPacketHandler());
        betaToModernRegistry.registerTranslator(KeepAlivePacketData.class, new KeepAlivePacketHandler());
        betaToModernRegistry.registerTranslator(ChatPacketData.class, new ChatPacketHandler());
        betaToModernRegistry.registerTranslator(UpdateHealthPacketData.class, new UpdateHealthPacketHandler());
        betaToModernRegistry.registerTranslator(KickDisconnectPacketData.class, new KickDisconnectPacketHandler());
        betaToModernRegistry.registerTranslator(RespawnPacketData.class, new RespawnPacketHandler());
        betaToModernRegistry.registerTranslator(AnimationPacketData.class, new AnimationPacketHandler());
        betaToModernRegistry.registerTranslator(EntityActionPacketData.class, new EntityActionPacketHandler());
        betaToModernRegistry.registerTranslator(BlockItemSwitchPacketData.class, new BlockItemSwitchPacketHandler());
        betaToModernRegistry.registerTranslator(BlockDigPacketData.class, new BlockDigPacketHandler());
        betaToModernRegistry.registerTranslator(FlyingPacketData.class, new FlyingPacketHandler());
        betaToModernRegistry.registerTranslator(PlayerLookPacketData.class, new PlayerLookPacketHandler());
        betaToModernRegistry.registerTranslator(BlockPlacePacketData.class, new BlockPlacePacketHandler());
        betaToModernRegistry.registerTranslator(CloseWindowPacketData.class, new CloseWindowPacketHandler());
        betaToModernRegistry.registerTranslator(WindowClickPacketData.class, new WindowClickPacketHandler());
        betaToModernRegistry.registerTranslator(TransactionPacketData.class, new TransactionPacketHandler());
        betaToModernRegistry.registerTranslator(UseEntityPacketData.class, new UseEntityPacketHandler());
        betaToModernRegistry.registerTranslator(UpdateSignPacketData.class, new UpdateSignPacketHandler());

        modernToBetaRegistry.registerTranslator(ServerChatPacket.class, new ServerChatTranslator());
        modernToBetaRegistry.registerTranslator(ServerUpdateTimePacket.class, new UpdateTimeTranslator());
        modernToBetaRegistry.registerTranslator(ServerPlayerHealthPacket.class, new ServerPlayerHealthTranslator());
        modernToBetaRegistry.registerTranslator(ServerRespawnPacket.class, new ServerRespawnTranslator());
        modernToBetaRegistry.registerTranslator(ServerChunkDataPacket.class, new ServerChunkDataTranslator());
        modernToBetaRegistry.registerTranslator(ServerSpawnMobPacket.class, new ServerSpawnMobTranslator());
        modernToBetaRegistry.registerTranslator(ServerJoinGamePacket.class, new ServerJoinGameTranslator());
        modernToBetaRegistry.registerTranslator(ServerSpawnPlayerPacket.class, new ServerSpawnPlayerTranslator());
        modernToBetaRegistry.registerTranslator(ServerPlayerPositionRotationPacket.class, new ServerPlayerPositionRotationTranslator());
        modernToBetaRegistry.registerTranslator(ServerBlockChangePacket.class, new ServerBlockChangeTranslator());
        modernToBetaRegistry.registerTranslator(ServerNotifyClientPacket.class, new ServerNotifyClientTranslator());
        modernToBetaRegistry.registerTranslator(ServerEntityHeadLookPacket.class, new ServerEntityHeadLookTranslator());
        modernToBetaRegistry.registerTranslator(ServerSetSlotPacket.class, new ServerSetSlotTranslator());
        modernToBetaRegistry.registerTranslator(ServerEntityVelocityPacket.class, new ServerEntityVelocityTranslator());
        modernToBetaRegistry.registerTranslator(ServerUnloadChunkPacket.class, new ServerUnloadChunkTranslator());
        modernToBetaRegistry.registerTranslator(ServerMultiBlockChangePacket.class, new ServerMultiBlockChangeTranslator());
        modernToBetaRegistry.registerTranslator(ServerSpawnPositionPacket.class, new ServerSpawnPositionTranslator());
        modernToBetaRegistry.registerTranslator(ServerEntityTeleportPacket.class, new ServerEntityTeleportTranslator());
        modernToBetaRegistry.registerTranslator(ServerEntityPositionPacket.class, new ServerEntityPositionTranslator());
        modernToBetaRegistry.registerTranslator(ServerEntityDestroyPacket.class, new ServerEntityDestroyTranslator());
        modernToBetaRegistry.registerTranslator(ServerEntityAnimationPacket.class, new ServerEntityAnimationTranslator());
        modernToBetaRegistry.registerTranslator(ServerPlayerChangeHeldItemPacket.class, new ServerPlayerChangeHeldItemTranslator());
        modernToBetaRegistry.registerTranslator(ServerEntityPositionRotationPacket.class, new ServerEntityPositionRotationTranslator());
        modernToBetaRegistry.registerTranslator(ServerEntityRotationPacket.class, new ServerEntityRotationTranslator());
        modernToBetaRegistry.registerTranslator(ServerKeepAlivePacket.class, new ServerKeepAliveTranslator());
        modernToBetaRegistry.registerTranslator(LoginDisconnectPacket.class, new LoginDisconnectTranslator());
        modernToBetaRegistry.registerTranslator(ServerEntityStatusPacket.class, new ServerEntityStatusTranslator());
        modernToBetaRegistry.registerTranslator(ServerSpawnGlobalEntityPacket.class, new ServerSpawnGlobalEntityTranslator());
        modernToBetaRegistry.registerTranslator(ServerExplosionPacket.class, new ServerExplosionTranslator());
        modernToBetaRegistry.registerTranslator(ServerOpenWindowPacket.class, new ServerOpenWindowTranslator());
        modernToBetaRegistry.registerTranslator(ServerWindowItemsPacket.class, new ServerWindowItemsTranslator());
        modernToBetaRegistry.registerTranslator(ServerConfirmTransactionPacket.class, new ServerConfirmTransactionTranslator());
        modernToBetaRegistry.registerTranslator(ServerSpawnObjectPacket.class, new ServerSpawnObjectTranslator());
        modernToBetaRegistry.registerTranslator(ServerEntityCollectItemPacket.class, new ServerEntityCollectItemTranslator());
        modernToBetaRegistry.registerTranslator(ServerEntityMetadataPacket.class, new ServerEntityMetadataTranslator());
        modernToBetaRegistry.registerTranslator(ServerUpdateTileEntityPacket.class, new ServerUpdateTileEntityTranslator());
        modernToBetaRegistry.registerTranslator(ServerDisconnectPacket.class, new ServerDisconnectTranslator());
        modernToBetaRegistry.registerTranslator(ServerEntityEquipmentPacket.class, new ServerEntityEquipmentTranslator());
        modernToBetaRegistry.registerTranslator(ServerPlayerListEntryPacket.class, new ServerPlayerListEntryTranslator());
        modernToBetaRegistry.registerTranslator(ServerEntitySetPassengersPacket.class, new ServerEntitySetPassengersTranslator());
        modernToBetaRegistry.registerTranslator(ServerPlayBuiltinSoundPacket.class, new ServerPlayBuiltinSoundTranslator());
        modernToBetaRegistry.registerTranslator(ServerVehicleMovePacket.class, new ServerVehicleMoveTranslator());
        modernToBetaRegistry.registerTranslator(ServerPlayerUseBedPacket.class, new ServerPlayerUseBedTranslator());
        modernToBetaRegistry.registerTranslator(ServerSpawnPaintingPacket.class, new ServerSpawnPaintingTranslator());
        modernToBetaRegistry.registerTranslator(ServerCloseWindowPacket.class, new ServerCloseWindowTranslator());
        modernToBetaRegistry.registerTranslator(ServerWindowPropertyPacket.class, new ServerWindowPropertyTranslator());
        modernToBetaRegistry.registerTranslator(ServerPlayEffectPacket.class, new ServerPlayEffectTranslator());
        modernToBetaRegistry.registerTranslator(ServerMapDataPacket.class, new ServerMapDataTranslator());
        modernToBetaRegistry.registerTranslator(ServerBlockValuePacket.class, new ServerBlockValueTranslator());
    }
}