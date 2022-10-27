package com.way.suslovila.entity.hunter.appearance;

import com.way.suslovila.entity.ModEntityTypes;
import com.way.suslovila.entity.hunter.HunterEntity;
import com.way.suslovila.particles.ModParticles;
import com.way.suslovila.savedData.SaveVictim;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.core.processor.IBone;

import java.util.Random;
import java.util.UUID;

public class HunterAppearanceFormEntity extends Entity implements IAnimatable {
private int timer = 39;
Random random = new Random();
int chis = 3;
    private AnimationFactory factory = new AnimationFactory(this);

    public HunterAppearanceFormEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = false;


    }
    @Override
    public void baseTick() {
        super.baseTick();
        timer--;
        if(this.level.isClientSide()){
            int chance = random.nextInt(5);
            if(chance == 2){
                double deltaX = random.nextDouble(0.8D, 1.5D);
                double deltaZ = random.nextDouble(0.8D, 1.5D);
                if(random.nextBoolean())deltaX = -deltaX;
                if(random.nextBoolean())deltaZ = -deltaZ;
                double deltaY = random.nextDouble(0, 4.9D);

                this.level.addParticle(ModParticles.HEAD_BLACK_PARTICLES.get(),
                        this.getX()+deltaX, this.getY()+deltaY, this.getZ()+deltaZ,
                        0.01D, 0.01D, 0.01D);
            }
        }
        if (timer <= 0) {
                HunterEntity hunter = new HunterEntity(ModEntityTypes.HUNTER.get(), this.level);
                hunter.moveTo(this.getX(), this.getY(), this.getZ(),this.getYRot(), this.getXRot());
                this.level.addFreshEntity(hunter);
                hunter.setYRot(this.getYRot());
                hunter.setYHeadRot(this.getYHeadRot());
            this.discard();

            }

    }


    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (chis ==3) {
            chis = random.nextInt(2);
        }

        if(chis == 1) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.hunterappearform.neck", true));
            return PlayState.CONTINUE;
        }
        if(chis == 0){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.hunterappearform.teleport", true));
            return PlayState.CONTINUE;
        }
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

    public void lookAtVictim(EntityAnchorArgument.Anchor pAnchor, Vec3 pTarget, IBone bone) {
        Vec3 vec3 = pAnchor.apply(this);
  double dx = pTarget.x - vec3.x;
    double dz = pTarget.z - vec3.z;
    //bone.setRotationY(Mth.wrapDegrees((float)(Math.atan2(dx,dz))));
////    bone.setRotationY(Mth.wrapDegrees((float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F));
        this.setYRot(Mth.wrapDegrees(-(float)(Mth.atan2(dz, dx) * (double)(180F / (float)Math.PI))));
  //  this.setYHeadRot((float)(Mth.atan2(dz, dx) * (double)(180F / (float)Math.PI)));
      // this.yRotO = this.getYRot();

    }

}

