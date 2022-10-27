package com.way.suslovila.simplybackpacks.data;

import com.way.suslovila.MysticalCreatures;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemModels extends ItemModelProvider {

    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, MysticalCreatures.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        registerBackpacks();
    }

    private void registerBackpacks() {
        simpleItem(MysticalCreatures.COMMONBACKPACK.get());

    }

    private void simpleItem(Item item) {
        String name = ForgeRegistries.ITEMS.getKey(item).getPath();
        singleTexture(name, mcLoc("item/handheld"), "layer0", modLoc("item/" + name));
    }
}
