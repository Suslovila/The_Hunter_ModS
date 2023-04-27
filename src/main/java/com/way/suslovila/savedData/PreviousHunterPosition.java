package com.way.suslovila.savedData;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import javax.annotation.Nonnull;


public class PreviousHunterPosition extends SavedData {
    private BlockPos previousHunterPos;
    @Nonnull
    public static PreviousHunterPosition get(Level level) {
        if (level.isClientSide) {
            throw new RuntimeException("Don't access this client-side!");
        }
        DimensionDataStorage storage = level.getServer().overworld().getDataStorage();
        return storage.computeIfAbsent(PreviousHunterPosition::new, PreviousHunterPosition::new, "previousHunterPos");
    }
    public PreviousHunterPosition() {
    }

    public PreviousHunterPosition(CompoundTag tag) {
        BlockPos PreviousHunter = new BlockPos(tag.getInt("previousHunterPosX"), tag.getInt("previousHunterPosY"), tag.getInt("previousHunterPosZ"));
        previousHunterPos = PreviousHunter;
        setDirty();
    }
    public BlockPos getPreviousHunterPos(){
        return previousHunterPos;

    }


    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("previousHunterPosX",previousHunterPos.getX());
        tag.putInt("previousHunterPosY",previousHunterPos.getY());
        tag.putInt("previousHunterPosZ",previousHunterPos.getZ());
        return tag;
    }
    public void setPreviousHunterPos(BlockPos previousHunterPosNew){
        previousHunterPos = previousHunterPosNew;
    }
}