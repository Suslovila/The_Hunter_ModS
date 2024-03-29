package com.way.suslovila.item.mixin;

import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(HopperBlockEntity.class)
public interface AccessorHopperBlockEntity {
    @Invoker("canPlaceItemInContainer")
    static boolean botania_canInsert(Container to, ItemStack stack, int slot, Direction direction) {
        throw new IllegalStateException("");
    }

    @Invoker("canMergeItems")
    static boolean botania_canMerge(ItemStack a, ItemStack b) {
        throw new IllegalStateException("");
    }
}
