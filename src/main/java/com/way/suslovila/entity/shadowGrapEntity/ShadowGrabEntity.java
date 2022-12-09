package com.way.suslovila.entity.shadowGrapEntity;

import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.entity.hunter.HunterEntity;
import com.way.suslovila.entity.trap.TrapEntity;
import com.way.suslovila.particles.ModParticles;
import com.way.suslovila.particles.TailBlackParticles;
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
import net.minecraft.world.phys.AABB;
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
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.way.suslovila.entity.hunter.HunterEntity.maxLight;
import static com.way.suslovila.entity.projectile.explosionArrow.ExplosionArrow.random;


public class ShadowGrabEntity extends Mob implements IAnimatable, IAnimationTickable {
    //todo: сделать так, чтобы игрок не тепался, чтобы анимация не зависала при esc, заставить игрока не разворачиваться, боится огня
    @Nullable
    private UUID ownerUUID;
    private int tickTimer = 0;
    private static final EntityDataAccessor<Integer> ISREADYTOCATCH = SynchedEntityData.defineId(ShadowGrabEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<String> OWNER = SynchedEntityData.defineId(ShadowGrabEntity.class, EntityDataSerializers.STRING);

    private AnimationFactory factory = new AnimationFactory(this);

    public ShadowGrabEntity(EntityType<? extends Mob> p_21368_, Level p_21369_) {
        super(p_21368_, p_21369_);
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
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.shadowgarden.catch", true));
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
    public int getIsReadyToCatch(){
        return getEntityData().get(ISREADYTOCATCH);
    }

    @Override
public void baseTick(){
        super.baseTick();
        if(!level.isClientSide()) {
if(getIsReadyToCatch() > 31 && !hasCaughtPlayer())kill();

if (this.getBrightness() > maxLight && tickCount % 20 == 0) this.hurt(DamageSource.DRY_OUT, 1);

            String owner = getEntityData().get(OWNER);

            if(owner.equals(""))this.discard();
            else{
                if((((ServerLevel) level).getEntity(UUID.fromString(owner)))== null)kill();
                else {
                    if (!(((ServerLevel) level).getEntity(UUID.fromString(owner))).isAlive() || ((HunterEntity) (((ServerLevel) level).getEntity(UUID.fromString(owner)))).getVulnarble())
                        kill();

                    else {
                        getLookControl().setLookAt(((ServerLevel) level).getEntity(UUID.fromString(owner)));
                    }
                }

            }



            this.getEntityData().set(ISREADYTOCATCH, getEntityData().get(ISREADYTOCATCH) + 1);
            if (hasCaughtPlayer()) {
                Entity entity = this.getPassengers().get(0);
                if (entity.isShiftKeyDown()) entity.setShiftKeyDown(false);
                entity.setYBodyRot(yBodyRot);
                entity.setYHeadRot(getYHeadRot());

                if(this.navigation.isDone()){
                    List<Entity> entities = level.getEntities(this, new AABB(this.getX() + 4, this.getY() + 4, this.getZ() + 4,this.getX() - 4, this.getY() - 4, this.getZ() - 4));
                    boolean isHunterNear = false;
                    for(int i = 0; i < entities.size(); i++){
                        if (entities.get(i) instanceof HunterEntity){
                            isHunterNear = true;
                        }
                    }
                    if(!isHunterNear){
                        if (tickTimer % 10 == 0) {
                            Player player = (Player)this.getPassengers().get(0);
                            int count1 = 0;
                            for (int i = 0; i < 4; i++) {
                                ItemStack armorItem = ((Player) this.getPassengers().get(0)).getInventory().armor.get(i);
                                if(armorItem.isEmpty()){
                                    count1 += 1;
                                }
                                else{
                                    if(((ArmorItem)armorItem.getItem()).getDefense() == 0){
                                        count1 += 1;
                                    }
                                }
                                if (armorItem.isDamageableItem()) {
                                    if (armorItem.getDamageValue() + 10 > armorItem.getMaxDamage()) {
                                        if (i == 0) {
                                            armorItem.hurtAndBreak(10, player, (p_35997_) -> {
                                                p_35997_.broadcastBreakEvent(EquipmentSlot.FEET);
                                            });
                                        }
                                        if(i == 1){
                                            armorItem.hurtAndBreak(10, player, (p_35997_) -> {
                                                p_35997_.broadcastBreakEvent(EquipmentSlot.LEGS);
                                            });
                                        }
                                        if(i == 2){
                                            armorItem.hurtAndBreak(10, player, (p_35997_) -> {
                                                p_35997_.broadcastBreakEvent(EquipmentSlot.CHEST);
                                            });
                                        }
                                        if(i == 3){
                                            armorItem.hurtAndBreak(10, player, (p_35997_) -> {
                                                p_35997_.broadcastBreakEvent(EquipmentSlot.HEAD);
                                            });
                                        }


                                    } else {
                                        armorItem.hurt(10, random, (ServerPlayer) player);
                                    }
                                }

                            }
                            if(count1 == 4){
                                DamageSource damageSourceP = new DamageSource(MysticalCreatures.MOD_ID + "pressure");
                                damageSourceP.bypassArmor();
                                player.hurt(damageSourceP, 1);
                            }
                            tickTimer = 0;
                        }
                    }
                }
            }
        }
        else{
            if(this.isDeadOrDying()){
                for(int i = 0; i < 1000; i++){
                    this.level.addParticle(new TailBlackParticles.TailParticleData(random.nextDouble(0.03D, 0.1D), random.nextInt(10, 70)),
                            getX() + random.nextDouble(-1.5, 1.5), getY()+random.nextDouble(-1.5, 1.5) + 1.5, getZ() + random.nextDouble(-1.5, 1.5),
                            random.nextDouble(-0.3d, 0.3d), random.nextDouble(-0.3d, 0.3d), random.nextDouble(-0.3d, 0.3d));
                }
                  this.discard();
            }
        }
}

    @Override
    public void playerTouch(Player player) {
        super.playerTouch(player);
        if (!level.isClientSide()) {
            float distance = this.distanceTo(player);
            if (getEntityData().get(ISREADYTOCATCH) > 31 && !hasCaughtPlayer() && !isDeadOrDying()) {
                player.startRiding(this, false);
            }
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
}
