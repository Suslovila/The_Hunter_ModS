package com.way.suslovila.entity.hunter.teleport;

import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.simplybackpacks.inventory.BackpackData;
import com.way.suslovila.simplybackpacks.items.BackpackItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class HunterTeleportFormEntity extends Entity implements IAnimatable {
private int timer = 36;
    private AnimationFactory factory = new AnimationFactory(this);

    public HunterTeleportFormEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = false;


    }
    @Override
    public void baseTick(){
        super.baseTick();
        timer--;
        if(timer <= 0){
            this.discard();

        }
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
//        System.out.println("RUNNING");
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.hunterteleportform.teleport", true));
        return PlayState.CONTINUE;
    }

    @Override
    protected void defineSynchedData() {

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

}

