package com.way.suslovila.entity.hunter.teleport;

import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.entity.hunter.appearance.HunterAppearanceFormEntity;
import com.way.suslovila.savedData.clientSynch.ClientVictimData;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

import javax.annotation.Nullable;

public class HunterTeleportFormModel extends AnimatedTickingGeoModel<HunterTeleportFormEntity> {
    @Override
    public ResourceLocation getModelLocation(HunterTeleportFormEntity object) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "geo/hunter.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(HunterTeleportFormEntity object) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/hunter/hunter.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(HunterTeleportFormEntity animatable) {
            return new ResourceLocation(MysticalCreatures.MOD_ID, "animations/hunter.animation.json");
    }
    public void setLivingAnimations(HunterTeleportFormEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if ((ClientVictimData.getVictim() != null) && entity.level.getPlayerByUUID(ClientVictimData.getVictim()) != null) {
            entity.lookAtVictim(EntityAnchorArgument.Anchor.FEET, entity.level.getPlayerByUUID(ClientVictimData.getVictim()).getEyePosition(), this.getAnimationProcessor().getBone("head2"));
        }
    }
}
