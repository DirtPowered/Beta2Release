package com.github.dirtpowered.betatorelease.proxy;

import com.github.dirtpowered.betatorelease.Server;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.packet.ingame.server.*;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPropertiesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerAbilitiesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerSetExperiencePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerDisplayScoreboardPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerScoreboardObjectivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerTeamPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerUpdateScorePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.*;
import com.github.steveice10.mc.protocol.packet.login.server.LoginSetCompressionPacket;
import com.github.steveice10.mc.protocol.packet.login.server.LoginSuccessPacket;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.event.session.ConnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;
import org.pmw.tinylog.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModernClient {
    private final List<Class<? extends Packet>> skipTranslating = new ArrayList<>();
    private Client client;
    private final Session betaSession;
    private final Server server;

    public ModernClient(Session betaSession) {
        this.betaSession = betaSession;
        this.server = betaSession.getServer();
        skipTranslating.addAll(Arrays.asList(
                LoginSuccessPacket.class,
                ServerPluginMessagePacket.class,
                ServerDifficultyPacket.class,
                ServerUnlockRecipesPacket.class,
                ServerResourcePackSendPacket.class,
                ServerEntityPropertiesPacket.class,
                ServerPlayerListDataPacket.class,
                ServerPlayerAbilitiesPacket.class,
                ServerPlayerSetExperiencePacket.class,
                ServerWorldBorderPacket.class,
                ServerAdvancementsPacket.class,
                ServerScoreboardObjectivePacket.class,
                ServerSpawnParticlePacket.class,
                ServerCombatPacket.class,
                ServerBlockBreakAnimPacket.class,
                LoginSetCompressionPacket.class,
                ServerPlaySoundPacket.class,
                ServerUpdateScorePacket.class,
                ServerTeamPacket.class,
                ServerDisplayScoreboardPacket.class
        ));
    }

    public void connect() {
        MinecraftProtocol protocol = new MinecraftProtocol(betaSession.getPlayerName());
        client = new Client(server.getConfiguration().getRemoteAddress(), server.getConfiguration().getRemotePort(), protocol, new TcpSessionFactory());
        client.getSession().addListener(new SessionAdapter() {
            @Override
            public void packetReceived(PacketReceivedEvent event) {
                /* Modern -> Beta */
                Packet packet = event.getPacket();
                processPacket(packet);
            }

            @Override
            public void connected(ConnectedEvent event) {
                Logger.info("[modern] connected to remote server");
                server.getOnlinePlayers().add(betaSession.getBetaPlayer());
                Logger.info("[main] online: {}", betaSession.getServer().getOnlinePlayers().size());
            }

            @Override
            public void disconnected(DisconnectedEvent event) {
                Logger.info("[modern] disconnected from remote server because: {}", event.getReason());
                disconnect(event.getReason());
                server.getOnlinePlayers().remove(betaSession.getBetaPlayer());
                Logger.info("[main] online: {}", betaSession.getServer().getOnlinePlayers().size());
            }
        });
        client.getSession().connect();
    }

    @SuppressWarnings("unchecked")
    private void processPacket(Packet packet) {
        ModernToBetaHandler handler = getBetaSession().getServer().getTranslatorRegistry().getByPacket(packet);
        if (skipTranslating.contains(packet.getClass()))
            return;

        if (handler != null) {
            handler.translate(packet, getBetaSession());
            if (getBetaSession().getServer().isDebugMode()) {
                Logger.info("[modern] translating {}", packet.getClass().getSimpleName());
            }
        } else {
            Logger.error("[modern] missing 'ModernToBeta' translator for {}", packet.getClass().getSimpleName());
        }
    }

    public void disconnect(String reason) {
        client.getSession().disconnect(reason);
        betaSession.disconnect(reason);
    }

    public void sendModernPacket(Packet modernPacket) {
        client.getSession().send(modernPacket);
    }

    private Session getBetaSession() {
        return betaSession;
    }
}
