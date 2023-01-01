package com.way.suslovila.entity.projectile.ghostArrow;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.way.suslovila.MysticalCreatures;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class RenderGhostArrow extends GeoProjectilesRenderer<GhostArrow> {
    public RenderGhostArrow(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GhostArrowModel());
        this.shadowRadius = 0.1f;
    }

    @Override
    public ResourceLocation getTextureLocation(GhostArrow instance) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/ghostarrow.png");
    }

    @Override
    public RenderType getRenderType(GhostArrow animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(0.82F, 0.82F, 0.82F);
        return RenderType.entityTranslucent(textureLocation);
    }
}
