package com.way.suslovila.entity.shadowGrapEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.way.suslovila.MysticalCreatures;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderShadowGrab extends GeoEntityRenderer<ShadowGrabEntity> {
    public RenderShadowGrab(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ShadowGrabModel());
        this.shadowRadius = 1f;
        this.shadowStrength = 10.0f;

    }

    @Override
    public ResourceLocation getTextureLocation(ShadowGrabEntity instance) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/shadowgarden.png");
    }

    @Override
    public RenderType getRenderType(ShadowGrabEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(1F, 1F, 1F);
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
