package com.way.suslovila.simplybackpacks.gui;



import com.way.suslovila.simplybackpacks.items.BackpackItem;
import com.way.suslovila.simplybackpacks.util.BackpackUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class SBContainerSlot extends SlotItemHandler {
    private Predicate<ItemStack> filterPredicate = BackpackUtils::filterItem;

    public SBContainerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    public SBContainerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Predicate<ItemStack> filterPredicate) {
        super(itemHandler, index, xPosition, yPosition);
        this.filterPredicate = filterPredicate;
    }

    @Override
    public int getMaxStackSize(@Nonnull ItemStack stack) {
        return super.getMaxStackSize();
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        if(!super.mayPlace(stack))
            return false;
        if (stack.getItem() instanceof BackpackItem){
            return false;
        }
        else{
            return true;
        }
    }

}
