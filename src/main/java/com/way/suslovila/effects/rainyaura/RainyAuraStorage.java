package com.way.suslovila.effects.rainyaura;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class RainyAuraStorage {
    private HashMap<BlockPos, Integer> blocks = new HashMap<>();
    private HashMap<UUID, Integer> entities = new HashMap<>();

    public @NotNull HashMap<BlockPos, Integer> getMapOfBlocks(){
        return blocks;
    }
    public @NotNull HashMap<UUID, Integer> getEntities(){
        return entities;
    }
    public void setBlocks(HashMap<BlockPos, Integer> blocks){
        this.blocks = blocks;
    }
    public void removeBlock(BlockPos pos){
        blocks.remove(pos);
    }
    public void removeEntity(UUID entity){
        entities.remove(entity);
    }
    public void addBlockPos(BlockPos pos, int timer){
        blocks.put(pos, timer);
    }
    public void addEntity(UUID entity, int timer){
        entities.put(entity, timer);
    }
    public void saveNBTData(CompoundTag tag) {
        ListTag listTag = new ListTag();
        Set<BlockPos> keys = blocks.keySet();
        Iterator<BlockPos> iterator = keys.iterator();
        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();
            int timer = blocks.get(pos);
            CompoundTag tag1 = new CompoundTag();
            tag1.putInt("x", pos.getX());
            tag1.putInt("y", pos.getY());
            tag1.putInt("z", pos.getZ());
            tag1.putInt("timer", timer);
            listTag.add(tag1);
        }
        tag.put("blocksnear", listTag);


        ListTag listTagForEntity = new ListTag();
        Set<UUID> keysEntities = entities.keySet();
        Iterator<UUID> iterator1 = keysEntities.iterator();
        while (iterator1.hasNext()) {
            UUID entity = iterator1.next();
            int timer = entities.get(entity);
            CompoundTag tag1 = new CompoundTag();
            tag1.putUUID("uuid",entity);
            tag1.putInt("timer", timer);
            listTagForEntity.add(tag1);
        }
        tag.put("entitynear", listTagForEntity);
    }

    public void loadNBTData(CompoundTag compound) {
        ListTag listTag = compound.getList("blocksnear", Tag.TAG_COMPOUND);
        for(int i = 0; i < listTag.size(); i++){
            CompoundTag tag = listTag.getCompound(i);
            blocks.put(new BlockPos(tag.getInt("x"), tag.getInt("y"),tag.getInt("z")), tag.getInt("timer"));
        }

        ListTag listTagForEntity = compound.getList("entitynear", Tag.TAG_COMPOUND);
        for(int i = 0; i < listTagForEntity.size(); i++){
            CompoundTag tag = listTagForEntity.getCompound(i);
            entities.put(tag.getUUID("uuid"), tag.getInt("timer"));
        }
    }
}



