package com.way.suslovila;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

import java.util.List;
@SuppressWarnings("unchecked")
public class SuslovilaProjectileRender<T extends Entity & IAnimatable> extends GeoProjectilesRenderer {
    public SuslovilaProjectileRender(EntityRendererProvider.Context renderManager, AnimatedGeoModel modelProvider) {
        super(renderManager, modelProvider);
    }

//    protected final List<GeoLayerRenderer<T>> layerRenderers = Lists.newArrayList();
//
//    public SuslovilaProjectileRender(EntityRendererProvider.Context renderManager, AnimatedGeoModel<T> modelProvider) {
//        super(renderManager, modelProvider);
//    }
//
//    @Override
//    public void render(Entity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn,
//                       MultiBufferSource bufferIn, int packedLightIn) {
//        super.render(entityIn,entityYaw, partialTicks, matrixStackIn,  bufferIn, packedLightIn);
//        if (!entity.isSpectator()) {
//            for (GeoLayerRenderer<T> layerRenderer : this.layerRenderers) {
//                this.renderLayer(stack, bufferIn, packedLightIn, entity, limbSwing, limbSwingAmount, partialTicks, f7,
//                        netHeadYaw, headPitch, bufferIn, layerRenderer);
//            }
//        }
//
//    }
//    public final boolean addLayer(GeoLayerRenderer<T> layer) {
//        return this.layerRenderers.add(layer);
//    }
}
