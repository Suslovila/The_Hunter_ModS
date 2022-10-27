package com.way.suslovila.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class HellishFlames extends MobEffect {
    protected HellishFlames(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }
    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);
        if (!pLivingEntity.level.isClientSide()){
            pLivingEntity.setSecondsOnFire(1);
            if (pLivingEntity.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                pLivingEntity.removeEffect(MobEffects.FIRE_RESISTANCE);
            }
        }
    }
    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
