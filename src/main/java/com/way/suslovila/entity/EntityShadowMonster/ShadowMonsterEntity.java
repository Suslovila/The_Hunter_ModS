package com.way.suslovila.entity.EntityShadowMonster;

import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.entity.ShadowCreature;
import com.way.suslovila.entity.hunter.HunterEntity;
import com.way.suslovila.particles.TailBlackParticles;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
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
import java.util.*;


public class ShadowMonsterEntity extends ShadowCreature implements IAnimatable, IAnimationTickable {
    @Nullable
    private UUID ownerUUID;
    //  private int tickTimer = 0;
    //private HashMap<Vec3, ArrayList<Object>> cordsForShadowsAroundHand = new HashMap<>();
    public static final int attackTime = 32;
    public static final int lifeTime = 70;
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
        if (isDeadOrDying()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("shadowmonster.animation.waiting", true));
            return PlayState.CONTINUE;
        }
        if (getEntityData().get(ISREADYTOCATCH) > attackTime) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("shadowmonster.animation.bite", false));
            return PlayState.CONTINUE;
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("shadowmonster.animation.waiting", true));
            return PlayState.CONTINUE;
        }

        // event.getController().markNeedsReload();

    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController controller3 = new AnimationController(this, "controller",
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
    public int tickTimer() {
        return tickCount;
    }

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

    public int getIsReadyToCatch() {
        return getEntityData().get(ISREADYTOCATCH);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if(getIsReadyToCatch() == attackTime + 1)
            super.refreshDimensions();
        if (!level.isClientSide()) {
            //this.checkInsideBlocks();
            System.out.println("IS ready to catch: " + getIsReadyToCatch());

            String owner = getEntityData().get(OWNER);
            if (owner.equals("") || (((ServerLevel) level).getEntity(UUID.fromString(owner))) == null || !(((ServerLevel) level).getEntity(UUID.fromString(owner))).isAlive() || !Objects.equals(((HunterEntity) (((ServerLevel) level).getEntity(UUID.fromString(owner)))).getActualTask(), "shadowMonster")) {
                if(getEntityData().get(ISREADYTOCATCH) > lifeTime+3) {
                    for (int i = 0; i < 400; i++) {
                        ((ServerLevel) this.level).sendParticles(new TailBlackParticles.TailParticleData(random.nextDouble(0.03D, 0.6D), random.nextInt(30, 60)),
                                this.getX() + random.nextDouble(-7, 7), this.getY() + random.nextDouble(20), this.getZ() + random.nextDouble(-7, 7), 5, random.nextDouble(-0.2, 0.2),
                                random.nextDouble(-0.2, 0.2), random.nextDouble(-0.2, 0.2), random.nextDouble(-0.2, 0.2));
                    }
                }
                this.discard();

            } else {
                getLookControl().setLookAt(((ServerLevel) level).getEntity(UUID.fromString(owner)));
                if (getEntityData().get(ISREADYTOCATCH)-10 == attackTime) {
                    List<Entity> entities = level.getEntities(this, this.getBoundingBox());
                    for(int i = 0; i < entities.size(); i++){
                        Entity entity = entities.get(i);
                        if(!(entity instanceof ShadowCreature)){
                        DamageSource damageSourceP = new DamageSource(MysticalCreatures.MOD_ID + "pressure");
                        entity.hurt(damageSourceP, 150);
                    }
                    }
                }
            }
            this.getEntityData().set(ISREADYTOCATCH, getEntityData().get(ISREADYTOCATCH) + 1);
            spawnShadowParticles(32,20, 3, 1, 2.5,0.3D, 0.4D,8,8,0,2,8,8);
        }
    }
    @Override
    protected void doPush(Entity pEntity) {
    }
    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
            return false;
    }
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }
    public EntityDimensions getDimensions(Pose pPose) {
        return getIsReadyToCatch() < attackTime ? EntityDimensions.scalable(0.5F, 0.5F) : super.getDimensions(pPose).scale(this.getScale());
    }
}
