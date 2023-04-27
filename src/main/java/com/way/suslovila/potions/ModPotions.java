package com.way.suslovila.potions;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.effects.ModEffects;
import com.way.suslovila.events.ModEvents;
import com.way.suslovila.item.ModItems;
import net.minecraft.sounds.Music;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.VanillaBrewingRecipe;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModPotions {
    public static int id = 0;
public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, MysticalCreatures.MOD_ID);
//public static final RegistryObject<Potion> Ender_Seal = POTIONS.register("ender_seal", ()->new Potion(new MobEffectInstance()));
public static final RegistryObject<Potion> WATER_SHIELD_POTION = POTIONS.register("water_shield_potion",
        () -> new Potion(new MobEffectInstance(ModEffects.WATER_SHIELD.get(), 9600, 0)));
public static HashMap<Potion, RegistryObject<Potion>> potions = new HashMap<>();

public static void registerVanillaPotions(){
    for(Potion potion : ForgeRegistries.POTIONS.getValues()){
        if(PotionBrewing.isBrewablePotion(potion)){
            ArrayList<MobEffectInstance> resultList = new ArrayList<>();
            for(MobEffectInstance effectInstance : potion.getEffects()) {
                resultList.add(new MobEffectInstance(effectInstance.getEffect(), 2*effectInstance.getDuration(), effectInstance.getAmplifier()+1, effectInstance.isAmbient(), effectInstance.isVisible(), effectInstance.showIcon()));
            }
            RegistryObject<Potion> myPotion = POTIONS.register(potion.getName("advanced" + id), () -> new Potion(potion.getName("Advanced"), resultList.toArray(new MobEffectInstance[]{})));
            //Potion myPotion = new Potion(potion.getName("Advanced "), resultList.toArray(new MobEffectInstance[]{}));
            potions.put(potion,myPotion);
            id++;
            //BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(potion, ModItems.STRANGE_BONE_ITEM.get(), myPotion.get()));
        }
    }
}

public static void register(IEventBus bus){
    POTIONS.register(bus);
    //Suppliers.memoize(() -> new Music(HUNT_THEME.get(), 14760, 14761, true));
    //BrewingRecipeRegistry.addRecipe(Suppliers.memoize(() -> new ModBrewingRecipe(Potions.AWKWARD, Items.HEART_OF_THE_SEA, ModPotions.WATER_SHIELD_POTION.get()));
}
    public static void addPotionRecipes() {
    //adding strange bone's recipe to all existing potions
        for(Potion potion : ForgeRegistries.POTIONS.getValues()){
            if(PotionBrewing.isBrewablePotion(potion)){
//                ArrayList<MobEffectInstance> resultList = new ArrayList<>();
//                for(MobEffectInstance effectInstance : potion.getEffects()) {
//                    resultList.add(new MobEffectInstance(effectInstance.getEffect(), effectInstance.getDuration(), effectInstance.getAmplifier()));
//                }
               //Potion myPotion = new Potion(potion.getName("Advanced"), resultList.toArray(new MobEffectInstance[]{})));
                //Potion myPotion = new Potion(potion.getName("Advanced "), resultList.toArray(new MobEffectInstance[]{}));
                //System.out.println("My potion is: " + resultList);
                if(potions.get(potion) != null)
                BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(potion, ModItems.STRANGE_BONE_ITEM.get(), potions.get(potion).get()));
            }
        }



        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(Potions.STRONG_TURTLE_MASTER, Items.HEART_OF_THE_SEA, ModPotions.WATER_SHIELD_POTION.get()));
    }
}
