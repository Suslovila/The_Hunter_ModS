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
    //Talisman modes: 0 - ACTIVE, 1 - SAVEMOD, 2 - PASSIVE 3 - DISABLED
     private HashMap<BlockPos, Integer> blocks = new HashMap<>();
    private HashMap<Integer, Integer> entities = new HashMap<>();
    private long energy = 0;
    private long maxEnergy = 0;
    private int mode = 0;

    public @NotNull HashMap<BlockPos, Integer> getMapOfBlocks(){
        return blocks;
    }
    public @NotNull HashMap<Integer, Integer> getEntities(){
        return entities;
    }
    public @NotNull long getEnergy(){return energy;}
    public void changeEnergy(long amount){
        energy = ensureRange(energy + amount, 0, maxEnergy);
    }
    public int getMode(){
        return mode;
    }
    public void setBlocks(HashMap<BlockPos, Integer> blocks){
        this.blocks = blocks;
    }
    public void changeMode(){
        if (mode == 2){
            mode = 0;
        }else{
            mode += 1;
        }
    }
    public void setEntities(HashMap<Integer, Integer> entities){
        this.entities = entities;
    }
    public void removeBlock(BlockPos pos){
        blocks.remove(pos);
    }
    public void removeEntity(Integer entity){
        entities.remove(entity);
    }
    public void addBlockPos(BlockPos pos, int timer){
        blocks.put(pos, timer);
    }
    public void addEntity(Integer entity, int timer){
        entities.put(entity, timer);
    }
    public void clearAll(){
        blocks.clear();
        entities.clear();
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
        Set<Integer> keysEntities = entities.keySet();
        Iterator<Integer> iterator1 = keysEntities.iterator();
        while (iterator1.hasNext()) {
            Integer entity = iterator1.next();
            int timer = entities.get(entity);
            CompoundTag tag1 = new CompoundTag();
            tag1.putInt("uuid",entity);
            tag1.putInt("timer", timer);
            listTagForEntity.add(tag1);
        }

        tag.put("entitynear", listTagForEntity);
        CompoundTag tag1 = new CompoundTag();
        tag1.putLong("energy", energy);
        tag1.putLong("maxenergy", energy);
        tag1.putInt("mode", mode);
        tag.put("energyinfo", tag1);

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
            entities.put(tag.getInt("uuid"), tag.getInt("timer"));
        }

        CompoundTag tag = compound.getCompound("energyinfo");
        energy = tag.getLong("energy");
        maxEnergy = tag.getLong("maxenergy");
        mode = tag.getInt("mode");
    }
    public static long ensureRange(long value, long min, long max) {
        return Math.min(Math.max(value, min), max);
    }

}



