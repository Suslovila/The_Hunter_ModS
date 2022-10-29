package com.way.suslovila.savedData.clientSynch;


import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Supplier;

public class PacketSyncRainyAuraToClient {

    private final ArrayList<BlockPos> blocks;
    private final ArrayList<UUID> entities;

    public PacketSyncRainyAuraToClient(ArrayList<BlockPos> blocks, ArrayList<UUID> entities) {
        this.blocks = blocks;
        this.entities = entities;
    }
    public PacketSyncRainyAuraToClient(FriendlyByteBuf buf) {
        CompoundTag compound = buf.readNbt();
        ArrayList<BlockPos> blockPosInside = new ArrayList<>();
        ListTag listTag = compound.getList("blocksnear", Tag.TAG_COMPOUND);
        for(int i = 0; i < listTag.size(); i++){
            CompoundTag tag = listTag.getCompound(i);
            blockPosInside.add(new BlockPos(tag.getInt("x"), tag.getInt("y"),tag.getInt("z")));
        }
        ArrayList<UUID> enttitiesInside = new ArrayList<>();
        ListTag listTagForEntity = compound.getList("entitynear", Tag.TAG_COMPOUND);
        for(int i = 0; i < listTagForEntity.size(); i++){
            CompoundTag tag = listTagForEntity.getCompound(i);
            enttitiesInside.add(tag.getUUID("uuid"));
        }
        blocks = blockPosInside;
        entities = enttitiesInside;
    }

    public void toBytes(FriendlyByteBuf buf) {
        CompoundTag tag = new CompoundTag();
        ListTag listTag = new ListTag();
        for(int i = 0; i < blocks.size(); i++) {
            BlockPos pos = blocks.get(i);
            CompoundTag tag1 = new CompoundTag();
            tag1.putInt("x", pos.getX());
            tag1.putInt("y", pos.getY());
            tag1.putInt("z", pos.getZ());
            listTag.add(tag1);
        }
        tag.put("blocksnear", listTag);

        ListTag listTagForEntity = new ListTag();
        for(int i = 0; i < entities.size(); i++) {
            UUID entity = entities.get(i);
            CompoundTag tag2 = new CompoundTag();
            tag2.putUUID("uuid",entity);
            listTagForEntity.add(tag2);
        }
        tag.put("entitynear", listTagForEntity);

        buf.writeNbt(tag);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            // Here we are client side.
            // Be very careful not to access client-only classes here! (like Minecraft) because
            // this packet needs to be available server-side too
            if(blocks != null && entities != null) {
                ClientRainyAuraData.setBlocksAndEntities(blocks, entities);
            }
        });
        return true;
    }
}