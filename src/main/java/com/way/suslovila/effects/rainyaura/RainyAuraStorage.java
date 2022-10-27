package com.way.suslovila.effects.rainyaura;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public class RainyAuraStorage {
    private ItemStack bag;
    public ItemStack getBag(){
        return bag;
    }
    public void setBag(ItemStack itemStack){
        this.bag = itemStack;
    }
    public void saveNBTData(CompoundTag tag) {
        ListTag list = tag.getList("blocksinnearrange", Tag.TAG_COMPOUND);
        for(int i = 0; i <list.size(); i++){



        }
    }

    public void loadNBTData(CompoundTag compound) {
        bag = ItemStack.of(compound);
    }
}



