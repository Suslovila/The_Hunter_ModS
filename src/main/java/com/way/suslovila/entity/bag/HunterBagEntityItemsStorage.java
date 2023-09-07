package com.way.suslovila.entity.bag;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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
        ListTag listTag = new ListTag();
        CompoundTag tag = new CompoundTag();
        bag.save(tag);
        listTag.add(tag);

        compound.put("HunterBag", listTag);
    }

    public void loadNBTData(CompoundTag compound) {
        bag = ItemStack.of(compound.getList("HunterBag", Tag.TAG_COMPOUND).getCompound(0));
        }
}

