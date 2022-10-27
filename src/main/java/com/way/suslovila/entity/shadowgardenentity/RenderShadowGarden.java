package com.way.suslovila.entity.shadowgardenentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.entity.shadowGrapEntity.ShadowGrabModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class RenderShadowGarden extends GeoProjectilesRenderer<ShadowGardenEntity> {
    public RenderShadowGarden(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ShadowGardenModel());
        this.shadowRadius = 1f;
        this.shadowStrength = 10.0f;

    }

    @Override
    public ResourceLocation getTextureLocation(ShadowGardenEntity instance) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/shadowgarden.png");
    }

    @Override
    public RenderType getRenderType(ShadowGardenEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(1f, 1f, 1f);
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
