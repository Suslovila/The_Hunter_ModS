package com.way.suslovila.potions;

import com.way.suslovila.MysticalCreatures;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPotions {
public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, MysticalCreatures.MOD_ID);
//public static final RegistryObject<Potion> Ender_Seal = POTIONS.register("ender_seal", ()->new Potion(new MobEffectInstance()));




public static void register(IEventBus bus){
    POTIONS.register(bus);
}
}
