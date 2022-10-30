package com.way.suslovila.simplybackpacks.items;


import com.way.suslovila.MysticalCreatures;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.RegistryObject;

/**
 * Represents a singleton instance of a backpack. Sets up each backpack in its own way
 */
public enum Backpack {
    COMMON("Common", Rarity.COMMON, 18, 2, 9, "bag.png", 176, 150, 7, 67, MysticalCreatures.COMMONBACKPACK);


    public final Rarity rarity;
    public final int slots;

    public final ResourceLocation texture;
    public final int xSize;
    public final int ySize;
    //offset from left edge of texture, to left edge of first player inventory slot.
    public final int slotXOffset;
    //offset from left edge of texture, to left edge of first player inventory slot.
    public final int slotYOffset;
    public final int slotRows;
    public final int slotCols;
    public final String name;
    public final RegistryObject<Item> item;

    Backpack(String name, Rarity rarity, int slots, int rows, int cols, String location, int xSize, int ySize, int slotXOffset, int slotYOffset, RegistryObject<Item> itemIn) {
        this.name = name;
        this.rarity = rarity;
        this.slots = slots;
        this.slotRows = rows;
        this.slotCols = cols;
        this.texture = new ResourceLocation(MysticalCreatures.MOD_ID, "textures/gui/" + location);
        this.xSize = xSize;
        this.ySize = ySize;
        this.slotXOffset = slotXOffset;
        this.slotYOffset = slotYOffset;
        this.item = itemIn;
    }
}