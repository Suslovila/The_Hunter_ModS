package com.way.suslovila.entity.projectile.explosionArrow;

import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.savedData.clientSynch.ClientVictimData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class ExplosionArrowModel extends AnimatedGeoModel<ExplosionArrow> {

    @Override
    public ResourceLocation getModelLocation(ExplosionArrow object) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "geo/explosionarrow.geo.json");
    }
    @Override
    public ResourceLocation getTextureLocation(ExplosionArrow object) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/explosionarrow.png");
    }
//I do not care about animations, arrow has no animation, but to prevent errors:
    @Override
    public ResourceLocation getAnimationFileLocation(ExplosionArrow animatable) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "animations/hunterbag.animation.json");
    }
    @Override
    public void setLivingAnimations(ExplosionArrow arrow, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(arrow, uniqueID, customPredicate);
        //System.out.println("Calling SuperMethod!!!");
        //IBone bone = this.getAnimationProcessor().getBone("hunter_teleport_form");
        //System.out.println("We have got the bone");
        //System.out.println(ClientVictimData.getVictim());
        //System.out.println(ClientVictimDataBoolean.getIsVictim());
//        if (arrow.getXCoordToAim() != 0) {
////            System.out.println("Not null");
//            // Player player = entity.level.getPlayerByUUID(ClientVictimData.getVictim());
////            System.out.println("Victim exists");
//            // i = (i + 0.1f)%360;
//            // entity.rotate60degrees(bone, i);
//            double dx = arrow.getXCoordToAim() - arrow.getX();
//            double dz = arrow.getZCoordToAim() - arrow.getZ();
//            double xz = Math.sqrt(dx * dx + dz * dz);
//            double dy = arrow.getYCoordToAim() - arrow.getY();
//
//            arrow.setYRot(90f + Mth.wrapDegrees(-(float)(Mth.atan2(dz, dx) * (double)(180F / (float)Math.PI))));
////            bone.setRotationY(Mth.wrapDegrees((float) (Math.atan2(dx, dz))));
////            bone.setRotationX(Mth.wrapDegrees((float) (Math.atan2(dy, xz))));
//            arrow.setXCoordToAim(0);
//        }
    }
}
