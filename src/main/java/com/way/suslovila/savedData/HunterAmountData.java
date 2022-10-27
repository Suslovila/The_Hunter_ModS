package com.way.suslovila.savedData;

import com.way.suslovila.entity.hunter.HunterEntity;
import net.minecraft.nbt.CompoundTag;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import javax.annotation.Nonnull;
import java.util.UUID;

public class HunterAmountData extends SavedData {
    private UUID PreviousHunter;
    @Nonnull
    public static HunterAmountData get(Level level) {
        if (level.isClientSide) {
            throw new RuntimeException("Don't access this client-side!");
        }
        DimensionDataStorage storage = level.getServer().overworld().getDataStorage();
        return storage.computeIfAbsent(HunterAmountData::new, HunterAmountData::new, "amountofhunters");
    }
    public HunterAmountData() {
    }

    public HunterAmountData(CompoundTag tag) {
        UUID PreviousHunterUUID = tag.getUUID("hunter");
        PreviousHunter = PreviousHunterUUID;
        setDirty();
    }
public UUID getPreviousHunter(){
        return PreviousHunter;

}


    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putUUID("hunter", PreviousHunter);
    return tag;
    }
    public void KillPreviousHunter(ServerLevel level){
        if (getPreviousHunter() != null && level.getEntity(getPreviousHunter()) != null ){
            ((HunterEntity)level.getEntity(getPreviousHunter())).disappearInShadows();
            setDirty();
        }
    }
    public void setPreviousHunter(UUID previousHunterUUID){
        PreviousHunter = previousHunterUUID;
    }
}
