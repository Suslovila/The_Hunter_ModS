package com.way.suslovila.entity.bag;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class HunterBagEntity extends Entity implements IAnimatable {

    private AnimationFactory factory = new AnimationFactory(this);
    public HunterBagEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = false;


    }
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        //event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.hunterbag.idle", true));
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
    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        //System.out.println("Entering the method");
        if (this.isAlive()) {
            //System.out.println("Entity is Alive");
            if (!this.level.isClientSide()) {
                    //System.out.println("Game rules are ok");
//                    Item item = MysticalCreatures.COMMONBACKPACK.get();
//                    ItemStack itemStack = item.getDefaultInstance();
//                    ItemStack copyOf = itemStack;
//                    itemStack = putItemsToBackpack(copyOf, this.level);
                    ItemStack bagToSpawn = this.getCapability(BagProvider.BAG).map(HunterBagEntityItemsStorage::getBag).get();
                    this.spawnAtLocation(bagToSpawn);


                this.discard();
            }

            player.swing(hand);

            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }

    @Override
    public boolean isPickable() {
        return true;
    }

@Override
    public void tick() {
        this.baseTick();
        if(!level.isClientSide()) {
        }
    }
}
