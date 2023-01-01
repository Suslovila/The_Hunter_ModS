package com.way.suslovila.entity.projectile.ghostArrow;

import com.way.suslovila.MysticalCreatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

import javax.annotation.Nullable;

public class GhostArrowModel extends AnimatedTickingGeoModel<GhostArrow> {

    @Override
    public ResourceLocation getModelLocation(GhostArrow object) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "geo/speedarrow.geo.json");
    }
    @Override
    public ResourceLocation getTextureLocation(GhostArrow object) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/ghostarrow.png");
    }
//I do not care about animations, arrow has no animation, but to prevent errors:
    @Override
    public ResourceLocation getAnimationFileLocation(GhostArrow animatable) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "animations/speedarrow.animation.json");
    }
    @Override
    public void setLivingAnimations(GhostArrow arrow, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(arrow, uniqueID, customPredicate);
        if (arrow.getXCoordToAim() != 0) {
            double dx = arrow.getXCoordToAim() - arrow.getX();
            double dz = arrow.getZCoordToAim() - arrow.getZ();
            double xz = Math.sqrt(dx * dx + dz * dz);
            double dy = arrow.getYCoordToAim() - arrow.getY();
            arrow.setYRot(90f + Mth.wrapDegrees(-(float)(Mth.atan2(dz, dx) * (double)(180F / (float)Math.PI))));
            arrow.setXCoordToAim(0);
        }
    }
}
