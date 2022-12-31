package com.way.suslovila.entity.shadowgardenentity;

import com.way.suslovila.MysticalCreatures;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class ShadowGardenModel extends AnimatedGeoModel<ShadowGardenEntity> {

    @Override
    public ResourceLocation getModelLocation(ShadowGardenEntity object) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "geo/shadowgarden.geo.json");
    }
    @Override
    public ResourceLocation getTextureLocation(ShadowGardenEntity object) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/shadowgarden.png");
    }
//I do not care about animations, shadowGrap has no animation, but to prevent errors:
    @Override
    public ResourceLocation getAnimationFileLocation(ShadowGardenEntity animatable) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "animations/shadowgarden.animation.json");
    }
    @Override
    public void setLivingAnimations(ShadowGardenEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);

        }
    }

