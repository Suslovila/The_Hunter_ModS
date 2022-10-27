package com.way.suslovila.entity.hunter.pushAttack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.way.suslovila.MysticalCreatures;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;


public class RenderPushAttackHunter extends GeoProjectilesRenderer<PushAttackHunter> {
    public RenderPushAttackHunter(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PushAttackHunterModel());
        this.shadowRadius = 4f;
        this.shadowStrength = 10.0f;
    }

    @Override
    public ResourceLocation getTextureLocation(PushAttackHunter instance) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/hunter/hunter.png");
    }

    @Override
    public RenderType getRenderType(PushAttackHunter animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(0.82F, 0.82F, 0.82F);
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
