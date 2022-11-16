package com.way.suslovila.item.RainyAuraTalisman;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.capability.EntityCapabilityProvider;
import com.way.suslovila.capability.EntityCapabilityStorage;
import com.way.suslovila.savedData.clientSynch.ClientWaterShieldData;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class WaterShieldLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T,M> {
    private static final ResourceLocation WATER_SHIELD = new ResourceLocation(MysticalCreatures.MOD_ID, "textures/entity/water_shield.png");

    public WaterShieldLayer(RenderLayerParent<T, M> entityRendererIn) {
        super(entityRendererIn);
    }
@Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
    if (entitylivingbaseIn.getCapability(EntityCapabilityProvider.BLOCKS).isPresent() && entitylivingbaseIn.getCapability(EntityCapabilityProvider.BLOCKS).map(EntityCapabilityStorage::getHasWaterShield).get()) {

//    System.out.println(ClientWaterShieldData.getShield());
//        if (ClientWaterShieldData.getShield()) {

            float f = (float) entitylivingbaseIn.tickCount + partialTicks;
            EntityModel<T> entitymodel = this.getParentModel();
            entitymodel.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, netHeadYaw, headPitch);
            this.getParentModel().copyPropertiesTo(entitymodel);
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.energySwirl(this.func_225633_a_(), this.func_225634_a_(f), f * 0.01F));
            entitymodel.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            entitymodel.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 0.1F, 0.1F, 1F, 1.0F);

    }
}

    protected float func_225634_a_(float p_225634_1_) {
        return p_225634_1_ * 0.02F;
    }

    protected ResourceLocation func_225633_a_() {
        return WATER_SHIELD;
    }
}
