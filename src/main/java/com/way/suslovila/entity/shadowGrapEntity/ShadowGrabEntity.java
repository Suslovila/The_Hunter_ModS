package com.way.suslovila.entity.shadowGrapEntity;

import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.entity.hunter.HunterEntity;
import com.way.suslovila.entity.trap.TrapEntity;
import com.way.suslovila.particles.ModParticles;
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
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;


public class ShadowGrabEntity extends Mob implements IAnimatable, IAnimationTickable {
    @Nullable
    private UUID ownerUUID;
    private int tickTimer = 0;
    private static final EntityDataAccessor<Integer> ISREADYTOCATCH = SynchedEntityData.defineId(ShadowGrabEntity.class, EntityDataSerializers.INT);

    private AnimationFactory factory = new AnimationFactory(this);

    public ShadowGrabEntity(EntityType<? extends Mob> p_21368_, Level p_21369_) {
        super(p_21368_, p_21369_);
    }


    public static AttributeSupplier setAttributes() {

        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 16.0f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.3f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 10D)
                .build();
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(ISREADYTOCATCH, 0);

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
    public void registerControllers(AnimationData data) {

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

    @Override
public void tick(){
        super.tick();
        if(!level.isClientSide()) {
            tickTimer++;
            int chance = random.nextInt(5);
            if(chance == 2){
                double deltaX = random.nextDouble(0.0D, 0.5D);
                double deltaZ = random.nextDouble(0.0D,0.5D);
                if(random.nextBoolean())deltaX = -deltaX;
                if(random.nextBoolean())deltaZ = -deltaZ;


                this.level.addParticle(ModParticles.HEAD_BLACK_PARTICLES.get(),
                        this.getX()+deltaX, this.getY(), this.getZ()+deltaZ,
                        0.01D, 0.01D, 0.01D);
            }
            if(tickTimer == 10) {
                this.level.addParticle(ModParticles.TEST_BLACK_PARTICLES.get(),
                        this.getX() + 1, this.getY() + 1, this.getZ() + 1,
                        0.1, 0.1, 0.1);
                tickTimer = 0;
            }
            this.getEntityData().set(ISREADYTOCATCH, getEntityData().get(ISREADYTOCATCH) + 1);
            if (hasCaughtPlayer()) {
                if (this.getPassengers().get(0).isShiftKeyDown()) {
                    this.getPassengers().get(0).setShiftKeyDown(false);
                }
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
}

    @Override
    public void playerTouch(Player player) {
        super.playerTouch(player);
        if (!level.isClientSide()) {
            float distance = this.distanceTo(player);
            if (distance < 0.6F && getEntityData().get(ISREADYTOCATCH) > 20 && !hasCaughtPlayer() && !(player.getVehicle() instanceof TrapEntity)) {
                player.startRiding(this, true);
                if (this.level instanceof ServerLevel) {
                    if (((ServerLevel) level).getEntity(this.ownerUUID) != null) {
                        this.navigation.moveTo(((ServerLevel) level).getEntity(this.ownerUUID), 0.1);
                    }
                }
            }
        }
    }


    @Override
    public boolean canRiderInteract() {
        return false;
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }
    public boolean hasCaughtPlayer() {
        return this.hasExactlyOnePlayerPassenger();
    }
}
