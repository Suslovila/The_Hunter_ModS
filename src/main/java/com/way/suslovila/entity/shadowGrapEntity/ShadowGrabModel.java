package com.way.suslovila.entity.shadowGrapEntity;

import com.way.suslovila.MysticalCreatures;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

import javax.annotation.Nullable;

public class ShadowGrabModel extends AnimatedTickingGeoModel<ShadowGrabEntity> {

    @Override
    public ResourceLocation getModelLocation(ShadowGrabEntity object) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "geo/shadowgrab.geo.json");
    }
    @Override
    public ResourceLocation getTextureLocation(ShadowGrabEntity object) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/shadowgrab.png");
    }
//I do not care about animations, shadowGrap has no animation, but to prevent errors:
    @Override
    public ResourceLocation getAnimationFileLocation(ShadowGrabEntity animatable) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "animations/shadowgrab.animation.json");
    }
    @Override
    public void setLivingAnimations(ShadowGrabEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);



        }
    }

