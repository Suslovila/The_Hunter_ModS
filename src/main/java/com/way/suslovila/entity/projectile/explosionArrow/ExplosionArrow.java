package com.way.suslovila.entity.projectile.explosionArrow;

import com.way.suslovila.entity.hunter.HunterEntity;
import com.way.suslovila.particles.DissolationParticles;
import com.way.suslovila.particles.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Random;

public class ExplosionArrow extends AbstractArrow implements IAnimatable {
    private AnimationFactory factory = new AnimationFactory(this);
    public static Random random = new Random();


    public ExplosionArrow(EntityType<? extends AbstractArrow> p_36721_, Level p_36722_) {
        super(p_36721_, p_36722_);
    }

    private static final EntityDataAccessor<Float> XCoord = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> YCoord = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ZCoord = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.FLOAT);



    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }


    @Override
    public void registerControllers(AnimationData data) {

    }
    public void killed(ServerLevel pLevel, LivingEntity pKilledEntity) {
        System.out.println("KILLED");
    }
    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
@Override
    protected void onHit(HitResult pResult) {

    System.out.println("Just hit");

    if(!level.isClientSide) {
        this.discard();

    }
        }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(XCoord, 0f);
        getEntityData().define(ZCoord, 0f);
        getEntityData().define(YCoord, 0f);
    }
    public float getXCoordToAim(){
        return getEntityData().get(XCoord);
    }
    public float getYCoordToAim(){
        return getEntityData().get(YCoord);
    }
    public float getZCoordToAim(){
        return getEntityData().get(ZCoord);
    }
    public void setXCoordToAim(float coord){
        getEntityData().set(XCoord, coord);
    }
    public void setYCoordToAim(float coord){
        getEntityData().set(YCoord, coord);
    }
    public void setZCoordToAim(float coord){getEntityData().set(ZCoord, coord);}

}


