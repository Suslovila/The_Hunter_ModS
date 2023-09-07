package com.way.suslovila.entity.bag;

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

public class BagProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<HunterBagEntityItemsStorage> BAG = CapabilityManager.get(new CapabilityToken<>(){});

    private HunterBagEntityItemsStorage bag = null;
    private final LazyOptional<HunterBagEntityItemsStorage> opt = LazyOptional.of(this::createBag);

    @Nonnull
    private HunterBagEntityItemsStorage createBag() {
        if (bag == null) {
            bag = new HunterBagEntityItemsStorage();
        }
        return bag;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return getCapability(cap);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (cap == BAG) {
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