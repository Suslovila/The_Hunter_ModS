package com.way.suslovila.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class PhoenixFeathering extends MobEffect {
    public PhoenixFeathering(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }
    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);
//        System.out.println(pLivingEntity.getBlockX());
//        System.out.println(pLivingEntity.getBlockY());
//        System.out.println(pLivingEntity.getBlockZ());
        if (!pLivingEntity.level.isClientSide() && pLivingEntity.isOnFire()) {
            if (pAmplifier == 0) {
                pLivingEntity.heal(0.05F);
            }
            if(pAmplifier >= 1){
                pLivingEntity.heal(0.1F);
            }
        }


    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
