package com.way.suslovila.savedData.clientSynch;

import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.entity.hunter.HunterEntity;
import com.way.suslovila.entity.hunter.appearance.HunterAppearanceFormEntity;
import com.way.suslovila.entity.projectile.speedArrow.SpeedArrow;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class Messages {

    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(MysticalCreatures.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(PacketSyncVictimToClient.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSyncVictimToClient::new)
                .encoder(PacketSyncVictimToClient::toBytes)
                .consumer(PacketSyncVictimToClient::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToHunter(MSG message, HunterAppearanceFormEntity entity) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }
    public static <MSG> void sendToHunter(MSG message, HunterEntity entity) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }
    public static <MSG> void sendToSpeedArrow(MSG message, SpeedArrow entity) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }
    public static <MSG> void sendToExplosionArrow(MSG message, HunterEntity entity) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }
    public static <MSG> void sendToCloneArrow(MSG message, HunterEntity entity) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }

}