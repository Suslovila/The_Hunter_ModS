package com.way.suslovila.event;

import com.way.suslovila.entity.shadowGrapEntity.ShadowGrabEntity;
import com.way.suslovila.item.RainyAuraTalisman.WaterShieldLayer;
import com.way.suslovila.particles.*;
import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.entity.ModEntityTypes;
import com.way.suslovila.entity.hunter.HunterEntity;

import com.way.suslovila.entity.trap.TrapEntity;
import com.way.suslovila.savedData.IsTheVictim.MessagesBoolean;
import com.way.suslovila.savedData.arrow.MessagesForArrow;
import com.way.suslovila.savedData.clientSynch.Messages;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = MysticalCreatures.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEventsAll {

    @SubscribeEvent
    public static void registerModifierSerializers(@Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().registerAll();
    }
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.HUNTER.get(), HunterEntity.setAttributes());
        event.put(ModEntityTypes.TRAP.get(), TrapEntity.setAttributes());
        event.put(ModEntityTypes.SHADOW_GRAB.get(), ShadowGrabEntity.setAttributes());

    }
    @SubscribeEvent
    public static void registerParticleFactories(final ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(ModParticles.HEAD_BLACK_PARTICLES.get(),
                HeadBlackParticles.Provider::new);
        Minecraft.getInstance().particleEngine.register(ModParticles.TEST_BLACK_PARTICLES.get(),
                TESTBlackHeadParticles.Provider::new);
      Minecraft.getInstance().particleEngine.register(ModParticles.DISSOLATION_PARTICLES.get(),
                DissolationParticles.Provider::new);
        Minecraft.getInstance().particleEngine.register(ModParticles.DISSOLATION_LIGHTNING_PARTICLES.get(),
                DissolationLightningParticles.Provider::new);
    }
    public static void init(FMLCommonSetupEvent event) {
        Messages.register();
        MessagesBoolean.register();
        MessagesForArrow.register();
    }

}
