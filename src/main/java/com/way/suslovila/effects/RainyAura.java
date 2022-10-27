package com.way.suslovila.effects;

import com.way.suslovila.effects.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

import java.util.*;


public class RainyAura extends MobEffect {
    Random random = new Random();
    Map<Entity, Integer> mobListServer = new HashMap<>();
    Map<Entity, Integer> mobListClient = new HashMap<>();

    Map<BlockToPut, Integer> blockList = new HashMap<>();
    Map<BlockToPut, Integer> lavaList = new HashMap<>();

    protected RainyAura(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);
        if(pLivingEntity.level.isClientSide) {
            System.out.println(blockList.size());
            System.out.println(mobListClient.size());
        }else{
            System.out.println(blockList.size());
            System.out.println(mobListServer.size());
        }


        if (pLivingEntity.isOnFire()) {
            pLivingEntity.clearFire();
        }




        if (pLivingEntity.hasEffect(ModEffects.HELLISH_FLAMES.get())) {
            pLivingEntity.removeEffect(ModEffects.HELLISH_FLAMES.get());
        }




        if (pLivingEntity.tickCount % 20 == 0) {
            List<Entity> entityList = pLivingEntity.level.getEntities(pLivingEntity, new AABB(pLivingEntity.getX() + 20, pLivingEntity.getY() + 20, pLivingEntity.getZ() + 20, pLivingEntity.getX() - 20, pLivingEntity.getY() - 20, pLivingEntity.getZ() - 20));
            for (int i = 0; i < entityList.size(); i++) {
                if (entityList.get(i).isOnFire() && !mobListServer.containsKey(entityList.get(i))) {
                    if (pLivingEntity.level.isClientSide()) {
                        mobListClient.put(entityList.get(i), 0);
                    } else {
                        if(entityList.get(i).isOnFire() && !mobListClient.containsKey(entityList.get(i))) {
                            mobListServer.put(entityList.get(i), 0);

                        }
                    }

                }
            }
        }

