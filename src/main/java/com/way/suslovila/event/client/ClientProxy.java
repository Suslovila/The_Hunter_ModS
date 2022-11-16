package com.way.suslovila.event.client;

import com.way.suslovila.event.ServerProxy;
import com.way.suslovila.item.RainyAuraTalisman.WaterShieldLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends ServerProxy {


    @Override
    public void init(final IEventBus modbus) {
        super.init(modbus);
        MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);


    }

    @Override
    public void onLateInit(final IEventBus modbus) {
        System.out.println(Minecraft.getInstance().getEntityRenderDispatcher().renderers.values().size());
        for (EntityRenderer<?> entityRenderer : Minecraft.getInstance().getEntityRenderDispatcher().renderers.values()) {
            if (entityRenderer instanceof LivingEntityRenderer) {
                LivingEntityRenderer livingRenderer = (LivingEntityRenderer) entityRenderer;
                livingRenderer.addLayer(new WaterShieldLayer(livingRenderer));
            }
        }
        for (EntityRenderer playerRenderer : Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap().values()) {
            System.out.println(Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap().values().size());
            if (playerRenderer instanceof LivingEntityRenderer) {
                LivingEntityRenderer livingRenderer = (LivingEntityRenderer) playerRenderer;
                livingRenderer.addLayer(new WaterShieldLayer(livingRenderer));
            }
        }
    }











}
