package com.way.suslovila.effects.rainyaura;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class RainyAuraCapProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<RainyAuraStorage> BLOCKS = CapabilityManager.get(new CapabilityToken<>(){});

    private RainyAuraStorage blocksInCap = null;
    private final LazyOptional<RainyAuraStorage> opt = LazyOptional.of(this::createBag);

    @Nonnull
    private RainyAuraStorage createBag() {
        if (blocksInCap == null) {
            blocksInCap = new RainyAuraStorage();
        }
        return blocksInCap;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return getCapability(cap);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (cap == BLOCKS) {
            return opt.cast();
        }
        return LazyOptional.empty();
    }


    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createBag().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createBag().loadNBTData(nbt);
    }
}