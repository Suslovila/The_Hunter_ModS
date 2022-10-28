package com.way.suslovila.particles;

import com.mojang.serialization.Codec;
import com.way.suslovila.MysticalCreatures;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = MysticalCreatures.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)

public class ParticleHandler {
    public static final DeferredRegister<ParticleType<?>> REG = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MysticalCreatures.MOD_ID);
//    public static final RegistryObject<ParticleType<NewHeadBlackParticles.HeadParticleData>> HEAD_PARTICLE = REG.register("head_black_particles", () -> new ParticleType<NewHeadBlackParticles.HeadParticleData>(false, NewHeadBlackParticles.HeadParticleData.DESERIALIZER) {
//        @Override
//        public Codec<NewHeadBlackParticles.HeadParticleData> codec() {
//            return NewHeadBlackParticles.HeadParticleData.CODEC(HEAD_PARTICLE.get());
//        }
//    });

    public static final RegistryObject<ParticleType<TailBlackParticles.TailParticleData>> TAIL_PARTICLE = REG.register("tail_black_particles", () -> new ParticleType<TailBlackParticles.TailParticleData>(false, TailBlackParticles.TailParticleData.DESERIALIZER) {

            @Override
        public Codec<TailBlackParticles.TailParticleData> codec() {
            return TailBlackParticles.TailParticleData.CODEC(TAIL_PARTICLE.get());
        }
    });


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerParticles(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(ParticleHandler.TAIL_PARTICLE.get(), TailBlackParticles.Provider::new);
//        Minecraft.getInstance().particleEngine.register(ParticleHandler.HEAD_PARTICLE.get(), NewHeadBlackParticles.Provider::new);

    }
        public static void register(IEventBus eventBus) {
            REG.register(eventBus);
        }

}
