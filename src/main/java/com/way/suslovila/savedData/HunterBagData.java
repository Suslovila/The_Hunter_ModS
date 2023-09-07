package com.way.suslovila.savedData;


import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HunterBagData  extends SavedData {

        private final List<ItemStack> itemsInBag = new ArrayList<>();
        @Nonnull
        public static HunterBagData get(Level level) {
            if (level.isClientSide) {
                throw new RuntimeException("Don't access this client-side!");
            }
            DimensionDataStorage storage = level.getServer().overworld().getDataStorage();
            return storage.computeIfAbsent(HunterBagData::new, HunterBagData::new, "hunterbagdata");
        }
        public HunterBagData() {
        }

        public HunterBagData(CompoundTag tag) {
            ListTag list = tag.getList("itemsinbag", Tag.TAG_COMPOUND);
           for(int i = 0; i <list.size(); i++){
               CompoundTag compoundtag = list.getCompound(i);
               ItemStack itemstack = ItemStack.of(compoundtag);
               itemsInBag.add(itemstack);
           }

        }
        public List getItemsInBag(){
            return itemsInBag;

        }


        @Override
        public CompoundTag save(CompoundTag tag) {
            ListTag list = new ListTag();
            int size = itemsInBag.size();
            for (int i = 0; i<size; i++){
                CompoundTag tagForItem = new CompoundTag();
            ItemStack item = itemsInBag.get(i);
            item.save(tagForItem);
            list.add(tagForItem);
            }
            tag.put("itemsinbag", list);
            return tag;
        }
        public void addItemsToBag(NonNullList<ItemStack> playerItems){
            for(int i = 0; i < playerItems.size(); i++) {
                if(!playerItems.get(i).isEmpty()){
                if(!playerItems.get(i).isStackable()) {
                    ItemStack certainItem = playerItems.get(i);
                        itemsInBag.add(certainItem);
                        setDirty();

                }
                else {
                    int y = itemsInBag.size();
                    boolean flag = true;
                    boolean wasStackedWith = false;
                        for (int u = 0; u < y; u++) {
                            if (flag) {
                                ItemStack permanentInBag = (itemsInBag.get(u)).copy();
                                ItemStack permanentInInv = (playerItems.get(i)).copy();
                                permanentInBag.setCount(3);
                                permanentInInv.setCount(3);
                                ItemStack InBag = (itemsInBag.get(u)).copy();
                                ItemStack InInv = (playerItems.get(i)).copy();
                            boolean isTheSame = matches2(permanentInInv, permanentInBag);
                            if (isTheSame) {
                                flag = false;

                                itemsInBag.remove(u);
                                setDirty();
                                int sum = InBag.getCount() + InInv.getCount();
                                int sizeOfStacks = permanentInBag.getMaxStackSize();
                                int amountOfStacks = sum / sizeOfStacks;
                                while (amountOfStacks > 0) {
                                    InBag.setCount(sizeOfStacks);
                                    itemsInBag.add(InBag);
                                    setDirty();
                                    amountOfStacks -= 1;
                                }
                                int remainder = sum % sizeOfStacks;
                                if (remainder != 0) {
                                    InInv.setCount(remainder);
                                    itemsInBag.add(InInv);
                                    setDirty();
                                }
                                wasStackedWith = true;
                            }
                        }
                    }
                        if(!wasStackedWith){
                            ItemStack playerItem = playerItems.get(i);
                            itemsInBag.add(playerItem);
                            setDirty();
                        }
                }

                }
            }
        }



    private static boolean matches2(ItemStack pStack, ItemStack pOther) {
         if (!pStack.is(pOther.getItem())) {
            return false;
        } else if (pStack.getTag() == null && pOther.getTag() != null) {
            return false;
        } else {
            return (pStack.getTag()== null || pStack.getTag().equals(pOther.getTag())) && pStack.areCapsCompatible(pOther);
        }
    }
public void removeItemFromBag(ItemStack item){
            itemsInBag.remove(item);
            setDirty();
}
    }

