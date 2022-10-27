package com.way.suslovila.item.bag;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ItemBackedInventory extends SimpleContainer {
    private static final String TAG_ITEMS = "Items";
    private final ItemStack stack;

    public ItemBackedInventory(ItemStack stack, int expectedSize) {
        super(expectedSize);
        this.stack = stack;

        ListTag lst = ItemNBTHelper.getList(stack, TAG_ITEMS, Tag.TAG_COMPOUND, false);
        int i = 0;
        for (; i < expectedSize && i < lst.size(); i++) {
            setItem(i, ItemStack.of(lst.getCompound(i)));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return !stack.isEmpty();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        ListTag list = new ListTag();
        for (int i = 0; i < getContainerSize(); i++) {
            list.add(getItem(i).save(new CompoundTag()));
        }
        ItemNBTHelper.setList(stack, TAG_ITEMS, list);
    }
}