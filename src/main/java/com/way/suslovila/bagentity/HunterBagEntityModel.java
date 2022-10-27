package com.way.suslovila.bagentity;

import com.way.suslovila.MysticalCreatures;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HunterBagEntityModel extends AnimatedGeoModel<HunterBagEntity> {
    @Override
    public ResourceLocation getModelLocation(HunterBagEntity object) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "geo/hunterbag.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(HunterBagEntity object) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/hunterbag.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(HunterBagEntity animatable) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "animations/hunterbag.animation.json");
    }
}