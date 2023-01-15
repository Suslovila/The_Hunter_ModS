package com.way.suslovila.entity.projectile.ghostArrow;

import com.way.suslovila.entity.hunter.HunterEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class GhostArrow extends AbstractArrow implements IAnimatable, IAnimationTickable {
    private AnimationFactory factory = new AnimationFactory(this);
    private static final EntityDataAccessor<Float> XCoord = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> YCoord = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ZCoord = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.FLOAT);

    public GhostArrow(EntityType<? extends AbstractArrow> p_36721_, Level p_36722_) {
        super(p_36721_, p_36722_);
        noPhysics = true;
        //setNoGravity(true);
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
        getEntityData().define(XCoord, 0f);
        getEntityData().define(ZCoord, 0f);
        getEntityData().define(YCoord, 0f);
    }

    public float getXCoordToAim() {
        return getEntityData().get(XCoord);
    }

    public float getYCoordToAim() {
        return getEntityData().get(YCoord);
    }

    public float getZCoordToAim() {
        return getEntityData().get(ZCoord);
    }

    public void setXCoordToAim(float coord) {
        getEntityData().set(XCoord, coord);
    }

    public void setYCoordToAim(float coord) {
        getEntityData().set(YCoord, coord);
    }

    public void setZCoordToAim(float coord) {
        getEntityData().set(ZCoord, coord);
    }

    @Override

    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        this.setDeltaMovement(this.getDeltaMovement().scale(0.01));
    }
    @Override
    public void playerTouch(Player player) {
        if(!level.isClientSide()){
            player.hurt(DamageSource.MAGIC, 2);
            this.discard();
        }
    }
}

