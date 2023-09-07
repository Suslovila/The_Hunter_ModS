package com.way.suslovila.item.bag;

import com.way.suslovila.MysticalCreatures;
import net.minecraft.resources.ResourceLocation;

public class ResourceLocationHelper {
    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, path);
    }
}