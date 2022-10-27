package com.way.suslovila.entity.shadowgardenentity;

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
import java.util.List;
import java.util.UUID;


public class ShadowGardenEntity extends Entity implements IAnimatable, IAnimationTickable {
    @Nullable
    private UUID ownerUUID;
    private int tickTimer = 0;
    private static final EntityDataAccessor<Integer> ISREADYTOCATCH = SynchedEntityData.defineId(ShadowGardenEntity.class, EntityDataSerializers.INT);

    private AnimationFactory factory = new AnimationFactory(this);

    public ShadowGardenEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Override
    protected void defineSynchedData() {
        getEntityData().define(ISREADYTOCATCH, 0);
    }



    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("shadowgarden.animation.catch", false));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
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
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }
    public double getMyRidingOffset() {
        return 0.5D;
    }

    public double getPassengersRidingOffset() {
        return 0.0D;
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
            this.getEntityData().set(ISREADYTOCATCH, getEntityData().get(ISREADYTOCATCH) + 1);
            if (hasCaughtPlayer()) {
                if (this.getPassengers().get(0).isShiftKeyDown()) {
                    this.getPassengers().get(0).setShiftKeyDown(false);
                }
            }
            if( this.getEntityData().get(ISREADYTOCATCH) > 15){
                if(hasCaughtPlayer()){
                }
            }
        }
}
@Override
    public void move(MoverType pType, Vec3 pPos) {
        super.move(pType, pPos);
        this.checkInsideBlocks();
    }

    @Override
    public void playerTouch(Player player) {
        super.playerTouch(player);
        if (!level.isClientSide()) {
            float distance = this.distanceTo(player);
            if (distance < 1F) {
                player.startRiding(this, true);

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
