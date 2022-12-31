package com.way.suslovila.entity.trap.hunter;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.entity.trap.TrapEntity;
import com.way.suslovila.entity.trap.TrapEntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.layer.LayerGlowingAreasGeo;

public class RenderTrapEntity extends GeoEntityRenderer<TrapEntity> {
    public RenderTrapEntity(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TrapEntityModel());
        this.shadowRadius = 0.01f;
        this.addLayer(new LayerGlowingAreasGeo<TrapEntity>(this, modelProvider::getTextureLocation, modelProvider::getModelLocation, RenderType::eyes));
    }

    @Override
    public ResourceLocation getTextureLocation(TrapEntity instance) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/trap/trap.png");
    }

    @Override
    public RenderType getRenderType(TrapEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(1.25F, 1.25F, 1.25F);
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
