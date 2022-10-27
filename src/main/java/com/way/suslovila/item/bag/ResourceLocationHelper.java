package com.way.suslovila.item.bag;

import net.minecraft.resources.ResourceLocation;

public class ResourceLocationHelper {
    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(LibMisc.MOD_ID, path);
    }
}