        //client Side Stuff
        if(pLivingEntity.level.isClientSide()){

            double xDelta = random.nextDouble(pLivingEntity.getBbWidth() / 2, pLivingEntity.getBbWidth() / 2 + 0.5D);
            double zDelta = random.nextDouble(pLivingEntity.getBbWidth() / 2, pLivingEntity.getBbWidth() / 2 + 0.5D);
            if (random.nextBoolean()) {
                xDelta = -xDelta;
            }
            if (random.nextBoolean()) {
                zDelta = -zDelta;
            }

            double yHeigh = pLivingEntity.getBbHeight();
            double yDelta = random.nextDouble(yHeigh);
            pLivingEntity.level.addParticle(ParticleTypes.UNDERWATER, pLivingEntity.getX() + xDelta, pLivingEntity.getY() + yDelta, pLivingEntity.getZ() + zDelta, 0, 0, 0);

            //mob client stuff

            Set<Entity> keys = mobListClient.keySet();
            Iterator<Entity> iterator = keys.iterator();
            while (iterator.hasNext()) {
                Entity entity = iterator.next();
                if (entity != null) {


                    double xDeltaForEntity = random.nextDouble(entity.getBbWidth() / 2, entity.getBbWidth() / 2 + 0.7D);
                    double zDeltaForEntity = random.nextDouble(entity.getBbWidth() / 2, entity.getBbWidth() / 2 + 0.7D);
                    if (random.nextBoolean()) {
                        xDeltaForEntity = -xDeltaForEntity;
                    }
                    if (random.nextBoolean()) {
                        zDeltaForEntity = -zDeltaForEntity;
                    }
                    double yHeighForEntity = entity.getBbHeight();
                    double yDeltaForEntity = random.nextDouble(yHeighForEntity + 0.5D);
                    if (random.nextBoolean()) {
                        yDeltaForEntity = random.nextDouble(yHeighForEntity + 0.7d, yHeighForEntity + 1.5D);
                    }
                    entity.level.addParticle(ParticleTypes.FALLING_WATER, entity.getX() + xDeltaForEntity, entity.getY() + yDeltaForEntity, entity.getZ() + zDeltaForEntity, 0, 0, 0);


                    double xDeltaForEntity2 = random.nextDouble(entity.getBbWidth() / 2, entity.getBbWidth() / 2 + 0.7D);
                    double zDeltaForEntity2 = random.nextDouble(entity.getBbWidth() / 2, entity.getBbWidth() / 2 + 0.7D);
                    if (random.nextBoolean()) {
                        xDeltaForEntity2 = -xDeltaForEntity2;
                    }
                    if (random.nextBoolean()) {
                        zDeltaForEntity2 = -zDeltaForEntity2;
                    }
                    double yHeighForEntity2 = entity.getBbHeight();
                    double yDeltaForEntity2 = random.nextDouble(yHeighForEntity2 + 0.5D);
                    if (random.nextBoolean()) {
                        yDeltaForEntity2 = random.nextDouble(yHeighForEntity2 + 0.7d, yHeighForEntity2 + 1.5D);
                    }
                    entity.level.addParticle(ParticleTypes.FALLING_WATER, entity.getX() + xDeltaForEntity2, entity.getY() + yDeltaForEntity2, entity.getZ() + zDeltaForEntity2, 0, 0, 0);


                    double xDeltaForEntity3 = random.nextDouble(entity.getBbWidth() / 2, entity.getBbWidth() / 2 + 0.7D);
                    double zDeltaForEntity3 = random.nextDouble(entity.getBbWidth() / 2, entity.getBbWidth() / 2 + 0.7D);
                    if (random.nextBoolean()) {
                        xDeltaForEntity3 = -xDeltaForEntity3;
                    }
                    if (random.nextBoolean()) {
                        zDeltaForEntity3 = -zDeltaForEntity3;
                    }
                    double yHeighForEntity3 = entity.getBbHeight();
                    double yDeltaForEntity3 = random.nextDouble(yHeighForEntity3 + 0.5D);
                    if (random.nextBoolean()) {
                        yDeltaForEntity3 = random.nextDouble(yHeighForEntity3 + 0.7d, yHeighForEntity3 + 1.5D);
                    }
                    entity.level.addParticle(ParticleTypes.FALLING_WATER, entity.getX() + xDeltaForEntity3, entity.getY() + yDeltaForEntity3, entity.getZ() + zDeltaForEntity3, 0, 0, 0);

                        if (entity.isOnFire()) {
                            mobListClient.put(entity, mobListClient.get(entity) + 1);
                            if (mobListClient.get(entity) > 20) {
                                entity.clearFire();
                                mobListClient.remove(entity);
                            }
                        } else {
                            mobListClient.remove(entity);
                        }
                        if(pLivingEntity.distanceTo(entity) > 15){
                            mobListClient.remove(entity);
                        }
                        if (!entity.isAlive()) {
                            mobListClient.remove(entity);
                        }
                    } else {
                    mobListClient.remove(entity);
                    }


            }

            //block Client stuff
            Set<BlockToPut> blockKeys = blockList.keySet();
            Iterator<BlockToPut> iteratorBlock = blockKeys.iterator();
            while (iteratorBlock.hasNext()) {
                BlockToPut blockToPut = iteratorBlock.next();
                if(blockToPut != null) {
                    BlockPos pos = blockToPut.pos;
                    if (pLivingEntity.level.dimension().equals(blockToPut.dimension)) {
                        blockList.put(blockToPut, blockList.get(blockToPut) + 1);
                        double x1 = random.nextDouble(0, 0.5D);
                        double z1 = random.nextDouble(0, 0.5D);
                        if (random.nextBoolean()) {
                            x1 = -x1;
                        }
                        if (random.nextBoolean()) {
                            z1 = -z1;
                        }

                        pLivingEntity.level.addParticle(ParticleTypes.FALLING_WATER, (double) pos.getX() + x1 + 0.5, (double) pos.getY() + random.nextDouble(1D, 2D), (double) pos.getZ() + z1 + 0.5, 0, 0, 0);
                    }
                }
            }



















        }
        else{
            //Mobs && BLocks Stuff Server Stuff

                BlockPos checkBlock;
                int distanceXZY = 15;
                for (int x = pLivingEntity.getBlockX() - distanceXZY; x < pLivingEntity.getX() + distanceXZY; x++) {
                    for (int y = pLivingEntity.getBlockY() - distanceXZY; y < pLivingEntity.getY() + distanceXZY; y++) {
                        for (int z = pLivingEntity.getBlockZ() - distanceXZY; z < pLivingEntity.getZ() + distanceXZY; z++) {
                            checkBlock = new BlockPos(x, y, z);
                            if (!blockList.containsKey(new BlockToPut(pLivingEntity.level.dimension(), checkBlock)) && (pLivingEntity.level.getBlockState(checkBlock).getBlock() == Blocks.FIRE || pLivingEntity.level.getBlockState(checkBlock).getBlock() == Blocks.SOUL_FIRE)) {
                                blockList.put(new BlockToPut(pLivingEntity.level.dimension(), checkBlock), 0);
                            }
                            if (!lavaList.containsKey(new BlockToPut(pLivingEntity.level.dimension(), checkBlock)) && (pLivingEntity.level.getBlockState(checkBlock).getBlock() == Blocks.LAVA) && pAmplifier >= 2) {
                                lavaList.put(new BlockToPut(pLivingEntity.level.dimension(), checkBlock), 0);
                            }
                        }
                    }
                }
            }

//Mobs Server Stuff
            Set<Entity> keys = mobListServer.keySet();
            Iterator<Entity> iterator = keys.iterator();
            while (iterator.hasNext()) {
                Entity entity = iterator.next();
                if (entity != null) {
                    if (entity.isOnFire()) {
                        mobListServer.put(entity, mobListServer.get(entity) + 1);
                        if (mobListServer.get(entity) > 20) {
                            entity.clearFire();
                            mobListServer.remove(entity);
                        }
                    } else {
                        mobListServer.remove(entity);
                    }
                    if(pLivingEntity.distanceTo(entity) > 15){
                        mobListServer.remove(entity);
                    }
                    if (!entity.isAlive()) {
                        mobListServer.remove(entity);
                    }
                } else {
                    mobListServer.remove(entity);
                }
            }
            //Blocks stuff

