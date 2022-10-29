package com.way.suslovila.savedData.clientSynch;

import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.savedData.NeedToBeDeleted;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class MessageForRainyAura {

    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(MysticalCreatures.MOD_ID, "messagesforrainyaura"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(NeedToBeDeleted.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(NeedToBeDeleted::new)
                .encoder(NeedToBeDeleted::toBytes)
                .consumer(NeedToBeDeleted::handle)
                .add();


    }


    public static <MSG> void sendRainyAuraInfo(MSG message, LivingEntity entity) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
        System.out.println("We've send info in messages");

    }
}