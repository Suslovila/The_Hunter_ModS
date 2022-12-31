package com.way.suslovila.entity.shadowMonster;

import com.way.suslovila.entity.hunter.HunterEntity;
import com.way.suslovila.particles.TailBlackParticles;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

import static com.way.suslovila.entity.hunter.HunterEntity.maxLight;


public class ShadowMonsterEntity extends PathfinderMob implements IAnimatable, IAnimationTickable {
    //todo: сделать так, заставить игрока не разворачиваться
    @Nullable
    private UUID ownerUUID;
    private int tickTimer = 0;
    private static final EntityDataAccessor<Integer> ISREADYTOCATCH = SynchedEntityData.defineId(ShadowMonsterEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<String> OWNER = SynchedEntityData.defineId(ShadowMonsterEntity.class, EntityDataSerializers.STRING);

    private AnimationFactory factory = new AnimationFactory(this);

    public ShadowMonsterEntity(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }


    public static AttributeSupplier setAttributes() {

        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.ATTACK_DAMAGE, 0f)
                .add(Attributes.ATTACK_SPEED, 0f)
                .add(Attributes.MOVEMENT_SPEED, 0.3f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 10D)
                .build();
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(ISREADYTOCATCH, 0);
        getEntityData().define(OWNER, "");

    }

    private <E extends IAnimatable> PlayState predicateForShadowGrab(AnimationEvent<E> event) {
        event.getController().setAnimationSpeed(0.7);
            if(getEntityData().get(ISREADYTOCATCH) > 30) {
            if(getIsReadyToCatch() < 57) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.shadowgarden.catch2", false));
                return PlayState.CONTINUE;
            }
            else{
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.shadowgarden.caught", true));
                return PlayState.CONTINUE;
            }
        }else{
            event.getController().setAnimation(new AnimationBuilder().addAnimation("shadowgarden.animation.waiting", true));
            return PlayState.CONTINUE;
        }

       // event.getController().markNeedsReload();

    }
    @Override
    public void registerControllers(AnimationData data) {
        AnimationController controller3 = new AnimationController(this, "controllerforshadowgrab1",
                0, this::predicateForShadowGrab);
        data.addAnimationController(controller3);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.hasUUID("Owner")) {
            this.ownerUUID = pCompound.getUUID("Owner");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }
    }
    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }


    @Override
    public int tickTimer() {return tickCount;}
    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }
    public boolean rideableUnderWater() {
        return true;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
    public boolean isOnFire() {
        return false;
    }
    public int getIsReadyToCatch(){
        return getEntityData().get(ISREADYTOCATCH);
    }

    @Override
public void baseTick(){
        super.baseTick();
        if(!level.isClientSide()) {
            System.out.println("IS ready to catch: " + getIsReadyToCatch());

if(getIsReadyToCatch() > 31 && !hasCaughtPlayer())kill();

if (this.getBrightness() > maxLight && tickCount % 20 == 0) this.hurt(DamageSource.DRY_OUT, 1);

            String owner = getEntityData().get(OWNER);

            if(owner.equals(""))this.kill();
            else{
                if((((ServerLevel) level).getEntity(UUID.fromString(owner)))== null)kill();
                else {
                    if (!(((ServerLevel) level).getEntity(UUID.fromString(owner))).isAlive() || !Objects.equals(((HunterEntity) (((ServerLevel) level).getEntity(UUID.fromString(owner)))).getActualTask(), "shadowMonster"))
                        kill();

                    else {
                        getLookControl().setLookAt(((ServerLevel) level).getEntity(UUID.fromString(owner)));
                    }
                }

            }



            this.getEntityData().set(ISREADYTOCATCH, getEntityData().get(ISREADYTOCATCH) + 1);
        }
        else{
            if(this.isDeadOrDying()){
                for(int i = 0; i < 70; i++){
                    this.level.addParticle(new TailBlackParticles.TailParticleData(random.nextDouble(0.03D, 0.1D), random.nextInt(10, 70)),
                            getX() + random.nextDouble(-1.5, 1.5), getY()+random.nextDouble(-1.5, 1.5) + 1.5, getZ() + random.nextDouble(-1.5, 1.5),
                            random.nextDouble(-0.2d, 0.2d), random.nextDouble(-0.2d, 0.2d), random.nextDouble(-0.2d, 0.2d));
                }
                  //this.discard();
            }
        }
}

    @Override
    protected void doPush(Entity pEntity) {
    }
    public boolean hasCaughtPlayer() {
        return this.hasExactlyOnePlayerPassenger();
    }
    public void die(DamageSource damageSource){
        super.die(damageSource);
    }
}
