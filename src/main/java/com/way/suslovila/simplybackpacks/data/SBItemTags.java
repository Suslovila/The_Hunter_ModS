package com.way.suslovila.simplybackpacks.data;


import com.way.suslovila.MysticalCreatures;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class SBItemTags extends ItemTagsProvider {
    public SBItemTags(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, MysticalCreatures.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tagShulkers();
        tagQuantumBags();
        tagKrates();


        TagOtherModsItem("mekanism", "personal_chest");
        TagOtherModsItem("mekanism", "personal_barrel");
        TagOtherModsItem("rftoolsstorage", "modular_storage");
        TagOtherModsItem("rftoolsstorage", "modular_storage");
        TagOtherModsItem("rftoolsstorage", "storage_module0");
        TagOtherModsItem("rftoolsstorage", "storage_module1");
        TagOtherModsItem("rftoolsstorage", "storage_module2");
        TagOtherModsItem("rftoolsstorage", "storage_module3");
        TagOtherModsItem("immersiveengineering", "crate");
        TagOtherModsItem("immersiveengineering", "reinforced_crate");
        TagOtherModsItem("industrialforegoing", "infinity_backpack");
        TagOtherModsItem("pneumaticcraft", "reinforced_chest");
        TagOtherModsItem("pneumaticcraft", "smart_chest");
        TagOtherModsItem("forcecraft", "force_pack");

        this.tag(MysticalCreatures.CURIOS_BACK).add(MysticalCreatures.COMMONBACKPACK.get());

    }

    private void tagKrates() {
        TagOtherModsItem("krate","krate_small");
        TagOtherModsItem("krate","krate_basic");
        TagOtherModsItem("krate","krate_big");
        TagOtherModsItem("krate","krate_large");
    }

    private void TagOtherModsItem(String modid, String item) {
        this.tag(MysticalCreatures.HOLDS_ITEMS).addOptional(new ResourceLocation(modid, item));
    }

    private void tagShulkers() {
        this.tag(MysticalCreatures.HOLDS_ITEMS).add(Items.SHULKER_BOX);
        this.tag(MysticalCreatures.HOLDS_ITEMS).add(Items.BLACK_SHULKER_BOX);
        this.tag(MysticalCreatures.HOLDS_ITEMS).add(Items.BLUE_SHULKER_BOX);
        this.tag(MysticalCreatures.HOLDS_ITEMS).add(Items.BROWN_SHULKER_BOX);
        this.tag(MysticalCreatures.HOLDS_ITEMS).add(Items.CYAN_SHULKER_BOX);
        this.tag(MysticalCreatures.HOLDS_ITEMS).add(Items.GRAY_SHULKER_BOX);
        this.tag(MysticalCreatures.HOLDS_ITEMS).add(Items.GREEN_SHULKER_BOX);
        this.tag(MysticalCreatures.HOLDS_ITEMS).add(Items.LIGHT_BLUE_SHULKER_BOX);
        this.tag(MysticalCreatures.HOLDS_ITEMS).add(Items.LIGHT_GRAY_SHULKER_BOX);
        this.tag(MysticalCreatures.HOLDS_ITEMS).add(Items.LIME_SHULKER_BOX);
        this.tag(MysticalCreatures.HOLDS_ITEMS).add(Items.MAGENTA_SHULKER_BOX);
        this.tag(MysticalCreatures.HOLDS_ITEMS).add(Items.ORANGE_SHULKER_BOX);
        this.tag(MysticalCreatures.HOLDS_ITEMS).add(Items.PINK_SHULKER_BOX);
        this.tag(MysticalCreatures.HOLDS_ITEMS).add(Items.PURPLE_SHULKER_BOX);
        this.tag(MysticalCreatures.HOLDS_ITEMS).add(Items.RED_SHULKER_BOX);
        this.tag(MysticalCreatures.HOLDS_ITEMS).add(Items.WHITE_SHULKER_BOX);
        this.tag(MysticalCreatures.HOLDS_ITEMS).add(Items.YELLOW_SHULKER_BOX);
    }

    private void tagQuantumBags() {
        TagOtherModsItem("quantumstorage", "quantum_bag_white");
        TagOtherModsItem("quantumstorage", "quantum_bag_orange");
        TagOtherModsItem("quantumstorage", "quantum_bag_magenta");
        TagOtherModsItem("quantumstorage", "quantum_bag_light_blue");
        TagOtherModsItem("quantumstorage", "quantum_bag_yellow");
        TagOtherModsItem("quantumstorage", "quantum_bag_lime");
        TagOtherModsItem("quantumstorage", "quantum_bag_pink");
        TagOtherModsItem("quantumstorage", "quantum_bag_gray");
        TagOtherModsItem("quantumstorage", "quantum_bag_light_gray");
        TagOtherModsItem("quantumstorage", "quantum_bag_cyan");
        TagOtherModsItem("quantumstorage", "quantum_bag_purple");
        TagOtherModsItem("quantumstorage", "quantum_bag_blue");
        TagOtherModsItem("quantumstorage", "quantum_bag_brown");
        TagOtherModsItem("quantumstorage", "quantum_bag_green");
        TagOtherModsItem("quantumstorage", "quantum_bag_red");
        TagOtherModsItem("quantumstorage", "quantum_bag_black");
    }

}