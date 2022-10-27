package com.way.suslovila.savedData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import javax.annotation.Nonnull;


public class HuntersHP extends SavedData {
    private double hp;
    @Nonnull
    public static HuntersHP get(Level level) {
        if (level.isClientSide) {
            throw new RuntimeException("Don't access this client-side!");
        }
        DimensionDataStorage storage = level.getServer().overworld().getDataStorage();
        return storage.computeIfAbsent(HuntersHP::new, HuntersHP::new, "huntershp");
    }
    public HuntersHP() {
    }

    public HuntersHP(CompoundTag tag) {
        double Hhp = tag.getDouble("hunterhp");
        hp = Hhp;
        setDirty();
    }
    public double getHunterHP(){
        return hp;

    }


    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putDouble("hunterhp", hp);
        return tag;
    }
    public void changeHP(Float Hp){
        hp = Hp;
        if (hp<=0){
            hp = 20;
        }
        setDirty();
    }

}