            Set<BlockToPut> blockKeys = blockList.keySet();
            Iterator<BlockToPut> iteratorBlock = blockKeys.iterator();
            while (iteratorBlock.hasNext()) {
                BlockToPut blockToPut = iteratorBlock.next();
                if(blockToPut != null){
                BlockPos pos = blockToPut.pos;
                if (pLivingEntity.level.dimension().equals(blockToPut.dimension)) {
                    if (!(pLivingEntity.level.getBlockState(pos).getBlock() == Blocks.FIRE || pLivingEntity.level.getBlockState(pos).getBlock() == Blocks.SOUL_FIRE)) {
                        blockList.remove(blockToPut);
                    } else {
                            blockList.put(blockToPut, blockList.get(blockToPut) + 1);
                            if (blockList.get(blockToPut) > 40) {
                                pLivingEntity.level.removeBlock(pos, false);
                            }
                            if (pLivingEntity.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) > 15) {
                                blockList.remove(blockToPut);
                            }

                        }
                    }
                }
            }












//
//
//
//
//
//            Set<Entity> keys = mobList.keySet();
//            Iterator<Entity> iterator = keys.iterator();
//            while (iterator.hasNext()) {
//                Entity entity = iterator.next();
//                if (entity != null) {
//                    if (entity.isOnFire()) {
//                        mobList.put(entity, mobList.get(entity) + 1);
//
//
//                        double xDeltaForEntity = random.nextDouble(entity.getBbWidth() / 2, entity.getBbWidth() / 2 + 0.7D);
//                        double zDeltaForEntity = random.nextDouble(entity.getBbWidth() / 2, entity.getBbWidth() / 2 + 0.7D);
//                        if (random.nextBoolean()) {
//                            xDeltaForEntity = -xDeltaForEntity;
//                        }
//                        if (random.nextBoolean()) {
//                            zDeltaForEntity = -zDeltaForEntity;
//                        }
//                        double yHeighForEntity = entity.getBbHeight();
//                        double yDeltaForEntity = random.nextDouble(yHeighForEntity + 0.5D);
//                        if (random.nextBoolean()) {
//                            yDeltaForEntity = random.nextDouble(yHeighForEntity + 0.7d, yHeighForEntity + 1.5D);
//                        }
//                        entity.level.addParticle(ParticleTypes.FALLING_WATER, entity.getX() + xDeltaForEntity, entity.getY() + yDeltaForEntity, entity.getZ() + zDeltaForEntity, 0, 0, 0);
//
//
//                        double xDeltaForEntity2 = random.nextDouble(entity.getBbWidth() / 2, entity.getBbWidth() / 2 + 0.7D);
//                        double zDeltaForEntity2 = random.nextDouble(entity.getBbWidth() / 2, entity.getBbWidth() / 2 + 0.7D);
//                        if (random.nextBoolean()) {
//                            xDeltaForEntity2 = -xDeltaForEntity2;
//                        }
//                        if (random.nextBoolean()) {
//                            zDeltaForEntity2 = -zDeltaForEntity2;
//                        }
//                        double yHeighForEntity2 = entity.getBbHeight();
//                        double yDeltaForEntity2 = random.nextDouble(yHeighForEntity2 + 0.5D);
//                        if (random.nextBoolean()) {
//                            yDeltaForEntity2 = random.nextDouble(yHeighForEntity2 + 0.7d, yHeighForEntity2 + 1.5D);
//                        }
//                        entity.level.addParticle(ParticleTypes.FALLING_WATER, entity.getX() + xDeltaForEntity2, entity.getY() + yDeltaForEntity2, entity.getZ() + zDeltaForEntity2, 0, 0, 0);
//
//
//                        double xDeltaForEntity3 = random.nextDouble(entity.getBbWidth() / 2, entity.getBbWidth() / 2 + 0.7D);
//                        double zDeltaForEntity3 = random.nextDouble(entity.getBbWidth() / 2, entity.getBbWidth() / 2 + 0.7D);
//                        if (random.nextBoolean()) {
//                            xDeltaForEntity3 = -xDeltaForEntity3;
//                        }
//                        if (random.nextBoolean()) {
//                            zDeltaForEntity3 = -zDeltaForEntity3;
//                        }
//                        double yHeighForEntity3 = entity.getBbHeight();
//                        double yDeltaForEntity3 = random.nextDouble(yHeighForEntity3 + 0.5D);
//                        if (random.nextBoolean()) {
//                            yDeltaForEntity3 = random.nextDouble(yHeighForEntity3 + 0.7d, yHeighForEntity3 + 1.5D);
//                        }
//                        entity.level.addParticle(ParticleTypes.FALLING_WATER, entity.getX() + xDeltaForEntity3, entity.getY() + yDeltaForEntity3, entity.getZ() + zDeltaForEntity3, 0, 0, 0);
//
//
//                        if (mobList.get(entity) > 20) {
//                            entity.clearFire();
//                            mobList.remove(entity);
//                        }
//                    } else {
//                        mobList.remove(entity);
//                    }
//                    if(pLivingEntity.distanceTo(entity) > 15){
//                        mobList.remove(entity);
//                    }
//                    if (!entity.isAlive()) {
//                        mobList.remove(entity);
//                    }
//                } else {
//                    mobList.remove(entity);
//                }
//
//        }
//            Set<BlockToPut> blockKeys = blockList.keySet();
//            Iterator<BlockToPut> iteratorBlock = blockKeys.iterator();
//            while (iteratorBlock.hasNext()) {
//                BlockToPut blockToPut = iteratorBlock.next();
//                BlockPos pos = blockToPut.pos;
//                if (pLivingEntity.level.dimension().equals(blockToPut.dimension)) {
//                    if (!(pLivingEntity.level.getBlockState(pos).getBlock() == Blocks.FIRE || pLivingEntity.level.getBlockState(pos).getBlock() == Blocks.SOUL_FIRE)) {
//                        blockList.remove(blockToPut);
//                    } else {
//                            double x = random.nextDouble(0, 0.5D);
//                            double z = random.nextDouble(0, 0.5D);
//                            if (random.nextBoolean()) {
//                                x = -x;
//                            }
//                            if (random.nextBoolean()) {
//                                z = -z;
//                            }
//
//                            pLivingEntity.level.addParticle(ParticleTypes.FALLING_WATER, (double) pos.getX() + x + 0.5, (double) pos.getY() + random.nextDouble(1D, 2D), (double) pos.getZ() + z + 0.5, 0, 0, 0);
//                            blockList.put(blockToPut, blockList.get(blockToPut) + 1);
//                        double x1 = random.nextDouble(0, 0.5D);
//                        double z1 = random.nextDouble(0, 0.5D);
//                        if (random.nextBoolean()) {
//                            x1 = -x1;
//                        }
//                        if (random.nextBoolean()) {
//                            z1 = -z1;
//                        }
//
//                        pLivingEntity.level.addParticle(ParticleTypes.FALLING_WATER, (double) pos.getX() + x1 + 0.5, (double) pos.getY() + random.nextDouble(1D, 2D), (double) pos.getZ() + z1 + 0.5, 0, 0, 0);
//                        if (blockList.get(blockToPut) > 40) {
//                            pLivingEntity.level.removeBlock(pos, false);
//                        }
//                        if(pLivingEntity.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) > 15){
//                            blockList.remove(blockToPut);
//                        }
//
//                    }
//                }
//            }
        }









    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
    class BlockToPut{
        ResourceKey<Level> dimension;
        BlockPos pos;
        public BlockToPut(ResourceKey<Level> lev,BlockPos posit){
            dimension = lev;
            pos = posit;

        }
        @Override
         public boolean equals(Object obj){
            if (!(obj instanceof BlockToPut)) return false;
            return (this.dimension.equals(((BlockToPut)obj).dimension) && this.pos.equals(((BlockToPut)obj).pos));

        }
    }
}
