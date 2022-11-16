package com.way.suslovila.savedData.clientSynch;


import com.way.suslovila.item.RainyAuraTalisman.RainyAuraTalismanItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class PacketSyncTalismanButtonToServer {

    String talismanHand;
    String whatToChange;
    public PacketSyncTalismanButtonToServer(String hand, String whatToChange) {
        talismanHand = hand;
        this.whatToChange = whatToChange;
    }
    public PacketSyncTalismanButtonToServer(FriendlyByteBuf buf) {
        CompoundTag tag = buf.readNbt();
        whatToChange = tag.getString("whattochange");
        talismanHand = tag.getString("talismanhand");
    }

    public void toBytes(FriendlyByteBuf buf) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("whattochange", whatToChange);
        compoundTag.putString("talismanhand", talismanHand);
        buf.writeNbt(compoundTag);

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            // Here we are server side.
            ServerPlayer player = ctx.getSender();
            if(player != null) {
              if(player.getItemInHand(InteractionHand.valueOf(talismanHand)).getItem() instanceof RainyAuraTalismanItem){
                    ItemStack stack = player.getItemInHand(InteractionHand.valueOf(talismanHand));
                   stack.getOrCreateTag().putBoolean(whatToChange, !stack.getOrCreateTag().getBoolean(whatToChange));
                }
            }
        });
        return true;
    }
}