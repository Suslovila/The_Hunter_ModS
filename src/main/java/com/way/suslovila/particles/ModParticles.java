package com.way.suslovila.particles;

import com.way.suslovila.MysticalCreatures;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MysticalCreatures.MOD_ID);

    public static final RegistryObject<SimpleParticleType> HEAD_BLACK_PARTICLES =
            PARTICLE_TYPES.register("head_black_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> TEST_BLACK_PARTICLES =
            PARTICLE_TYPES.register("test_black_particles", () -> new SimpleParticleType(true));
//   public static final RegistryObject<SimpleParticleType> TAIL_BLACK_PARTICLES =
//           PARTICLE_TYPES.register("tail_black_particles", () -> new SimpleParticleType(true));
//public static final RegistryObject<BlockParticleOption> DISSOLATION_PARTICLES =
//        PARTICLE_TYPES.register("dissolation_particles", () -> new BlockParticleOption());

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}