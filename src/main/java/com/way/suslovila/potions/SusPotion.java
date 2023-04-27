package com.way.suslovila.potions;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SusPotion extends Potion {
//    @Nullable
//    private final String name;
//    private final ImmutableList<MobEffectInstance> effects;
//
//    /**
//     * Attempts to find a Potion using a name. The name will be parsed as a namespaced identifier which will be used to
//     * lookup the potion in the potion registry.
//     * @return The potion that was found in the registry.
//     * @param pName The name of the potion to search for. This name will be parsed as a namespaced identifier.
//     */
//    public static Potion byName(String pName) {
//        return Registry.POTION.get(ResourceLocation.tryParse(pName));
//    }
//
//    public SusPotion(MobEffectInstance... pEffects) {
//        this((String)null, pEffects);
//    }
//
//    public SusPotion(@Nullable String pName, ArrayList<MobEffectInstance> effectInstances) {
//        this.name = pName;
//        this.effects = effectInstances;
//    }
//
//    /**
//     * Gets the prefixed potion name. This is often used to create a localization key for items like the tipped arrows or
//     * bottled potion.
//     * @return The prefixed potion name.
//     * @param pPrefix The prefix to add on to the base name.
//     */
//    public String getName(String pPrefix) {
//        return pPrefix + (this.name == null ? Registry.POTION.getKey(this).getPath() : this.name);
//    }
//
//    /**
//     * Gets the base effects applied by the potion.
//     * @return The effects applied by the potion.
//     */
//    public List<MobEffectInstance> getEffects() {
//        return this.effects;
//    }
//
//    /**
//     * Checks if the potion contains any instant effects such as instant health or instant damage.
//     * @return Whether or not the potion contained an instant effect.
//     */
//    public boolean hasInstantEffects() {
//        if (!this.effects.isEmpty()) {
//            for(MobEffectInstance mobeffectinstance : this.effects) {
//                if (mobeffectinstance.getEffect().isInstantenous()) {
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }
}
