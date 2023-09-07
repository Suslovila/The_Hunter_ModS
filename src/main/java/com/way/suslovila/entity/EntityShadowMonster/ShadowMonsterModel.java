package com.way.suslovila.entity.EntityShadowMonster;

import com.way.suslovila.MysticalCreatures;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

import javax.annotation.Nullable;

public class ShadowMonsterModel extends AnimatedTickingGeoModel<ShadowMonsterEntity> {

    @Override
    public ResourceLocation getModelLocation(ShadowMonsterEntity object) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "geo/shadowmonster.geo.json");
    }
    @Override
    public ResourceLocation getTextureLocation(ShadowMonsterEntity object) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/shadowmonster.png");
    }
//I do not care about animations, shadowGrap has no animation, but to prevent errors:
    @Override
    public ResourceLocation getAnimationFileLocation(ShadowMonsterEntity animatable) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "animations/shadowmonster.animation.json");
    }
    @Override
    public void setLivingAnimations(ShadowMonsterEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);



        }
    }

