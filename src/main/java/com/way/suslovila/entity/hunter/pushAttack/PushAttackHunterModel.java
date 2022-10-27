package com.way.suslovila.entity.hunter.pushAttack;

import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.savedData.IsTheVictim.ClientVictimDataBoolean;
import com.way.suslovila.savedData.clientSynch.ClientVictimData;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class PushAttackHunterModel extends AnimatedGeoModel<PushAttackHunter> {
    public float i = 0;
    @Override
    public ResourceLocation getModelLocation(PushAttackHunter object) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "geo/pushform.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(PushAttackHunter object) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/hunter/hunter.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(PushAttackHunter animatable) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "animations/hunterpushattack.animation.json");
    }

    @Override
    public void setLivingAnimations(PushAttackHunter entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        //System.out.println("Calling SuperMethod!!!");
        //IBone bone = this.getAnimationProcessor().getBone("hunter_teleport_form");
        //System.out.println("We have got the bone");
        //System.out.println(ClientVictimData.getVictim());
        //System.out.println(ClientVictimDataBoolean.getIsVictim());
        if ((ClientVictimData.getVictim() != null) && ClientVictimDataBoolean.getIsVictim()) {
//            System.out.println("Not null");
           // Player player = entity.level.getPlayerByUUID(ClientVictimData.getVictim());
//            System.out.println("Victim exists");
           // i = (i + 0.1f)%360;
           // entity.rotate60degrees(bone, i);


        }
    }
}

