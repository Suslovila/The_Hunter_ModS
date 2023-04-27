package com.way.suslovila.effects;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.way.suslovila.MysticalCreatures;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.renderers.geo.layer.AbstractLayerGeo;
import software.bernie.geckolib3.renderers.texture.AutoGlowingTexture;

import java.util.function.Function;

public class LayerWaterShieldGeo<T extends Mob & IAnimatable> extends AbstractLayerGeo<T> {
    private static final ResourceLocation WATER_SHIELD = new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/water_shield.png");


    protected final Function<ResourceLocation, RenderType> funcGetEmissiveRenderType;

    public LayerWaterShieldGeo(GeoEntityRenderer<T> renderer, Function<T, ResourceLocation> funcGetCurrentTexture,
                               Function<T, ResourceLocation> funcGetCurrentModel,
                               Function<ResourceLocation, RenderType> funcGetEmissiveRenderType) {
        super(renderer, funcGetCurrentTexture, funcGetCurrentModel);
        this.funcGetEmissiveRenderType = funcGetEmissiveRenderType;
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entityLivingBaseIn,
                       float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw,
                       float headPitch) {
        //this.reRenderCurrentModelInRenderer(entityLivingBaseIn, partialTicks, matrixStackIn, bufferIn, packedLightIn,
                //this.funcGetEmissiveRenderType.apply(AutoGlowingTexture.get(this.funcGetCurrentTexture.apply(entityLivingBaseIn))));


        float f = (float) entityLivingBaseIn.tickCount + partialTicks;
        //GeoModelProvider<T> entitymodelProvider = this.getEntityModel();
//        GeoModel geoModel = entitymodelProvider.getModel(entitymodelProvider.getModelLocation(entityLivingBaseIn));
//        entitymodel.(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks, netHeadYaw, headPitch);
//        this.getParentModel().copyPropertiesTo(entitymodel);
//        VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.energySwirl(this.func_225633_a_(), this.func_225634_a_(f), f * 0.01F));
//        entitymodel.setupAnim(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
//        entitymodel.(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 0.1F, 0.1F, 1F, 1.0F);
        //VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.energySwirl(this.func_225633_a_(), this.func_225634_a_(f), f * 0.01F));
        //IGeoRenderer<T> renderer = this.getRenderer();
        RenderType type = RenderType.energySwirl(this.func_225633_a_(), this.func_225634_a_(f), f * 0.01F);
        this.reRenderCurrentModelInRenderer(entityLivingBaseIn, partialTicks, matrixStackIn, bufferIn, packedLightIn, type);

    }
    protected float func_225634_a_(float p_225634_1_) {
        return p_225634_1_ * 0.02F;
    }

    protected ResourceLocation func_225633_a_() {
        return WATER_SHIELD;
    }
}
