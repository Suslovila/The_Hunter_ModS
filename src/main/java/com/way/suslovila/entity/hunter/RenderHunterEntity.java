package com.way.suslovila.entity.hunter;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.way.suslovila.MysticalCreatures;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.layer.LayerGlowingAreasGeo;
import software.bernie.geckolib3.renderers.texture.AutoGlowingTexture;

import java.util.function.Function;

public class RenderHunterEntity extends GeoEntityRenderer<HunterEntity> {
 
    public RenderHunterEntity(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HunterModel());
        this.shadowRadius = 1.5f;

  this.addLayer(new LayerGlowingAreasGeo<HunterEntity>(this, modelProvider::getTextureLocation, modelProvider::getModelLocation, RenderType::eyes));
    }

    @Override
    public ResourceLocation getTextureLocation(HunterEntity instance) {
        return new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/hunter/hunter.png");
    }



    @Override
    public RenderType getRenderType(HunterEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(0.82F, 0.82F, 0.82F);
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }




}
