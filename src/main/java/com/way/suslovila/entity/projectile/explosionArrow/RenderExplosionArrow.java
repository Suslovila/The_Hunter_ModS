package com.way.suslovila.entity.projectile.explosionArrow;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.SuslovilaProjectileRender;
import com.way.suslovila.entity.hunter.HunterEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.EnderEyesLayer;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;
import software.bernie.geckolib3.renderers.geo.layer.LayerGlowingAreasGeo;

public class RenderExplosionArrow extends GeoProjectilesRenderer<ExplosionArrow> {
    public RenderExplosionArrow(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ExplosionArrowModel());
        this.shadowRadius = 0.1f;

    }

    @Override
    public ResourceLocation getTextureLocation(ExplosionArrow instance) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/explosionarrow.png");
    }

    @Override
    public RenderType getRenderType(ExplosionArrow animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(0.82F, 0.82F, 0.82F);
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }

}
