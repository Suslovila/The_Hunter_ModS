package com.way.suslovila.event;


import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.savedData.clientSynch.MessageIsVictim;
import com.way.suslovila.savedData.clientSynch.MessageWaterShield;
import com.way.suslovila.savedData.clientSynch.PacketSyncVictimToClient;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ServerProxy {
    private int nextMessageId;

    public void init(final IEventBus modbus) {
    }
    public void onLateInit(final IEventBus modbus) {}

    public void initNetwork() {
        final String version = "1";
        MysticalCreatures.NETWORK = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MysticalCreatures.MOD_ID, "nett"))
                .networkProtocolVersion(() -> version)
                .clientAcceptedVersions(version::equals)
                .serverAcceptedVersions(version::equals)
                .simpleChannel();
               // this.registerServerClientMessage(MessageWaterShield.class, MessageWaterShield::new, MessageWaterShield::toBytes, MessageWaterShield::handle);
        //this.registerServerClientMessage(MessageIsVictim.class, MessageIsVictim::new, MessageIsVictim::toBytes, MessageIsVictim::handle);


    }


    private <MSG> void registerMessage(final Class<MSG> clazz, final Function<FriendlyByteBuf, MSG> decoder, final BiConsumer<MSG, FriendlyByteBuf> encoder, final SimpleChannel.MessageBuilder.ToBooleanBiFunction<MSG, Supplier<NetworkEvent.Context>> consumer) {
        MysticalCreatures.NETWORK.messageBuilder(clazz, this.nextMessageId++)
                .encoder(encoder).decoder(decoder)
                .consumer(consumer)
                .add();
    }
    private <MSG> void registerServerClientMessage(final Class<MSG> clazz, final Function<FriendlyByteBuf, MSG> decoder, final BiConsumer<MSG, FriendlyByteBuf> encoder, final SimpleChannel.MessageBuilder.ToBooleanBiFunction<MSG, Supplier<NetworkEvent.Context>> consumer) {
        MysticalCreatures.NETWORK.messageBuilder(clazz, this.nextMessageId++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(encoder).decoder(decoder)
                .consumer(consumer)
                .add();
    }



}

