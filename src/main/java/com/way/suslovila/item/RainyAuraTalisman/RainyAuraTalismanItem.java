package com.way.suslovila.item.RainyAuraTalisman;

import com.way.suslovila.effects.ModEffects;
import com.way.suslovila.effects.rainyaura.RainyAuraCapProvider;
import com.way.suslovila.effects.rainyaura.RainyAuraStorage;
import com.way.suslovila.savedData.clientSynch.ClientRainyAuraData;
import com.way.suslovila.savedData.clientSynch.Messages;
import com.way.suslovila.savedData.clientSynch.PacketSyncRainyAuraToClient;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.*;

public class RainyAuraTalismanItem extends Item {
    Random random = new Random();
    public RainyAuraTalismanItem(Properties pProperties) {
        super(pProperties);
    }
//@Override
//public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
//    if (!entity.level.isClientSide()) {
//        int mode = stack.getCapability(RainyAuraCapProvider.BLOCKS).map(RainyAuraStorage::getMode).get();
//        if (entity.isInWaterRainOrBubble() && mode != 3) {
//            stack.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(capa -> {
//                capa.changeEnergy(1);
//            });
//        }
//        if (mode < 2) {
//            if (entity.isOnFire()) {
//                if (stack.getCapability(RainyAuraCapProvider.BLOCKS).isPresent() && stack.getCapability(RainyAuraCapProvider.BLOCKS).map(RainyAuraStorage::getEnergy).get() >= EnergyForDifferentActions.WATER_SHIELD.amount) {
//                    entity.clearFire();
//                    if (!(entity instanceof LivingEntity)) {
//                        stack.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(capa -> {
//                            capa.changeEnergy(entity.level.dimension() == Level.NETHER ? EnergyForDifferentActions.EXTINGUISHING_ENTITY.amount * 2 : EnergyForDifferentActions.EXTINGUISHING_ENTITY.amount);
//                        });
//                    } else {
//                        if (((LivingEntity) entity).hasEffect(ModEffects.HELLISH_FLAMES.get()))
//                            ((LivingEntity) entity).removeEffect(ModEffects.HELLISH_FLAMES.get());
//                        ((LivingEntity) entity).addEffect(new MobEffectInstance(ModEffects.RAINY_AURA.get(), 100, 0, false, false));
//                        stack.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(capa -> {
//                            capa.changeEnergy(entity.level.dimension() == Level.NETHER ? EnergyForDifferentActions.WATER_SHIELD.amount * 2 : EnergyForDifferentActions.WATER_SHIELD.amount);
//                        });
//                    }
//                }
//            }
//        }
//        if (mode == 0){
//            if (level.isClientSide()) {
//
//
//
//                if (ClientRainyAuraData.getEntities() != null) {
//                    ArrayList<Integer> entitiesFromCap = ClientRainyAuraData.getEntities();
//
//                    for (int i = 0; i < entitiesFromCap.size(); i++) {
//                        Entity entityInMode = level.getEntity(entitiesFromCap.get(i));
//                        if (entityInMode != null) {
//                            for (int u = 0; u < 4; u++) {
//                                double xDeltaForEntity = random.nextDouble(entity.getBbWidth() / 2, entity.getBbWidth() / 2 + 0.7D);
//                                double zDeltaForEntity = random.nextDouble(entity.getBbWidth() / 2, entity.getBbWidth() / 2 + 0.7D);
//                                if (random.nextBoolean()) {
//                                    xDeltaForEntity = -xDeltaForEntity;
//                                }
//                                if (random.nextBoolean()) {
//                                    zDeltaForEntity = -zDeltaForEntity;
//                                }
//                                double yHeighForEntity = entity.getBbHeight();
//                                double yDeltaForEntity = random.nextDouble(yHeighForEntity + 0.5D);
//                                if (random.nextBoolean()) {
//                                    yDeltaForEntity = random.nextDouble(yHeighForEntity + 0.7d, yHeighForEntity + 1.5D);
//                                }
//                                entity.level.addParticle(ParticleTypes.FALLING_WATER, entityInMode.getX() + xDeltaForEntity, entityInMode.getY() + yDeltaForEntity, entityInMode.getZ() + zDeltaForEntity, 0, 0, 0);
//
//                            }
//                        }
//                    }
//
//                }
//                //block Client stuff
//                if (ClientRainyAuraData.getBlocks() != null) {
//                    ArrayList<BlockPos> blocksFromCap = ClientRainyAuraData.getBlocks();
//                    for (int g = 0; g < 2; g++) {
//                        for (int i = 0; i < blocksFromCap.size(); i++) {
//                            BlockPos pos = blocksFromCap.get(i);
//                            double x1 = random.nextDouble(0, 0.5D);
//                            double z1 = random.nextDouble(0, 0.5D);
//                            if (random.nextBoolean()) {
//                                x1 = -x1;
//                            }
//                            if (random.nextBoolean()) {
//                                z1 = -z1;
//                            }
//
//                            level.addParticle(ParticleTypes.FALLING_WATER, (double) pos.getX() + x1 + 0.5, (double) pos.getY() + random.nextDouble(1D, 2D), (double) pos.getZ() + z1 + 0.5, 0, 0, 0);
//
//
//                        }
//                    }
//
//                }
//
//
//            }
//            if (!level.isClientSide()) {
//                if (stack.getCapability(RainyAuraCapProvider.BLOCKS).isPresent()) {
//                    HashMap<BlockPos, Integer> blocksFromCap = stack.getCapability(RainyAuraCapProvider.BLOCKS).map(RainyAuraStorage::getMapOfBlocks).get();
//                    HashMap<Integer, Integer> entitiesFromCap = stack.getCapability(RainyAuraCapProvider.BLOCKS).map(RainyAuraStorage::getEntities).get();
//
//                    Messages.sendRainyAuraInfo(new PacketSyncRainyAuraToClient(new ArrayList<>(blocksFromCap.keySet()), new ArrayList<>(entitiesFromCap.keySet())), stack);
//                }
//
//                //Mobs && BLocks Stuff Server Stuff
//
//                BlockPos checkBlock;
//                int distanceXZY = 7;
//                for (int x = pLivingEntity.getBlockX() - distanceXZY; x < pLivingEntity.getBlockX() + distanceXZY; x++) {
//                    for (int y = pLivingEntity.getBlockY() - distanceXZY; y < pLivingEntity.getBlockY() + distanceXZY; y++) {
//                        for (int z = pLivingEntity.getBlockZ() - distanceXZY; z < pLivingEntity.getBlockZ() + distanceXZY; z++) {
//                            checkBlock = new BlockPos(x, y, z);
//                            if (pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).isPresent()) {
//                                HashMap<BlockPos, Integer> blocksFromCap = pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).map(RainyAuraStorage::getMapOfBlocks).get();
//                                if ((!(pLivingEntity.level.dimension() == Level.NETHER)) || (pLivingEntity.level.dimension() == Level.NETHER && pAmplifier >= 1)) {
//                                    Block block = pLivingEntity.level.getBlockState(checkBlock).getBlock();
//                                    if (!blocksFromCap.containsKey(checkBlock) && (block == Blocks.FIRE || block == Blocks.SOUL_FIRE || (block == Blocks.LAVA && pAmplifier >= 2))) {
//                                        BlockPos finalCheckBlock = checkBlock;
//                                        pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(blocks -> {
//                                            blocks.addBlockPos(finalCheckBlock, 0);
//                                        });
//                                        pLivingEntity.level.playSound(null, finalCheckBlock.getX(), finalCheckBlock.getY(), finalCheckBlock.getZ(), SoundEvents.WEATHER_RAIN, SoundSource.AMBIENT, 0.5F, 1.0F);
//
//
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//                if (pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).isPresent()) {
//                    HashMap<Integer, Integer> EntitiesFromCap = pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).map(RainyAuraStorage::getEntities).get();
//
//                    List<Entity> entityList = pLivingEntity.level.getEntities(pLivingEntity, new AABB(pLivingEntity.getX() + 7, pLivingEntity.getY() + 7, pLivingEntity.getZ() + 7, pLivingEntity.getX() - 7, pLivingEntity.getY() - 7, pLivingEntity.getZ() - 7));
//                    for (int i = 0; i < entityList.size(); i++) {
//
//                        if (entityList.get(i).isOnFire() && !EntitiesFromCap.containsKey(entityList.get(i).getId()) && (pLivingEntity.level.dimension() != Level.NETHER || (pLivingEntity.level.dimension() == Level.NETHER) && pAmplifier >= 1)) {
//                            int finalI = i;
//                            pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(blocks -> {
//                                blocks.addEntity(entityList.get(finalI).getId(), 1);
//
//                            });
//                        }
//                    }
//                }
//
//                if (pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).isPresent()) {
//                    HashMap<BlockPos, Integer> blocksFromCap = pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).map(RainyAuraStorage::getMapOfBlocks).get();
//
//                    System.out.println(blocksFromCap);
//                    HashMap<BlockPos, Integer> blocksFromCap20 = (HashMap<BlockPos, Integer>)blocksFromCap.clone();
//                    Iterator<BlockPos> iteratorBlock = blocksFromCap20.keySet().iterator();
//                    while (iteratorBlock.hasNext()) {
//                        BlockPos pos = iteratorBlock.next();
//                        if (pos != null) {
//                            Block block = pLivingEntity.level.getBlockState(pos).getBlock();
//
//                            if (!(block == Blocks.FIRE || block == Blocks.SOUL_FIRE || (block == Blocks.LAVA && pAmplifier >= 2))) {
//                                pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(blocks -> {
//                                    blocks.removeBlock(pos);
//                                });
//
//                            } else {
//                                BlockPos entityPos = pLivingEntity.blockPosition();
//
//                                pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(blocks -> {
//                                    System.out.print("Entering Lambda     ");
//                                    HashMap<BlockPos, Integer> map = blocks.getMapOfBlocks();
//                                    int timer = blocks.getMapOfBlocks().get(pos);
//                                    System.out.print("Here is the Timer : " + timer + "     ");
//                                    if (timer > 22) {
//                                        if (block == Blocks.FIRE || block == Blocks.SOUL_FIRE) {
//                                            pLivingEntity.level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.AMBIENT, 0.2F, 1.0F);
//                                            pLivingEntity.level.removeBlock(pos, false);
//                                            blocks.removeBlock(pos);
//
//                                        }
//                                        if (block == Blocks.LAVA) {
//                                            pLivingEntity.level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LAVA_EXTINGUISH, SoundSource.AMBIENT, 0.2F, 1.0F);
//                                            pLivingEntity.level.setBlockAndUpdate(pos, Blocks.OBSIDIAN.defaultBlockState());
//                                        }
//                                    } else {
//                                        System.out.print("      Timer Is not equal or more than 25       ");
//                                        blocks.addBlockPos(pos, timer + 1);
//                                    }
//                                    if (Math.sqrt(pLivingEntity.blockPosition().distToCenterSqr(pos.getX(), pos.getY(), pos.getZ())) > 12.2D) {
//                                        blocks.removeBlock(pos);
//                                        System.out.print("    Block Is Too Far    ");
//                                    }
//                                    System.out.println();
//                                });
//
//
//                            }
//                        }
//                    }
//                }
//
//                if (pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).isPresent()) {
//                    HashMap<Integer, Integer> entitiesFromCap = pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).map(RainyAuraStorage::getEntities).get();
//
//                    System.out.println(entitiesFromCap);
//                    HashMap<Integer, Integer> blocksFromCap20 = (HashMap<Integer, Integer>)entitiesFromCap.clone();
//                    Iterator<Integer> iteratorBlock = blocksFromCap20.keySet().iterator();
//                    while (iteratorBlock.hasNext()) {
//                        int id = iteratorBlock.next();
//                        Entity entity = pLivingEntity.level.getEntity(id);
//                        if (entity != null) {
//                            if (!entity.isOnFire() || (entity.isOnFire() && pLivingEntity.level.dimension() == Level.NETHER && pAmplifier < 1) || !entity.isAlive() || pLivingEntity.distanceTo(entity) > 7) {
//                                pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(blocks -> {
//                                    blocks.removeEntity(id);
//                                });
//                            }else{
//                                pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(blocks -> {
//                                    int timer = blocks.getEntities().get(id);
//                                    if(timer <= 22) {
//                                        blocks.addEntity(id, timer + 1);
//                                    }else{
//                                        entity.clearFire();
//                                        pLivingEntity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.AMBIENT, 0.2F, 1.0F);
//                                        blocks.removeEntity(id);
//                                    }
//
//                                });
//                            }
//
//                        }
//                        else{
//                            pLivingEntity.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(blocks -> {
//                                blocks.removeEntity(id);
//                            });
//                        }
//                    }
//                }
//            }
//        }
//
//    }
//}

@Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {

        ItemStack itemstack = pPlayer.getItemInHand(pHand);

            HitResult hitresult = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.SOURCE_ONLY);
            if (hitresult.getType() == HitResult.Type.MISS && pPlayer.isShiftKeyDown()) {
                if(!pLevel.isClientSide()) itemstack.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(RainyAuraStorage::changeMode);
                return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
            } else {
                if (hitresult.getType() == HitResult.Type.BLOCK) {
                    BlockPos blockpos = ((BlockHitResult)hitresult).getBlockPos();
                    if (!pLevel.mayInteract(pPlayer, blockpos)) {
                        return InteractionResultHolder.pass(itemstack);
                    }

                    if (pLevel.getFluidState(blockpos).is(FluidTags.WATER)) {
                        pLevel.playSound(pPlayer, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                        pLevel.gameEvent(pPlayer, GameEvent.FLUID_PICKUP, blockpos);
                        if(!pLevel.isClientSide()){
                            itemstack.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(capa -> {
                                capa.changeEnergy(EnergyForDifferentActions.EXTINGUISHING_LAVA.amount);
                            });
                        }
                        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
                    }
                }

                return InteractionResultHolder.pass(itemstack);
            }

    }
//    @Nullable
//    @Override
//    public CompoundTag getShareTag(ItemStack stack) {
//        super.getShareTag(stack);
////        return QuiverHolderAttacher.getQuiverHolderUnwrap(stack).serializeNBT(true);
//    }
//
//    @Override
//    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
////        QuiverHolderAttacher.getQuiverHolderUnwrap(stack).deserializeNBT(nbt, true);
//        super.readShareTag(stack,nbt);
//    }
public enum EnergyForDifferentActions{
        ACTIVATION_MINIMUM(500),
        WATER_SHIELD(1000),
    EXTINGUISHING_LAVA(400),
    EXTINGUISHING_THE_FIRE(200),
    EXTINGUISHING_ENTITY(300);
        private long amount;
       EnergyForDifferentActions(long amount){
            this.amount = amount;

        }
}

}
