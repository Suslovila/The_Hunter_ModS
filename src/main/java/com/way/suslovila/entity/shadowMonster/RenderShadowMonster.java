package com.way.suslovila.entity.shadowMonster;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.way.suslovila.MysticalCreatures;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderShadowMonster extends GeoEntityRenderer<ShadowMonsterEntity> {
    public RenderShadowMonster(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ShadowMonsterModel());
        this.shadowRadius = 2.5f;
        this.shadowStrength = 10.0f;

    }

    @Override
    public ResourceLocation getTextureLocation(ShadowMonsterEntity instance) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/shadowgarden.png");
    }

    @Override
    public RenderType getRenderType(ShadowMonsterEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(1F, 1F, 1F);
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
