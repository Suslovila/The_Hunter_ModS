package com.way.suslovila.entity.bag;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.way.suslovila.MysticalCreatures;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class RenderHunterBagEntity extends GeoProjectilesRenderer<HunterBagEntity> {
    public RenderHunterBagEntity(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HunterBagEntityModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(HunterBagEntity instance) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/hunterbag.png");
    }

    @Override
    public RenderType getRenderType(HunterBagEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(1.0F, 1.0F, 1.0F);
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}