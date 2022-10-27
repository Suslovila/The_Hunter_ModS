package com.way.suslovila.entity.trap.ender;

import com.way.suslovila.MysticalCreatures;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class TrapEntity extends PathfinderMob implements IAnimatable {
    private int timer;
    private boolean previousCapt;
    private AnimationFactory factory = new AnimationFactory(this);

    public TrapEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
        this.noCulling = true;
        this.previousCapt = true;
    }

    public static AttributeSupplier setAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.0f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.0f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 10D)
                .build();
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        boolean now = ((TrapEntity) event.getAnimatable()).captured();
        /*if (now && !previousCapt) {
            previousCapt = now;
            System.out.println(previousCapt);
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.trap.catch", false));
            return PlayState.CONTINUE;
        }
        if (!now && previousCapt) {
            previousCapt = now;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.trap.lost", false));
            return PlayState.CONTINUE;
        }
        if (now && previousCapt) {
            previousCapt = now;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.trap.idle", true));
            return PlayState.CONTINUE;
        }
        if (!now && !previousCapt) {
            previousCapt = now;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.trap.idle", true));
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
        return PlayState.CONTINUE;
    }
*/
        if (now) {
            previousCapt = now;
            System.out.println(previousCapt);
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.trap.catched", true));
            return PlayState.CONTINUE;
        }
        if(!now){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.trap.idle", true));
            return PlayState.CONTINUE;
        }
        return PlayState.CONTINUE;
    }
    private <E extends IAnimatable> PlayState predicate2(AnimationEvent<E> event) {
        boolean now = ((TrapEntity) event.getAnimatable()).captured();
        if (now) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.trap.catch", false));
            return PlayState.CONTINUE;
        }
        if(!now){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.trap.lost", false));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controllerOfFast", 0, this::predicate2));
        data.addAnimationController(new AnimationController(this, "controllerOfLongBehaviour",
                0, this::predicate));
    }
@Override
public void baseTick() {
    super.baseTick();

    if (this.captured() && !this.level.isClientSide()) {
        timer++;
        if (this.getPassengers().get(0).isShiftKeyDown()) {
            this.getPassengers().get(0).setShiftKeyDown(false);
        }
        if (timer == 12) {
            Player player = (Player)this.getPassengers().get(0);
            System.out.println(player);
            int count1 = 0;
            for (int i = 0; i < 2; i++) {
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
                        } else {
                            armorItem.hurtAndBreak(10, player, (p_35997_) -> {
                                p_35997_.broadcastBreakEvent(EquipmentSlot.LEGS);
                            });
                        }


                    } else {
                        armorItem.hurt(10, random, (ServerPlayer) player);
                    }
                }

            }
            if(count1 == 2){
                DamageSource damageSourceP = new DamageSource(MysticalCreatures.MOD_ID + "pressure");
                damageSourceP.bypassArmor();
                player.hurt(damageSourceP, 1);
            }
            timer = 0;
        }
    }
}




    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    protected void doPush(Entity pEntity) {
    }

    @Override
    public boolean canRiderInteract() {
        return true;
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    public boolean captured() {
        return this.hasExactlyOnePlayerPassenger();
    }

    @Override
    public boolean canBeControlledByRider() {
        return true;
    }
    @Override
    public void playerTouch(Player player) {
        super.playerTouch(player);
        boolean IsShifting = player.isShiftKeyDown();
        if(!IsShifting && !this.captured() && !player.level.isClientSide()) {
            float distance = this.distanceTo(player);
            if (distance < 0.6F) {
                player.startRiding(this, true);
                NonNullList<ItemStack> armor = player.getInventory().armor;
                float koef = 0;
                if(!armor.get(0).isEmpty()){
                    koef += ((ArmorItem)armor.get(0).getItem()).getDefense();
                }
                if (!armor.get(1).isEmpty()){
                    koef += ((ArmorItem)armor.get(1).getItem()).getDefense();
                }
                DamageSource damageSource = new DamageSource(MysticalCreatures.MOD_ID +"shadow_trap");
                damageSource.bypassArmor();
                player.hurt(damageSource, 15-koef*1.3f);
            }
        }
    }
    public void setPreviousCapt(boolean new1){
        this.previousCapt = new1;
    }
@Override
public boolean showVehicleHealth() {return false;}
    @Override
    public double getMyRidingOffset() {
        return 0.3D;
    }
}
