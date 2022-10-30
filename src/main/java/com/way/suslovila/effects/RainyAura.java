package com.way.suslovila.effects;


import com.way.suslovila.effects.rainyaura.RainyAuraCapProvider;
import com.way.suslovila.effects.rainyaura.RainyAuraStorage;
import com.way.suslovila.savedData.clientSynch.ClientRainyAuraData;
import com.way.suslovila.savedData.clientSynch.Messages;
import com.way.suslovila.savedData.clientSynch.PacketSyncRainyAuraToClient;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.*;

import static com.way.suslovila.entity.projectile.explosionArrow.ExplosionArrow.random;


public class RainyAura extends MobEffect {
    Random random = new Random();


    protected RainyAura(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);
        if (pLivingEntity.isOnFire()) pLivingEntity.clearFire();


        if (pLivingEntity.hasEffect(ModEffects.HELLISH_FLAMES.get()))
            pLivingEntity.removeEffect(ModEffects.HELLISH_FLAMES.get());

        if (pLivingEntity.level.isClientSide()) {

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

//            Set<Entity> keys = mobListClient.keySet();
//            Iterator<Entity> iterator = keys.iterator();
//            while (iterator.hasNext()) {
//                Entity entity = iterator.next();
//                if (entity != null) {
//
//
//                    double xDeltaForEntity = random.nextDouble(entity.getBbWidth() / 2, entity.getBbWidth() / 2 + 0.7D);
//                    double zDeltaForEntity = random.nextDouble(entity.getBbWidth() / 2, entity.getBbWidth() / 2 + 0.7D);
//                    if (random.nextBoolean()) {
//                        xDeltaForEntity = -xDeltaForEntity;
//                    }
//                    if (random.nextBoolean()) {
//                        zDeltaForEntity = -zDeltaForEntity;
//                    }
//                    double yHeighForEntity = entity.getBbHeight();
//                    double yDeltaForEntity = random.nextDouble(yHeighForEntity + 0.5D);
//                    if (random.nextBoolean()) {
//                        yDeltaForEntity = random.nextDouble(yHeighForEntity + 0.7d, yHeighForEntity + 1.5D);
//                    }
//                    entity.level.addParticle(ParticleTypes.FALLING_WATER, entity.getX() + xDeltaForEntity, entity.getY() + yDeltaForEntity, entity.getZ() + zDeltaForEntity, 0, 0, 0);
//
//
//                    double xDeltaForEntity2 = random.nextDouble(entity.getBbWidth() / 2, entity.getBbWidth() / 2 + 0.7D);
//                    double zDeltaForEntity2 = random.nextDouble(entity.getBbWidth() / 2, entity.getBbWidth() / 2 + 0.7D);
//                    if (random.nextBoolean()) {
//                        xDeltaForEntity2 = -xDeltaForEntity2;
//                    }
//                    if (random.nextBoolean()) {
//                        zDeltaForEntity2 = -zDeltaForEntity2;
//                    }
//                    double yHeighForEntity2 = entity.getBbHeight();
//                    double yDeltaForEntity2 = random.nextDouble(yHeighForEntity2 + 0.5D);
//                    if (random.nextBoolean()) {
//                        yDeltaForEntity2 = random.nextDouble(yHeighForEntity2 + 0.7d, yHeighForEntity2 + 1.5D);
//                    }
//                    entity.level.addParticle(ParticleTypes.FALLING_WATER, entity.getX() + xDeltaForEntity2, entity.getY() + yDeltaForEntity2, entity.getZ() + zDeltaForEntity2, 0, 0, 0);
//
//
//                    double xDeltaForEntity3 = random.nextDouble(entity.getBbWidth() / 2, entity.getBbWidth() / 2 + 0.7D);
//                    double zDeltaForEntity3 = random.nextDouble(entity.getBbWidth() / 2, entity.getBbWidth() / 2 + 0.7D);
//                    if (random.nextBoolean()) {
//                        xDeltaForEntity3 = -xDeltaForEntity3;
//                    }
//                    if (random.nextBoolean()) {
//                        zDeltaForEntity3 = -zDeltaForEntity3;
//                    }
//                    double yHeighForEntity3 = entity.getBbHeight();
//                    double yDeltaForEntity3 = random.nextDouble(yHeighForEntity3 + 0.5D);
//                    if (random.nextBoolean()) {
//                        yDeltaForEntity3 = random.nextDouble(yHeighForEntity3 + 0.7d, yHeighForEntity3 + 1.5D);
//                    }
//                    entity.level.addParticle(ParticleTypes.FALLING_WATER, entity.getX() + xDeltaForEntity3, entity.getY() + yDeltaForEntity3, entity.getZ() + zDeltaForEntity3, 0, 0, 0);
//
//
//


//                    if (entity.isOnFire()) {
//                        mobListClient.put(entity, mobListClient.get(entity) + 1);
//                        if (mobListClient.get(entity) > 20) {
//                            entity.clearFire();
//                            mobListClient.remove(entity);
//                        }
//                    } else {
//                        mobListClient.remove(entity);
//                    }
//                    if (pLivingEntity.distanceTo(entity) > 15) {
//                        mobListClient.remove(entity);
//                    }
//                    if (!entity.isAlive()) {
//                        mobListClient.remove(entity);
//                    }
//                } else {
//                    mobListClient.remove(entity);
//                }
//
//
//            }

            if (ClientRainyAuraData.getEntities() != null) {
                ArrayList<Integer> entitiesFromCap = ClientRainyAuraData.getEntities();

                    for (int i = 0; i < entitiesFromCap.size(); i++) {
                        Entity entity = pLivingEntity.level.getEntity(entitiesFromCap.get(i));
                        if (entity != null) {
                            for (int u = 0; u < 4; u++) {
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

                            }
                        }
                    }

            }
            //block Client stuff
            if (ClientRainyAuraData.getBlocks() != null) {
                ArrayList<BlockPos> blocksFromCap = ClientRainyAuraData.getBlocks();
                for (int g = 0; g < 2; g++) {
                    for (int i = 0; i < blocksFromCap.size(); i++) {
                        BlockPos pos = blocksFromCap.get(i);
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
        if (!pLivingEntity.level.isClientSide()) {
            if (pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).isPresent()) {
                HashMap<BlockPos, Integer> blocksFromCap = pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).map(RainyAuraStorage::getMapOfBlocks).get();
                HashMap<Integer, Integer> entitiesFromCap = pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).map(RainyAuraStorage::getEntities).get();

//                Messages.sendRainyAuraInfo(new PacketSyncRainyAuraToClient(new ArrayList<>(blocksFromCap.keySet()), new ArrayList<>(entitiesFromCap.keySet())), pLivingEntity);
            }

            //Mobs && BLocks Stuff Server Stuff

            BlockPos checkBlock;
            int distanceXZY = 7;
            for (int x = pLivingEntity.getBlockX() - distanceXZY; x < pLivingEntity.getBlockX() + distanceXZY; x++) {
                for (int y = pLivingEntity.getBlockY() - distanceXZY; y < pLivingEntity.getBlockY() + distanceXZY; y++) {
                    for (int z = pLivingEntity.getBlockZ() - distanceXZY; z < pLivingEntity.getBlockZ() + distanceXZY; z++) {
                        checkBlock = new BlockPos(x, y, z);
                        if (pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).isPresent()) {
                            HashMap<BlockPos, Integer> blocksFromCap = pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).map(RainyAuraStorage::getMapOfBlocks).get();
                            if ((!(pLivingEntity.level.dimension() == Level.NETHER)) || (pLivingEntity.level.dimension() == Level.NETHER && pAmplifier >= 1)) {
                                Block block = pLivingEntity.level.getBlockState(checkBlock).getBlock();
                                if (!blocksFromCap.containsKey(checkBlock) && (block == Blocks.FIRE || block == Blocks.SOUL_FIRE || (block == Blocks.LAVA && pAmplifier >= 2))) {
                                    BlockPos finalCheckBlock = checkBlock;
                                    pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(blocks -> {
                                        blocks.addBlockPos(finalCheckBlock, 0);
                                    });
                                   pLivingEntity.level.playSound(null, finalCheckBlock.getX(), finalCheckBlock.getY(), finalCheckBlock.getZ(), SoundEvents.WEATHER_RAIN, SoundSource.AMBIENT, 0.5F, 1.0F);


                                }
                            }
                        }
                    }
                }
            }
            if (pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).isPresent()) {
                HashMap<Integer, Integer> EntitiesFromCap = pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).map(RainyAuraStorage::getEntities).get();

                List<Entity> entityList = pLivingEntity.level.getEntities(pLivingEntity, new AABB(pLivingEntity.getX() + 7, pLivingEntity.getY() + 7, pLivingEntity.getZ() + 7, pLivingEntity.getX() - 7, pLivingEntity.getY() - 7, pLivingEntity.getZ() - 7));
            for (int i = 0; i < entityList.size(); i++) {

                if (entityList.get(i).isOnFire() && !EntitiesFromCap.containsKey(entityList.get(i).getId()) && (pLivingEntity.level.dimension() != Level.NETHER || (pLivingEntity.level.dimension() == Level.NETHER) && pAmplifier >= 1)) {
                    int finalI = i;
                    pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(blocks -> {
                        blocks.addEntity(entityList.get(finalI).getId(), 1);

              });
            }
        }
            }

