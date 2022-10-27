package com.way.suslovila.savedData.IsTheVictim;

import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.entity.hunter.HunterEntity;
import com.way.suslovila.entity.hunter.appearance.HunterAppearanceFormEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class MessagesBoolean {

    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(MysticalCreatures.MOD_ID, "messagesboolean"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(PacketSyncVictimToClientBoolean.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSyncVictimToClientBoolean::new)
                .encoder(PacketSyncVictimToClientBoolean::toBytes)
                .consumer(PacketSyncVictimToClientBoolean::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToHunter(MSG message, HunterAppearanceFormEntity entity) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
//        System.out.println("We've send info");
    }
    public static <MSG> void sendToHunter(MSG message, HunterEntity entity) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
//        System.out.println("We've send info");
    }

}