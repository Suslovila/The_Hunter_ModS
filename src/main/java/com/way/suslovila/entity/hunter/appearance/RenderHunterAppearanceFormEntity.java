package com.way.suslovila.entity.hunter.appearance;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.way.suslovila.MysticalCreatures;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;


public class RenderHunterAppearanceFormEntity extends GeoProjectilesRenderer<HunterAppearanceFormEntity> {
    public RenderHunterAppearanceFormEntity(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HunterAppearanceFormModel());
        this.shadowRadius = 1.5f;
        this.shadowStrength = 10.0f;
    }

    @Override
    public ResourceLocation getTextureLocation(HunterAppearanceFormEntity instance) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/hunter/hunterteleportform.png");
    }

    @Override
    public RenderType getRenderType(HunterAppearanceFormEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(0.82F, 0.82F, 0.82F);
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
