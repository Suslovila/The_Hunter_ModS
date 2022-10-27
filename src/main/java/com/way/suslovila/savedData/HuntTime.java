package com.way.suslovila.savedData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import javax.annotation.Nonnull;


public class HuntTime extends SavedData {
    private int time;
    @Nonnull
    public static HuntTime get(Level level) {
        if (level.isClientSide) {
            throw new RuntimeException("Don't access this client-side!");
        }
        DimensionDataStorage storage = level.getServer().overworld().getDataStorage();
        return storage.computeIfAbsent(HuntTime::new, HuntTime::new, "hunt_time");
    }
    public HuntTime() {
    }

    public HuntTime(CompoundTag tag) {
        int times = tag.getInt("hunt_time");
        time = times;
        setDirty();
    }
    public int getHuntTime(){
        return time;

    }


    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("hunt_time", time);
        return tag;
    }
public void reduceTime(){
        time=time-1;
        setDirty();
}
public void setTime(){
        time = 24000;
}
}
