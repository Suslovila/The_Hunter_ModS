package com.way.suslovila.entity.trap;

import com.way.suslovila.entity.shadowGrabEntity.ShadowGrabEntity;
import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.particles.TailBlackParticles;
import com.way.suslovila.savedData.HuntTime;
import com.way.suslovila.savedData.SaveVictim;
import net.minecraft.core.NonNullList;
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
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class TrapEntity  extends PathfinderMob implements IAnimatable {
    private int timer;
    private HashMap<Vec3, ArrayList<Object>> cordsForShadowsAroundHand = new HashMap<>();

    private boolean previousCapt;
    private AnimationFactory factory = new AnimationFactory(this);

    public TrapEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = false;
        this.setNoGravity(false);
        this.noCulling = true;
        this.previousCapt = true;
    }

    public static AttributeSupplier setAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.0f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.0f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 10D)
                .build();
    }



    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        boolean now = ((TrapEntity) event.getAnimatable()).captured();

        if (now) {
            previousCapt = now;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.trap.catched", true));
            return PlayState.CONTINUE;
        }
        if(!now){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.trap.idle", true));
            //System.out.println("No one is catched");
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
    public boolean rideableUnderWater() {
        return true;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if(!level.isClientSide()){
//            if (cordsForShadowsAroundHand.size() < 16) {
//                double radius = random.nextDouble(0.3, 1.4);
//                int timer = 0;
//                Vec3 lookVector = this.getViewVector(0);
//                Vec3 lookVectorNormal = new Vec3(lookVector.x + random.nextDouble(-2, 2), 0, lookVector.z + random.nextDouble(-2, 2));
//                Vec3 m = new Vec3(lookVectorNormal.z, 0, -lookVectorNormal.x);
//                m = m.normalize();
//                // Vec3 k = lookVectorNormal.cross(m);
//                Vec3 k = new Vec3(0, -1, 0);
//                //k = new Vec3(k.x, -Math.abs(k.y), k.z);
//                ArrayList<Object> arrayList = new ArrayList<Object>();
//                arrayList.add(radius);
//                arrayList.add(m);
//                arrayList.add(k);
//                arrayList.add(random.nextDouble(0.1D, 0.4D));
//                arrayList.add(timer);
//                cordsForShadowsAroundHand.put(new Vec3(this.position().x + random.nextDouble(-3, 3), this.position().y + random.nextDouble(-3, 3), this.position().z + random.nextDouble(-3, 3)), arrayList);
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
//                for (int h = 0; h < 12; h++) {
//                    int timer = (int) list.get(4);
//                    if (timer % 100 == 0 && timer != 0 && random.nextBoolean()) {
//                        cordsForShadowsAroundHand.remove(dotInSpace);
//                        double newRadius = random.nextDouble(0.3, 1.4);
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
//                    ((ServerLevel) this.level).sendParticles(new TailBlackParticles.TailParticleData(random.nextDouble(particleSize-0.04*particleSize/0.3, particleSize), random.nextInt(21, 22)),
//                            endPosition.x, endPosition.y, endPosition.z, 1, 0,
//                            0, 0, 0);
//                    if (random.nextInt(200) == 37) {
//                        cordsForShadowsAroundHand.remove(dotInSpace);
//                    }
//                }
//            }
        }
//        this.level.addParticle(ModParticles.HEAD_BLACK_PARTICLES.get(),
//                this.getX() + 1, this.getY() + 1, this.getZ() + 1,
//                1, 1, 1);

        if (this.captured() && !this.level.isClientSide()) {
            timer++;
            if (this.getPassengers().get(0).isShiftKeyDown()) {
                this.getPassengers().get(0).setShiftKeyDown(false);
            }
            if (timer == 10) {
                Player player = (Player)this.getPassengers().get(0);
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
            if (distance < 0.6F && !(player.getVehicle() instanceof ShadowGrabEntity)) {
                player.startRiding(this, true);
                if(SaveVictim.get(player.level).getVictim().equals("novictim")){
                    SaveVictim.get(player.level).changeVictim(player.getUUID().toString());
                    HuntTime.get(player.level).setTime();
                }
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
        return 0.5D;
    }

    public double getPassengersRidingOffset() {
        return 0.0D;
    }

}
