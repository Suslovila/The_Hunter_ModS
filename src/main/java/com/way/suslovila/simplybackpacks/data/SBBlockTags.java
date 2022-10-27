package com.way.suslovila.simplybackpacks.data;

import com.way.suslovila.MysticalCreatures;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class SBBlockTags extends BlockTagsProvider {
    public SBBlockTags(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, MysticalCreatures.MOD_ID, existingFileHelper);
    }
}
