package com.way.suslovila.entity.EntityShadowMonster;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.way.suslovila.MysticalCreatures;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.layer.LayerGlowingAreasGeo;

public class RenderShadowMonster extends GeoEntityRenderer<ShadowMonsterEntity> {
    public RenderShadowMonster(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ShadowMonsterModel());
        this.shadowRadius = 10f;
        this.shadowStrength = 10.0f;
        this.addLayer(new LayerGlowingAreasGeo<ShadowMonsterEntity>(this, modelProvider::getTextureLocation, modelProvider::getModelLocation, RenderType::eyes));


    }

    @Override
    public ResourceLocation getTextureLocation(ShadowMonsterEntity instance) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/shadowmonster.png");
    }

    @Override
    public RenderType getRenderType(ShadowMonsterEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(9F, 9F, 9F);
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
