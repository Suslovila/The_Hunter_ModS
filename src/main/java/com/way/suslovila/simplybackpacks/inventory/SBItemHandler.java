package com.way.suslovila.simplybackpacks.inventory;


import com.way.suslovila.savedData.HunterBagData;
import com.way.suslovila.simplybackpacks.util.BackpackUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;


public class SBItemHandler extends ItemStackHandler {
    public SBItemHandler(int size) {
        super(size);
    }

    @Override
    protected void onContentsChanged(int slot) {
        BackpackManager.get().setDirty();
    }

    public void upgrade(int slots) {
        if (slots <= this.stacks.size())
            return;
        NonNullList<ItemStack> oldStacks = this.stacks;
        this.stacks = NonNullList.withSize(slots, ItemStack.EMPTY);
        for (int i = 0; i < oldStacks.size(); i++) {
            this.stacks.set(i, oldStacks.get(i));
        }
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return BackpackUtils.filterItem(stack);
    }


    public void putItemsInBackpackFromHunterStorage(Level level) {
        if (!level.isClientSide()) {
            int size = 18;
            this.stacks = NonNullList.withSize(size, ItemStack.EMPTY);
            Random random = new Random();
            for (int i = 0; i < size; i++) {
                List<ItemStack> itemsInBag = HunterBagData.get(level).getItemsInBag();
                int sizeOfBag = itemsInBag.size();
                if (sizeOfBag != 0) {
                    int number = random.nextInt(3);
                    if (number == 2) {
                        int index = random.nextInt(sizeOfBag);
                        ItemStack item = itemsInBag.get(index);
                        this.stacks.set(i, item);
                        HunterBagData.get(level).removeItemFromBag(itemsInBag.get(index));
                    }
                }
            }
        }
    }
}
