package com.way.suslovila.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class EntityCapabilityStorage {
    private boolean hasWaterShield;
    private boolean isVictim;
    public boolean getHasWaterShield(){
        return hasWaterShield;
    }
    public boolean getIsVictim(){
        return isVictim;
    }
    public void setHasWaterShield(boolean flag){
        hasWaterShield = flag;
    }
    public void setIsVictim(boolean flag){
        isVictim = flag;
    }
    public void saveNBTData(CompoundTag tag) {
        tag.putBoolean("hasWaterShield", hasWaterShield);
        tag.putBoolean("isVictim", isVictim);
    }
    public void loadNBTData(CompoundTag compound) {
        hasWaterShield = compound.getBoolean("hasWaterShield");
        isVictim = compound.getBoolean("isVictim");

    }
}