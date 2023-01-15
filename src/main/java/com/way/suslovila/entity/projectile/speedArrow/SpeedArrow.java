package com.way.suslovila.entity.projectile.speedArrow;

import com.way.suslovila.effects.ModEffects;
import com.way.suslovila.entity.hunter.HunterEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SpeedArrow extends AbstractArrow implements IAnimatable, IAnimationTickable {
    private AnimationFactory factory = new AnimationFactory(this);

    public static final Random random = new Random();
    private static final EntityDataAccessor<Integer> ID_EFFECT_COLOR = SynchedEntityData.defineId(SpeedArrow.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HIT_ENTITY = SynchedEntityData.defineId(SpeedArrow.class, EntityDataSerializers.BOOLEAN);


    private final static ArrayList<MobEffect> effectList  = new ArrayList<MobEffect>() {{
        add(MobEffects.BLINDNESS);
        add(MobEffects.DIG_SLOWDOWN);
        add(MobEffects.MOVEMENT_SLOWDOWN);
        add(ModEffects.ENDER_SEAL.get());
        add(MobEffects.WEAKNESS);
        add(MobEffects.HUNGER);
        add(MobEffects.CONFUSION);
        add(MobEffects.WITHER);
        add(MobEffects.CONFUSION);
        add(MobEffects.POISON);
        add(MobEffects.LEVITATION);
    }};
    public SpeedArrow(EntityType<? extends AbstractArrow> p_36721_, Level p_36722_) {
        super(p_36721_, p_36722_);
        this.getEntityData().set(ID_EFFECT_COLOR, random.nextInt(effectList.size()));
    }


    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController controller = new AnimationController(this, "controllerforarrow",
                4, this::predicateForShooting);
        data.addAnimationController(controller);

    }

    private <E extends IAnimatable> PlayState predicateForShooting(AnimationEvent<E> event) {

        event.getController().setAnimation(new AnimationBuilder().addAnimation("speedarrow.animation.idle2", true));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public int tickTimer() {
        return tickCount;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_EFFECT_COLOR, -1);
        this.entityData.define(HIT_ENTITY, false);
    }

    @Override
    protected void onHit(HitResult pResult) {
        // setNoGravity(false);
super.onHit(pResult);
//this.setDeltaMovement(this.getDeltaMovement().scale(0.01));
    }
    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        DamageSource damageSource = DamageSource.arrow(this, this);
        if(!getEntityData().get(HIT_ENTITY)) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.005));
            getEntityData().set(HIT_ENTITY, true);
        }
//        Entity entity = pResult.getEntity();
//        if (entity.hurt(damageSource, 1)) {
//            if (entity instanceof LivingEntity) {
//                    ((LivingEntity) entity).addEffect(new MobEffectInstance(effectList.get(getEntityData().get(ID_EFFECT_COLOR)), random.nextInt(100, 200), random.nextInt(2)));
//            }
     //   }
    }
    protected float getWaterInertia() {
        return 0.99F;
    }
    public void tick() {
        super.tick();
        if(level.isClientSide()){
            makeParticle(1);
        }
    }
    public int getColor() {
        return effectList.get(this.entityData.get(ID_EFFECT_COLOR)).getColor();
    }
    private void makeParticle(int pParticleCount) {
        int i = this.getColor();
        if (i != -1 && pParticleCount > 0) {
            double d0 = (double)(i >> 16 & 255) / 255.0D;
            double d1 = (double)(i >> 8 & 255) / 255.0D;
            double d2 = (double)(i >> 0 & 255) / 255.0D;

            for(int j = 0; j < pParticleCount; ++j) {
                this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
            }

        }
    }
    @Override
    protected void doPostHurtEffects(LivingEntity pLiving) {
        super.doPostHurtEffects(pLiving);
        pLiving.addEffect(new MobEffectInstance(effectList.get(getEntityData().get(ID_EFFECT_COLOR)), random.nextInt(100, 200), random.nextInt(2)));

    }
}

