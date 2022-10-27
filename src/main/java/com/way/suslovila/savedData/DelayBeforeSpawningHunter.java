package com.way.suslovila.savedData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import javax.annotation.Nonnull;


public class DelayBeforeSpawningHunter extends SavedData {
    private int tickDelay;
    @Nonnull
    public static DelayBeforeSpawningHunter get(Level level) {
        if (level.isClientSide) {
            throw new RuntimeException("Don't access this client-side!");
        }
        DimensionDataStorage storage = level.getServer().overworld().getDataStorage();
        return storage.computeIfAbsent(DelayBeforeSpawningHunter::new, DelayBeforeSpawningHunter::new, "huntershp");
    }
    public DelayBeforeSpawningHunter() {
    }

    public DelayBeforeSpawningHunter(CompoundTag tag) {
        int delay = tag.getInt("hunterspawndelay");
        tickDelay = delay;
        setDirty();
    }
    public double getHunterHP(){
        return tickDelay;

    }


    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("hunterspawndelay", tickDelay);
        return tag;
    }
    public void changeHP(int delay){
        tickDelay += delay;
        setDirty();
    }

}
