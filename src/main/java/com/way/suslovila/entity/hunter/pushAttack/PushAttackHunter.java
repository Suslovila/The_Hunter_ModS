package com.way.suslovila.entity.hunter.pushAttack;

import com.way.suslovila.entity.ModEntityTypes;
import com.way.suslovila.entity.hunter.HunterEntity;
import com.way.suslovila.particles.ModParticles;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.core.processor.IBone;

import java.util.Random;

public class PushAttackHunter extends Projectile implements IAnimatable, IAnimationTickable {
private int timer = 39;
Random random = new Random();
int chis = 3;
    private static final EntityDataAccessor<Boolean> HasPushed = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.BOOLEAN);

    private AnimationFactory factory = new AnimationFactory(this);

    public PushAttackHunter(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
        this.noPhysics = true;
    }


    @Override
    public void baseTick() {
        super.baseTick();
        if (!level.isClientSide()) {



            }
        }




    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {

        if (getHasPushed()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("hunter.animation.AfterPush2", true));
        return PlayState.CONTINUE;
    }
else{
            event.getController().setAnimation(new AnimationBuilder().addAnimation("hunter.animation.flight", false));
            return PlayState.CONTINUE;

        }
    }


    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);

    }


    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
    }

    public void lookAtVictim(EntityAnchorArgument.Anchor pAnchor, Vec3 pTarget, IBone bone) {
        Vec3 vec3 = pAnchor.apply(this);
  double dx = pTarget.x - vec3.x;
    double dz = pTarget.z - vec3.z;
    //bone.setRotationY(Mth.wrapDegrees((float)(Math.atan2(dx,dz))));
////    bone.setRotationY(Mth.wrapDegrees((float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F));
        this.setYRot(Mth.wrapDegrees(-(float)(Mth.atan2(dz, dx) * (double)(180F / (float)Math.PI))));
  //  this.setYHeadRot((float)(Mth.atan2(dz, dx) * (double)(180F / (float)Math.PI)));
      // this.yRotO = this.getYRot();

    }
    @Override
    protected void defineSynchedData() {
        getEntityData().define(HasPushed, false);

    }
    public boolean getHasPushed(){
        return getEntityData().get(HasPushed);
    }
    public void setHasPushed(boolean pushed){
        getEntityData().set(HasPushed,pushed);
    }
    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if (!level.isClientSide()) {
            setHasPushed(true);
            Entity entity = pResult.getEntity();
            double d5 = entity.getX() - this.getX();
            double d7 =entity.getEyeY() - this.getY();
            double d9 = entity.getZ() - this.getZ();
            double d11 = 0.25;
            pResult.getEntity().setDeltaMovement(entity.getDeltaMovement().add(d5 * d11, d7 * d11, d9 * d11));
        }
    }
    @Override
    protected void onHitBlock(BlockHitResult p_37258_) {
        this.discard();
    }

    @Override
    public int tickTimer() {
        return tickCount;
    }
}

