package com.way.suslovila.entity.shadowGrabEntity;

import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.entity.ShadowCreature;
import com.way.suslovila.entity.hunter.HunterEntity;
import com.way.suslovila.particles.TailBlackParticles;
import com.way.suslovila.savedData.SaveVictim;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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


public class ShadowGrabEntity extends ShadowCreature implements IAnimatable, IAnimationTickable {
    //todo: сделать так, заставить игрока не разворачиваться
    @Nullable
    private UUID ownerUUID;
    private int tickTimer = 0;
    public static final int prepareTime = 20;

    private HashMap<Vec3, ArrayList<Object>> cordsForShadowsAroundHand = new HashMap<>();

    private static final EntityDataAccessor<Integer> ISREADYTOCATCH = SynchedEntityData.defineId(ShadowGrabEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<String> OWNER = SynchedEntityData.defineId(ShadowGrabEntity.class, EntityDataSerializers.STRING);

    private AnimationFactory factory = new AnimationFactory(this);

    public ShadowGrabEntity(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }


    public static AttributeSupplier setAttributes() {

        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 15.0D)
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
//        if(Minecraft.getInstance().isPaused()) {
//            event.getController().markNeedsReload();
//            return PlayState.STOP;
//
//        }
        if (isDeadOrDying()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("shadowgarden.animation.waiting", true));
            return PlayState.CONTINUE;
        }
        if (getEntityData().get(ISREADYTOCATCH) > 30) {
            if (getIsReadyToCatch() < 57) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.shadowgarden.catch2", false));
                return PlayState.CONTINUE;
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.shadowgarden.caught", true));
                return PlayState.CONTINUE;
            }
        } else {
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
            if (deathTime > 18)
                discard();
            System.out.println("IS ready to catch: " + getIsReadyToCatch());

            if (getIsReadyToCatch() > prepareTime && !hasCaughtPlayer()) kill();
            if(!SaveVictim.get(level).getVictim().equals("novictim")) {
                if (hasCaughtPlayer() && !Objects.equals(SaveVictim.get(level).getVictim(), getPassengers().get(0).getStringUUID())) kill();
            }
            else
                kill();
            if (this.getBrightness() > maxLight && tickCount % 20 == 0) this.hurt(DamageSource.DRY_OUT, 1);

            String owner = getEntityData().get(OWNER);

            if (owner.equals("")) this.kill();
            else {
                if ((((ServerLevel) level).getEntity(UUID.fromString(owner))) == null) kill();
                else {
                    if (!(((ServerLevel) level).getEntity(UUID.fromString(owner))).isAlive() || ((HunterEntity) (((ServerLevel) level).getEntity(UUID.fromString(owner)))).isGrabbing())
                        kill();

                    else {
                        getLookControl().setLookAt(((ServerLevel) level).getEntity(UUID.fromString(owner)));
                        System.out.println("Is player catched: " + hasCaughtPlayer());
                    }
                }

            }


            this.getEntityData().set(ISREADYTOCATCH, getEntityData().get(ISREADYTOCATCH) + 1);
            if (hasCaughtPlayer()) {
                Entity entity = this.getPassengers().get(0);
                if (entity.isShiftKeyDown()) entity.setShiftKeyDown(false);
                ((Player) entity).setYRot(this.getYRot());
                entity.setYHeadRot(getYHeadRot());
                if (getEntityData().get(ISREADYTOCATCH) % 20 == 0) {
                    Player player = (Player) this.getPassengers().get(0);
                    int count1 = 0;
                    for (int i = 0; i < 4; i++) {
                        ItemStack armorItem = ((Player) this.getPassengers().get(0)).getInventory().armor.get(i);
                        if (armorItem.isEmpty()) {
                            count1 += 1;
                        } else {
                            if (((ArmorItem) armorItem.getItem()).getDefense() == 0) {
                                count1 += 1;
                            }
                        }
                        if (armorItem.isDamageableItem()) {
                            if (armorItem.getDamageValue() + 2 > armorItem.getMaxDamage()) {
                                if (i == 0) {
                                    armorItem.hurtAndBreak(2, player, (p_35997_) -> {
                                        p_35997_.broadcastBreakEvent(EquipmentSlot.FEET);
                                    });
                                }
                                if (i == 1) {
                                    armorItem.hurtAndBreak(2, player, (p_35997_) -> {
                                        p_35997_.broadcastBreakEvent(EquipmentSlot.LEGS);
                                    });
                                }
                                if (i == 2) {
                                    armorItem.hurtAndBreak(2, player, (p_35997_) -> {
                                        p_35997_.broadcastBreakEvent(EquipmentSlot.CHEST);
                                    });
                                }
                                if (i == 3) {
                                    armorItem.hurtAndBreak(2, player, (p_35997_) -> {
                                        p_35997_.broadcastBreakEvent(EquipmentSlot.HEAD);
                                    });
                                }


                            } else {
                                armorItem.hurt(2, random, (ServerPlayer) player);
                            }
                        }

                    }
                    if (count1 == 4) {
                        DamageSource damageSourceP = new DamageSource(MysticalCreatures.MOD_ID + "pressure");
                        damageSourceP.bypassArmor();
                        player.hurt(damageSourceP, 1);
                    }
                    tickTimer = 0;
                }
            }
//            for(int hl = 0; hl < 2; hl++){
//                if (cordsForShadowsAroundHand.size() < 10) {
//                    double radius = random.nextDouble(0.3, 1);
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
//                    arrayList.add(random.nextDouble(0.1D, 0.2D));
//                    arrayList.add(timer);
//                    cordsForShadowsAroundHand.put(new Vec3(this.position().x + random.nextDouble(-1.5, 1.5), this.position().y + random.nextDouble( 1.5), this.position().z + random.nextDouble(-1.5, 1.5)), arrayList);
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
//                        double newRadius = random.nextDouble(0.3, 1);
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
//                    if (random.nextInt(200) == 37) {
//                        cordsForShadowsAroundHand.remove(dotInSpace);
//                    }
//                }
//            }
            spawnShadowParticles(10, 15, 2, 0.3, 1,0.1D, 0.2D, 1.5, 1.5, 0, 1.5, 1.5, 1.5);
        } else {
            if (this.isDeadOrDying()) {
                for (int i = 0; i < 70; i++) {
                    this.level.addParticle(new TailBlackParticles.TailParticleData(random.nextDouble(0.03D, 0.1D), random.nextInt(10, 70)),
                            getX() + random.nextDouble(-1.5, 1.5), getY() + random.nextDouble(-1.5, 1.5) + 1.5, getZ() + random.nextDouble(-1.5, 1.5),
                            random.nextDouble(-0.2d, 0.2d), random.nextDouble(-0.2d, 0.2d), random.nextDouble(-0.2d, 0.2d));
                }
                //this.discard();
            }
        }
    }

    @Override
    public void playerTouch(Player player) {
        super.playerTouch(player);

//            float distance = this.distanceTo(player);
                if (getEntityData().get(ISREADYTOCATCH) > prepareTime && !hasCaughtPlayer() && !isDeadOrDying()) {
                    player.setShiftKeyDown(false);
                    player.startRiding(this, false);

        }
    }


    @Override
    public boolean canRiderInteract() {
        return false;
    }

    @Override
    protected void doPush(Entity pEntity) {
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    public boolean hasCaughtPlayer() {
        return this.hasExactlyOnePlayerPassenger();
    }

    @Override
    public double getMyRidingOffset() {
        return 3D;
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.43D;
    }

    public boolean shouldRiderFaceForward(Player player) {
        return false;
    }

    @Override
    public boolean showVehicleHealth() {
        return false;
    }

    public void die(DamageSource damageSource) {
        super.die(damageSource);
        //if(!level.isClientSide()) this.discard();
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (getIsReadyToCatch() <= prepareTime)
            return false;
        else
            return super.hurt(pSource, pAmount);
    }
}
