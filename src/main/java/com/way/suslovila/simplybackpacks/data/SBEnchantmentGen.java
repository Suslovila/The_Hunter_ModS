package com.way.suslovila.simplybackpacks.data;


import com.way.suslovila.MysticalCreatures;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;

public class SBEnchantmentGen extends TagsProvider<Enchantment> {


    protected SBEnchantmentGen(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, Registry.ENCHANTMENT, MysticalCreatures.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(MysticalCreatures.SOULBOUND).addOptional(new ResourceLocation("ensorcellation","soulbound"));
        this.tag(MysticalCreatures.SOULBOUND).addOptional(new ResourceLocation("tombstone","soulbound"));
    }

    @Override
    @Nonnull
    protected Path getPath(ResourceLocation id) {
        return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/enchantments/" + id.getPath() + ".json");
    }

    @Override
    @Nonnull
    public String getName() {
        return "Enchantment Tags";
    }
}
