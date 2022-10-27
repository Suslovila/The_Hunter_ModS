package com.way.suslovila.entity.hunter.teleport;

import com.way.suslovila.MysticalCreatures;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class HunterTeleportFormModel extends AnimatedGeoModel<HunterTeleportFormEntity> {
    @Override
    public ResourceLocation getModelLocation(HunterTeleportFormEntity object) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "geo/hunterteleportform.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(HunterTeleportFormEntity object) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/hunter/hunterteleportform.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(HunterTeleportFormEntity animatable) {
            return new ResourceLocation(MysticalCreatures.MOD_ID, "animations/hunterteleportform.animation.json");
    }

}
