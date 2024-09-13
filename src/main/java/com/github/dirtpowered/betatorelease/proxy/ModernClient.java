package com.github.dirtpowered.betatorelease.proxy;

import com.github.dirtpowered.betatorelease.Main;
import com.github.dirtpowered.betatorelease.Server;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.packet.ingame.server.*;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPropertiesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerAbilitiesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerChangeHeldItemPacket;
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
                ServerDisplayScoreboardPacket.class,
                ServerPlayBuiltinSoundPacket.class,
                ServerSetCooldownPacket.class,
                ServerPlayerChangeHeldItemPacket.class
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
                Main.LOGGER.info("Connected to remote server");
                server.getOnlinePlayers().add(betaSession.getBetaPlayer());
                Main.LOGGER.info("Online: {}", betaSession.getServer().getOnlinePlayers().size());
            }

            @Override
            public void disconnected(DisconnectedEvent event) {
                Main.LOGGER.info("Disconnected from remote server because: {}", event.getReason());
                server.getOnlinePlayers().remove(betaSession.getBetaPlayer());
                disconnect(event.getReason());
            }
        });
        client.getSession().connect();
    }

    @SuppressWarnings("unchecked")
    private void processPacket(Packet packet) {
        ModernToBetaHandler handler = getBetaSession().getServer().getModernToBetaRegistry().getByPacket(packet);
        if (skipTranslating.contains(packet.getClass()))
            return;

        if (handler != null) {
            try {
                handler.translate(packet, getBetaSession());
            } catch (Exception e) {
                Main.LOGGER.error("Error while translating packet: {}", packet.getClass().getSimpleName());
                e.printStackTrace();
            }
        } else {
            Main.LOGGER.error("Missing 'ModernToBeta' translator for {}", packet.getClass().getSimpleName());
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
