package com.way.suslovila.savedData.clientSynch;

import com.way.suslovila.capability.EntityCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageIsVictim {

    private boolean isVictim;
    private int entityID;

    public MessageIsVictim(boolean activate, int id) {
        this.isVictim = activate;
        entityID = id;
    }

    public MessageIsVictim(FriendlyByteBuf buf) {
        isVictim = buf.readBoolean();
        entityID = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(isVictim);
        buf.writeInt(entityID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
           if (Minecraft.getInstance().level != null) {
               Entity entity = Minecraft.getInstance().level.getEntity(entityID);
                if (entity instanceof LivingEntity) {
                   LivingEntity living = (LivingEntity) entity;
                   living.getCapability(EntityCapabilityProvider.BLOCKS).ifPresent(capa ->
                            {
                               capa.setIsVictim(isVictim);
                            }
                    );
               }
           }




      //      ClientWaterShieldData.setDoShield(hasWaterShield);
        });
        return true;
    }
}
