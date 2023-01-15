package com.way.suslovila.effects;

import com.way.suslovila.MysticalCreatures;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOD_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MysticalCreatures.MOD_ID);
    public static final RegistryObject<MobEffect> ENDER_SEAL = MOD_EFFECTS.register("ender_seal", ()->new EnderSealEffect(MobEffectCategory.HARMFUL,34342342));
    public static final RegistryObject<MobEffect> ENDER_FRACTURE = MOD_EFFECTS.register("ender_fracture", ()-> new EnderFractureEffect(MobEffectCategory.BENEFICIAL, 44545454));
    public static final RegistryObject<MobEffect> PHOENIX_FEATHRING = MOD_EFFECTS.register("phoenix_feathering", ()-> new PhoenixFeathering(MobEffectCategory.BENEFICIAL, 32334976));
    public static final RegistryObject<MobEffect> WATER_SHIELD = MOD_EFFECTS.register("water_shield_effect", ()-> new WaterShieldEffect(MobEffectCategory.BENEFICIAL, 8842423));
    public static final RegistryObject<MobEffect> HELLISH_FLAMES = MOD_EFFECTS.register("hellish_flames", ()-> new HellishFlames(MobEffectCategory.NEUTRAL, 8842423));


    public static void register(IEventBus bus){
    MOD_EFFECTS.register(bus);
}
}