//Mobs Server Stuff
//            Set<Entity> keys = mobListServer.keySet();
//            Iterator<Entity> iterator = keys.iterator();
//            while (iterator.hasNext()) {
//                Entity entity = iterator.next();
//                if (entity != null) {
//                    if (entity.isOnFire()) {
//                        mobListServer.put(entity, mobListServer.get(entity) + 1);
//                        if (mobListServer.get(entity) > 20) {
//                            entity.clearFire();
//                            mobListServer.remove(entity);
//                        }
//                    } else {
//                        mobListServer.remove(entity);
//                    }
//                    if(pLivingEntity.distanceTo(entity) > 15){
//                        mobListServer.remove(entity);
//                    }
//                    if (!entity.isAlive()) {
//                        mobListServer.remove(entity);
//                    }
//                } else {
//                    mobListServer.remove(entity);
//                }
//            }
            //Blocks stuff
            if (pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).isPresent()) {
                HashMap<BlockPos, Integer> blocksFromCap = pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).map(RainyAuraStorage::getMapOfBlocks).get();

                System.out.println(blocksFromCap);
                HashMap<BlockPos, Integer> blocksFromCap20 = (HashMap<BlockPos, Integer>)blocksFromCap.clone();
                Iterator<BlockPos> iteratorBlock = blocksFromCap20.keySet().iterator();
                while (iteratorBlock.hasNext()) {
                    BlockPos pos = iteratorBlock.next();
                    if (pos != null) {
                        Block block = pLivingEntity.level.getBlockState(pos).getBlock();

                        if (!(block == Blocks.FIRE || block == Blocks.SOUL_FIRE || (block == Blocks.LAVA && pAmplifier >= 2))) {
                            pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(blocks -> {
                                blocks.removeBlock(pos);
                            });

                        } else {
                            BlockPos entityPos = pLivingEntity.blockPosition();

                            pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(blocks -> {
                                System.out.print("Entering Lambda     ");
                                HashMap<BlockPos, Integer> map = blocks.getMapOfBlocks();
                                int timer = blocks.getMapOfBlocks().get(pos);
                                System.out.print("Here is the Timer : " + timer + "     ");
                                if (timer > 22) {
                                    if (block == Blocks.FIRE || block == Blocks.SOUL_FIRE) {
                                        pLivingEntity.level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.AMBIENT, 0.2F, 1.0F);
                                        pLivingEntity.level.removeBlock(pos, false);
                                        blocks.removeBlock(pos);

                                    }
                                    if (block == Blocks.LAVA) {
                                        pLivingEntity.level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LAVA_EXTINGUISH, SoundSource.AMBIENT, 0.2F, 1.0F);
                                        pLivingEntity.level.setBlockAndUpdate(pos, Blocks.OBSIDIAN.defaultBlockState());
                                    }
                                } else {
                                    System.out.print("      Timer Is not equal or more than 25       ");
                                    blocks.addBlockPos(pos, timer + 1);
                                }
                                if (Math.sqrt(pLivingEntity.blockPosition().distToCenterSqr(pos.getX(), pos.getY(), pos.getZ())) > 12.2D) {
                                    blocks.removeBlock(pos);
                                    System.out.print("    Block Is Too Far    ");
                                }
                                System.out.println();
                            });


                        }
                    }
                }
            }

            if (pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).isPresent()) {
                HashMap<Integer, Integer> entitiesFromCap = pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).map(RainyAuraStorage::getEntities).get();

                System.out.println(entitiesFromCap);
                HashMap<Integer, Integer> blocksFromCap20 = (HashMap<Integer, Integer>)entitiesFromCap.clone();
                Iterator<Integer> iteratorBlock = blocksFromCap20.keySet().iterator();
                while (iteratorBlock.hasNext()) {
                    int id = iteratorBlock.next();
                    Entity entity = pLivingEntity.level.getEntity(id);
                    if (entity != null) {
                        if (!entity.isOnFire() || (entity.isOnFire() && pLivingEntity.level.dimension() == Level.NETHER && pAmplifier < 1) || !entity.isAlive() || pLivingEntity.distanceTo(entity) > 7) {
                            pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(blocks -> {
                                blocks.removeEntity(id);
                            });
                        }else{
                            pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(blocks -> {
                                int timer = blocks.getEntities().get(id);
                                if(timer <= 22) {
                                    blocks.addEntity(id, timer + 1);
                                }else{
                                    entity.clearFire();
                                    pLivingEntity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.AMBIENT, 0.2F, 1.0F);
                                    blocks.removeEntity(id);
                                }

                            });
                        }
//                        mobListServer.put(entity, mobListServer.get(entity) + 1);
//                        if (mobListServer.get(entity) > 20) {
//                            entity.clearFire();
//                            mobListServer.remove(entity);
//                        }
//                    } else {
//                        mobListServer.remove(entity);
//                    }
//                    if(pLivingEntity.distanceTo(entity) > 15){
//                        mobListServer.remove(entity);
//                    }
//                    if (!entity.isAlive()) {
//                        mobListServer.remove(entity);
//                    }
//                } else {
//                    mobListServer.remove(entity);
//                }
                    }
                    else{
                        pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(blocks -> {
                                blocks.removeEntity(id);
                        });
                    }
                }
            }
        }
    }

//        if (pLivingEntity.tickCount % 20 == 0) {
//            List<Entity> entityList = pLivingEntity.level.getEntities(pLivingEntity, new AABB(pLivingEntity.getX() + 20, pLivingEntity.getY() + 20, pLivingEntity.getZ() + 20, pLivingEntity.getX() - 20, pLivingEntity.getY() - 20, pLivingEntity.getZ() - 20));
//            for (int i = 0; i < entityList.size(); i++) {
//                if (entityList.get(i).isOnFire() && !mobListServer.containsKey(entityList.get(i))) {
//                    if (pLivingEntity.level.isClientSide()) {
//                        mobListClient.put(entityList.get(i), 0);
//                    } else {
//                        if (entityList.get(i).isOnFire() && !mobListClient.containsKey(entityList.get(i))) {
//                            mobListServer.put(entityList.get(i), 0);
//
//                        }
//                    }
//
//                }
//            }
//        }

        //client Side Stuff




    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

}
