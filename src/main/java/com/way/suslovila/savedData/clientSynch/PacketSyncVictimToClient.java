package com.way.suslovila.savedData.clientSynch;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;


import java.util.UUID;
import java.util.function.Supplier;

public class PacketSyncVictimToClient {

    private UUID victim;

    public PacketSyncVictimToClient(UUID victim) {
        this.victim = victim;
    }

    public PacketSyncVictimToClient(FriendlyByteBuf buf) {
        victim = buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(victim);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            // Here we are client side.
            // Be very careful not to access client-only classes here! (like Minecraft) because
            // this packet needs to be available server-side too
            //System.out.println("VICTIM CLIENT SSUS");
            ClientVictimData.set(victim);
        });
        return true;
    }
}