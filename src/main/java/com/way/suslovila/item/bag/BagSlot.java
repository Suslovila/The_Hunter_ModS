package com.way.suslovila.item.bag;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class BagSlot extends Slot {

    public BagSlot(Container inv, int index, int xPos, int yPos) {
        super(inv, index, xPos, yPos);
    }



    @Override
    public int getMaxStackSize(@Nonnull ItemStack stack) {
        return super.getMaxStackSize();
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return super.mayPlace(stack) && !(stack.getItem() instanceof ItemHunterBag);
    }
}
