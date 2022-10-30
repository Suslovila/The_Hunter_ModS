package com.way.suslovila.entity.hunter;

import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.entity.hunter.appearance.HunterAppearanceFormEntity;
import com.way.suslovila.savedData.IsTheVictim.ClientVictimDataBoolean;
import com.way.suslovila.savedData.clientSynch.ClientVictimData;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

import javax.annotation.Nullable;

public class HunterModel extends AnimatedTickingGeoModel<HunterEntity> {
    @Override
    public ResourceLocation getModelLocation(HunterEntity object) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "geo/hunter.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(HunterEntity object) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/hunter/hunter.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(HunterEntity animatable) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "animations/hunter.animation.json");
    }
    @Override
    public void setLivingAnimations(HunterEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        //System.out.println("Calling SuperMethod!!!");
        //IBone bone = this.getAnimationProcessor().getBone("hunter_teleport_form");
        //System.out.println("We have got the bone");
        //System.out.println(ClientVictimData.getVictim());
        //System.out.println(ClientVictimDataBoolean.getIsVictim());
        if ((ClientVictimData.getVictim() != null) && !((HunterEntity)entity).getVulnarble() && entity.level.getPlayerByUUID(ClientVictimData.getVictim()) != null) {
//            System.out.println("Not null");
                // Player player = entity.level.getPlayerByUUID(ClientVictimData.getVictim());
//            System.out.println("Victim exists");
                // i = (i + 0.1f)%360;
                // entity.rotate60degrees(bone, i);
                Vec3 vec = new Vec3(entity.getXCoordToAim(), entity.getYCoordToAim(), entity.getZCoordToAim());
                if (entity.isAlive() && !Minecraft.getInstance().isPaused()) {
                    ((HunterEntity) entity).lookAtVictim(EntityAnchorArgument.Anchor.FEET, entity.level.getPlayerByUUID(ClientVictimData.getVictim()).getEyePosition(), this.getAnimationProcessor().getBone("hunter_teleport_form"), this.getAnimationProcessor().getBone("head2"));
                    if (((HunterEntity) entity).getShouldRotateHandsForShooting()) {
                        ((HunterEntity) entity).aimBowAtVictim(EntityAnchorArgument.Anchor.FEET, vec, this.getAnimationProcessor().getBone("leftArm"), this.getAnimationProcessor().getBone("rightArm"), this.getAnimationProcessor().getBone("palm"));
                    }
                }
        }
    }
}
