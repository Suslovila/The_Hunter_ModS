package com.way.suslovila.effects;


import com.way.suslovila.effects.rainyaura.RainyAuraCapProvider;
import com.way.suslovila.effects.rainyaura.RainyAuraStorage;
import com.way.suslovila.savedData.clientSynch.ClientRainyAuraData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;

import java.util.*;


public class WaterShieldEffect extends MobEffect {
    Random random = new Random();


    protected WaterShieldEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);
        if (pLivingEntity.isOnFire()) pLivingEntity.clearFire();
        //pLivingEntity.setRemainingFireTicks(0);




        if (pLivingEntity.hasEffect(ModEffects.HELLISH_FLAMES.get()))
            pLivingEntity.removeEffect(ModEffects.HELLISH_FLAMES.get());

        if (pLivingEntity.level.isClientSide()) {

            double xDelta = random.nextDouble(pLivingEntity.getBbWidth() / 2, pLivingEntity.getBbWidth() / 2 + 0.5D);
            double zDelta = random.nextDouble(pLivingEntity.getBbWidth() / 2, pLivingEntity.getBbWidth() / 2 + 0.5D);
            if (random.nextBoolean()) {
                xDelta = -xDelta;
            }
            if (random.nextBoolean()) {
                zDelta = -zDelta;
            }

            double yHeigh = pLivingEntity.getBbHeight();
            double yDelta = random.nextDouble(yHeigh);
            pLivingEntity.level.addParticle(ParticleTypes.UNDERWATER, pLivingEntity.getX() + xDelta, pLivingEntity.getY() + yDelta, pLivingEntity.getZ() + zDelta, 0, 0, 0);
        }
        else{
//            extinguishAll(pLivingEntity.getBoundingBox(), pLivingEntity);
//            BlockPos posUnderEntity = new BlockPos(pLivingEntity.getBlockX(), pLivingEntity.getBlockY()-1, pLivingEntity.getBlockZ());
//            if(pLivingEntity.level.getBlockState(posUnderEntity).getMaterial() == Material.LAVA){
//                pLivingEntity.level.setBlockAndUpdate(posUnderEntity, Blocks.OBSIDIAN.defaultBlockState());
//                pLivingEntity.level.playSound(null, posUnderEntity.getX(), posUnderEntity.getY(), posUnderEntity.getZ(), SoundEvents.LAVA_EXTINGUISH, SoundSource.AMBIENT, 0.2F, 1.0F);
//                for(int i = 0; i< 20; i++) {
//                    ((ServerLevel) pLivingEntity.level).sendParticles(ParticleTypes.LARGE_SMOKE, posUnderEntity.getX(), posUnderEntity.getY()+1, posUnderEntity.getZ(), 1,  random.nextDouble(-0.2, 0.2), random.nextDouble(0, 0.2), random.nextDouble(-0.2, 0.2), random.nextDouble(-0.2, 0.2));
//                }
//            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
    private void extinguishAll(AABB pArea, LivingEntity entity) {
        int i = Mth.floor(pArea.minX);
        int j = Mth.floor(pArea.minY);
        int k = Mth.floor(pArea.minZ);
        int l = Mth.floor(pArea.maxX);
        int i1 = Mth.floor(pArea.maxY);
        int j1 = Mth.floor(pArea.maxZ);
        for(int k1 = i; k1 <= l; ++k1) {
            for(int l1 = j; l1 <= i1; ++l1) {
                for(int i2 = k; i2 <= j1; ++i2) {
                    BlockPos blockpos = new BlockPos(k1, l1, i2);
                    BlockState blockstate = entity.level.getBlockState(blockpos);
                    if (!blockstate.isAir()){
                     if(blockstate.getMaterial() == Material.LAVA){
                         entity.level.setBlockAndUpdate(blockpos, Blocks.AIR.defaultBlockState());
                            entity.level.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.LAVA_EXTINGUISH, SoundSource.AMBIENT, 0.2F, 1.0F);
                        }
                     if(blockstate.getMaterial() == Material.FIRE){
                         entity.level.setBlockAndUpdate(blockpos, Blocks.AIR.defaultBlockState());
                         entity.level.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.AMBIENT, 0.2F, 1.0F);
                     }
                    }
                }
            }
        }
    }
}
