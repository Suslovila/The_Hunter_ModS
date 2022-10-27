package com.way.suslovila.entity.projectile.speedArrow;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.way.suslovila.MysticalCreatures;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class RenderSpeedArrow extends GeoProjectilesRenderer<SpeedArrow> {
    public RenderSpeedArrow(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SpeedArrowModel());
        this.shadowRadius = 0.1f;
    }

    @Override
    public ResourceLocation getTextureLocation(SpeedArrow instance) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/speedarrow.png");
    }

    @Override
    public RenderType getRenderType(SpeedArrow animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(0.82F, 0.82F, 0.82F);
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
