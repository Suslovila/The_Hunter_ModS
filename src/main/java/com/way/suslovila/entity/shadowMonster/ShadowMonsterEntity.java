package com.way.suslovila.entity.shadowMonster;

import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.entity.ShadowCreature;
import com.way.suslovila.entity.hunter.HunterEntity;
import com.way.suslovila.particles.ModParticles;
import com.way.suslovila.particles.TailBlackParticles;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
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

import javax.annotation.Nullable;
import java.util.*;

import static com.way.suslovila.entity.hunter.HunterEntity.maxLight;


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
                    BlockPos pos = blockPosition();
                    for (int x = pos.getX() - 7; x < pos.getX() + 7; x++) {
                        for (int y = pos.getY(); y < pos.getY() + 10; y++) {
                            for (int z = pos.getZ() - 7; z < pos.getZ() + 7; z++) {
                                BlockPos checkPos = new BlockPos(x, y, z);
                                //Vec3 vec = new Vec3(checkPos.getX() - getX(), checkPos.getY() - getY(), checkPos.getZ() - getZ());
                                level.destroyBlock(checkPos, false, this);
                            }
                        }
                    }
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
//            for(int hl = 0; hl < 3; hl++){
//                if (cordsForShadowsAroundHand.size() < 35) {
//                    double radius = random.nextDouble(1.5, 3);
//                    int timer = 0;
//                    Vec3 lookVector = this.getViewVector(0);
//                    Vec3 lookVectorNormal = new Vec3(lookVector.x + random.nextDouble(-2, 2), 0, lookVector.z + random.nextDouble(-2, 2));
//                    Vec3 m = new Vec3(lookVectorNormal.z, 0, -lookVectorNormal.x);
//                    m = m.normalize();
//                    // Vec3 k = lookVectorNormal.cross(m);
//                    Vec3 k = new Vec3(0, -1, 0);
//                    //k = new Vec3(k.x, -Math.abs(k.y), k.z);
//                    ArrayList<Object> arrayList = new ArrayList<Object>();
//                    arrayList.add(radius);
//                    arrayList.add(m);
//                    arrayList.add(k);
//                    arrayList.add(random.nextDouble(0.3D, 0.4D));
//                    arrayList.add(timer);
//                    cordsForShadowsAroundHand.put(new Vec3(this.position().x + random.nextDouble(-8, 8), this.position().y + random.nextDouble(2), this.position().z + random.nextDouble(-8, 8)), arrayList);
//                }
//            }
//            HashMap<Vec3, ArrayList> map = (HashMap) cordsForShadowsAroundHand.clone();
//            Iterator<Vec3> iterator = map.keySet().iterator();
//            while (iterator.hasNext()) {
//                Vec3 dotInSpace = iterator.next();
//                ArrayList<Object> list = map.get(dotInSpace);
//                double radius = (double) list.get(0);
//                Vec3 m = ((Vec3) list.get(1)).scale(radius);
//                Vec3 k = ((Vec3) list.get(2)).scale(radius);
//                double particleSize = (double)list.get(3);
//                for (int h = 0; h < 15; h++) {
//                    int timer = (int) list.get(4);
//                    if (timer % 100 == 0 && timer != 0 && random.nextBoolean()) {
//                        cordsForShadowsAroundHand.remove(dotInSpace);
//                        double newRadius = random.nextDouble(1.5, 3);
//                        double chis = k.y*Math.cos(timer * Math.PI / 100);
//                        Vec3 newDotInSpace = new Vec3(dotInSpace.x, dotInSpace.y + chis/Math.abs(chis)*(newRadius + radius), dotInSpace.z);
//                        Vec3 newK = new Vec3(0, -k.y, 0);
//                        ArrayList<Object> newList = new ArrayList<>();
//                        newList.add(newRadius);
//                        newList.add(m.normalize());
//                        newList.add(newK.normalize());
//                        newList.add(particleSize);
//                        newList.add(timer+1);
//                        cordsForShadowsAroundHand.put(newDotInSpace, newList);
//                    }
//                    Vec3 a = m.scale(Math.sin(timer * Math.PI / 100)).add(k.scale(Math.cos(timer * Math.PI / 100)));
//                    list.remove(4);
//                    list.add(timer + 1);
//                    Vec3 endPosition = dotInSpace.add(a);
//                    ((ServerLevel) this.level).sendParticles(new TailBlackParticles.TailParticleData(random.nextDouble(particleSize-0.04*particleSize/0.3, particleSize), random.nextInt(13, 14)),
//                            endPosition.x, endPosition.y, endPosition.z, 1, 0,
//                            0, 0, 0);
//                    if (random.nextInt(300) == 37) {
//                        cordsForShadowsAroundHand.remove(dotInSpace);
//                    }
//                }
//            }
            spawnShadowParticles(35,15, 3, 1.5, 3,0.3D, 0.4D,8,8,0,2,8,8);
        }
    }
    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return pPose == Pose.SLEEPING ? SLEEPING_DIMENSIONS : super.getDimensions(pPose).scale(this.getScale());
    }
//@Override
//    public float getScale() {
//        return this.getIsReadyToCatch() <= attackTime ? 0.5F : 1.0F;
//    }
    @Override
    protected void doPush(Entity pEntity) {
    }
    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
            return false;
    }
}
