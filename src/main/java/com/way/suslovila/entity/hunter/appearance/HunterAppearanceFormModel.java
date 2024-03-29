package com.way.suslovila.entity.hunter.appearance;

import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.entity.hunter.HunterEntity;
import com.way.suslovila.entity.hunter.teleport.HunterTeleportFormEntity;
import com.way.suslovila.savedData.IsTheVictim.ClientVictimDataBoolean;
import com.way.suslovila.savedData.SaveVictim;
import com.way.suslovila.savedData.clientSynch.ClientVictimData;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class HunterAppearanceFormModel extends AnimatedTickingGeoModel<HunterAppearanceFormEntity> {
    @Override
    public ResourceLocation getModelLocation(HunterAppearanceFormEntity object) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "geo/hunter.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(HunterAppearanceFormEntity object) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/hunter/hunter.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(HunterAppearanceFormEntity animatable) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "animations/hunter.animation.json");
    }

    @Override
    public void setLivingAnimations(HunterAppearanceFormEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        //System.out.println("Calling SuperMethod!!!");
        //IBone bone = this.getAnimationProcessor().getBone("hunter_teleport_form");
        //System.out.println("We have got the bone");
        //System.out.println(ClientVictimData.getVictim());
        //System.out.println(ClientVictimDataBoolean.getIsVictim());
        if ((ClientVictimData.getVictim() != null) && entity.level.getPlayerByUUID(ClientVictimData.getVictim()) != null) {
//            System.out.println("Not null");
           // Player player = entity.level.getPlayerByUUID(ClientVictimData.getVictim());
//            System.out.println("Victim exists");
           // i = (i + 0.1f)%360;
           // entity.rotate60degrees(bone, i);


           entity.lookAtVictim(EntityAnchorArgument.Anchor.FEET, entity.level.getPlayerByUUID(ClientVictimData.getVictim()).getEyePosition(), this.getAnimationProcessor().getBone("head2"));
        }
    }
}

