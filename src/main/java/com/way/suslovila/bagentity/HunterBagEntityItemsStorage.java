package com.way.suslovila.bagentity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class HunterBagEntityItemsStorage {
    private ArrayList<ItemStack> bag;
    public ArrayList<ItemStack> getBag(){
        return bag;
    }
    public void setBag(ArrayList<ItemStack> itemStacks){
        this.bag = itemStacks;
    }
    public void saveNBTData(CompoundTag compound) {
        ListTag listTag = new ListTag();
        for(int i = 0; i < bag.size(); i++){
            CompoundTag tag = new CompoundTag();
            bag.get(i).save(tag);
            listTag.add(tag);
        }
        compound.put("itemsInHunterBag", listTag);
    }

    public void loadNBTData(CompoundTag compound) {
        bag = new ArrayList<>();
        ListTag listTag = compound.getList("itemsInHunterBag", Tag.TAG_COMPOUND);
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag tag1 = listTag.getCompound(i);
            bag.add(ItemStack.of(tag1));

        }
    }
}

