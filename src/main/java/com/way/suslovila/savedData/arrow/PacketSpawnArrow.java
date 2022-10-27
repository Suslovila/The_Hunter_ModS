package com.way.suslovila.savedData.arrow;

import com.way.suslovila.savedData.clientSynch.ClientVictimData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketSpawnArrow {
    boolean shouldArrowSpawn;
    public PacketSpawnArrow(boolean should) {
shouldArrowSpawn = should;
    }

    public PacketSpawnArrow(FriendlyByteBuf buf) {
        shouldArrowSpawn = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(shouldArrowSpawn);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            // Here we are client side.
            // Be very careful not to access client-only classes here! (like Minecraft) because
            // this packet needs to be available server-side too
          SpawnArrow.setShouldSpawn(shouldArrowSpawn);

        });
        return true;
    }

}
