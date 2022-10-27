package com.way.suslovila.entity.trap.fire;

import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.entity.trap.TrapEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class TrapEntityModel extends AnimatedGeoModel<TrapEntity> {

    @Override
    public ResourceLocation getModelLocation(TrapEntity object) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "geo/trap.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(TrapEntity object) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/trap/trap.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(TrapEntity animatable) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "animations/trap.animation.json");
    }
}
