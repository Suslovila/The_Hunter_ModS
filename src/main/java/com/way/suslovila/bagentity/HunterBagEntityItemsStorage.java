package com.way.suslovila.bagentity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class HunterBagEntityItemsStorage {
    private ItemStack bag;
    public ItemStack getBag(){
        return bag;
    }
    public void setBag(ItemStack itemStack){
        this.bag = itemStack;
    }
    public void saveNBTData(CompoundTag compound) {
        bag.save(compound);
    }

    public void loadNBTData(CompoundTag compound) {
        bag = ItemStack.of(compound);
    }
}

