package com.way.suslovila.savedData.IsTheVictim;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncVictimToClientBoolean {

    private boolean IsVictim;

    public PacketSyncVictimToClientBoolean(boolean victim) {
        this.IsVictim = victim;
    }

    public PacketSyncVictimToClientBoolean(FriendlyByteBuf buf) {
        IsVictim= buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(IsVictim);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            // Here we are client side.
            // Be very careful not to access client-only classes here! (like Minecraft) because
            // this packet needs to be available server-side too
            ClientVictimDataBoolean.set(IsVictim);
        });
        return true;
    }
}