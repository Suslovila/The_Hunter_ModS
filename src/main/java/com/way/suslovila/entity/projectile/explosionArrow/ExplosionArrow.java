package com.way.suslovila.entity.projectile.explosionArrow;

import com.way.suslovila.entity.hunter.HunterEntity;
import com.way.suslovila.particles.DissolationParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Random;

public class ExplosionArrow extends AbstractArrow implements IAnimatable {
    private AnimationFactory factory = new AnimationFactory(this);
    public static Random random = new Random();

    public ExplosionArrow(EntityType<? extends AbstractArrow> p_36721_, Level p_36722_) {
        super(p_36721_, p_36722_);
    }

    private static final EntityDataAccessor<Float> XCoord = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> YCoord = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ZCoord = SynchedEntityData.defineId(HunterEntity.class, EntityDataSerializers.FLOAT);

    @Override
    protected void onHitEntity(EntityHitResult ray) {
        super.onHitEntity(ray);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }


    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
@Override
    protected void onHit(HitResult pResult) {

    BlockPos pos = this.blockPosition();
    for(int x = pos.getX()-  5;x < pos.getX() + 5; x++) {
        for (int y = pos.getY() - 5; y < pos.getY() + 5; y++) {
            for (int z = pos.getZ() - 5; z < pos.getZ() + 5; z++) {

                BlockPos checkPos = new BlockPos(x,y,z);
                Vec3 vec = new Vec3(checkPos.getX() - this.getX(), checkPos.getY() - this.getY(), checkPos.getZ() - this.getZ());
                if (vec.length() <= 5 && !(this.level.getBlockState(checkPos).getBlock().getExplosionResistance() >=  Blocks.OBSIDIAN.getExplosionResistance())) {
                    if (level.isClientSide() && !level.getBlockState(checkPos).isAir()) {
//                        int g = 0;
//                        while (g < 30) {
//                            TerrainParticle particle = new TerrainParticle((ClientLevel) this.level, checkPos.getX(), checkPos.getY(), checkPos.getZ(), random.nextDouble(-0.1, 0.1), random.nextDouble(-0.1, 0.1), random.nextDouble(-0.1, 0.1), this.level.getBlockState(checkPos), checkPos);
//
//                            Minecraft.getInstance().particleEngine.add(new DissolationParticles((ClientLevel) this.level, checkPos.getX(), checkPos.getY(), checkPos.getZ(), random.nextDouble(-0.1, 0.1), random.nextDouble(-0.1, 0.1), random.nextDouble(-0.1, 0.1), 34, 45));
//                            g++;
//                        }
                    }
                       level.removeBlock(checkPos, true);
                }



            }
        }
    }









//            this.level.explode(this, this.getX(), this.getY(), this.getZ(), 6.0F, Explosion.BlockInteraction.BREAK);
            this.discard();
        }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(XCoord, 0f);
        getEntityData().define(ZCoord, 0f);
        getEntityData().define(YCoord, 0f);
    }
    public float getXCoordToAim(){
        return getEntityData().get(XCoord);
    }
    public float getYCoordToAim(){
        return getEntityData().get(YCoord);
    }
    public float getZCoordToAim(){
        return getEntityData().get(ZCoord);
    }
    public void setXCoordToAim(float coord){
        getEntityData().set(XCoord, coord);
    }
    public void setYCoordToAim(float coord){
        getEntityData().set(YCoord, coord);
    }
    public void setZCoordToAim(float coord){getEntityData().set(ZCoord, coord);}

}



