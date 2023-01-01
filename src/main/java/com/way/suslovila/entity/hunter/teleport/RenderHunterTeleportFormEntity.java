package com.way.suslovila.entity.hunter.teleport;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.way.suslovila.MysticalCreatures;

import com.way.suslovila.entity.hunter.HunterEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;
import software.bernie.geckolib3.renderers.geo.layer.LayerGlowingAreasGeo;


public class RenderHunterTeleportFormEntity extends GeoEntityRenderer<HunterTeleportFormEntity> {
    public RenderHunterTeleportFormEntity(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HunterTeleportFormModel());
        this.shadowRadius = 1.5f;
        this.shadowStrength = 10.0f;
        this.addLayer(new LayerGlowingAreasGeo<HunterTeleportFormEntity>(this, modelProvider::getTextureLocation, modelProvider::getModelLocation, RenderType::eyes));

    }

    @Override
    public ResourceLocation getTextureLocation(HunterTeleportFormEntity instance) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/hunter/hunterteleportform.png");
    }

    @Override
    public RenderType getRenderType(HunterTeleportFormEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(0.82F, 0.82F, 0.82F);
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
