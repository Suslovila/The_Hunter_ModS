package com.way.suslovila.item.RainyAuraTalisman;

import com.way.suslovila.effects.ModEffects;
import com.way.suslovila.effects.rainyaura.RainyAuraCapProvider;
import com.way.suslovila.effects.rainyaura.RainyAuraStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RainyAuraTalismanItem extends Item {
    Random random = new Random();

    public RainyAuraTalismanItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entityWithAnItem, int slotId, boolean isSelected) {
        if (!entityWithAnItem.level.isClientSide() && stack.getCapability(RainyAuraCapProvider.BLOCKS).isPresent()) {

            LazyOptional<RainyAuraStorage> rainyAuraCapProvider = stack.getCapability(RainyAuraCapProvider.BLOCKS);
            int mode = stack.getCapability(RainyAuraCapProvider.BLOCKS).map(RainyAuraStorage::getMode).get();
//            long energy = stack.getCapability(RainyAuraCapProvider.BLOCKS).map(RainyAuraStorage::getEnergy).get();
            long maxEnergy = rainyAuraCapProvider.map(RainyAuraStorage::getMaxEnergy).get();
            Supplier<Long> getEnergy = () -> stack.getCapability(RainyAuraCapProvider.BLOCKS).map(RainyAuraStorage::getEnergy).get();
            Supplier<Integer> getMode = () -> stack.getCapability(RainyAuraCapProvider.BLOCKS).map(RainyAuraStorage::getMode).get();

            stack.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(capa -> {
                capa.setMaxEnergy(10000);
            });

            HashMap<BlockPos, Integer> blocksFromCap = stack.getCapability(RainyAuraCapProvider.BLOCKS).map(RainyAuraStorage::getMapOfBlocks).get();
            HashMap<Integer, Integer> entitiesFromCap = stack.getCapability(RainyAuraCapProvider.BLOCKS).map(RainyAuraStorage::getEntities).get();
            Supplier<HashMap<BlockPos, Integer>> getBlocksFromCap = () -> stack.getCapability(RainyAuraCapProvider.BLOCKS).map(RainyAuraStorage::getMapOfBlocks).get();
            Supplier<HashMap<Integer, Integer>> getEntitiesFromCap = () -> stack.getCapability(RainyAuraCapProvider.BLOCKS).map(RainyAuraStorage::getEntities).get();

            int modifier = getModifierAccordingToWorld(level);
            System.out.println("Mode: " + mode);
            System.out.println("Actual Energy: " + getEnergy.get());
            System.out.println("MaxEnergy: " + maxEnergy);
            System.out.println("Blocks: " + blocksFromCap);
            System.out.println("Entities: " + entitiesFromCap);
            System.out.println("Modifier: " + modifier);


            //writing info to tag
            CompoundTag tag = stack.getOrCreateTag();
            tag.putLong("energyamount", getEnergy.get());
            tag.putLong("maxenergy", maxEnergy);
            tag.putInt("actualmode", mode);
            ListTag listTag = new ListTag();
            ArrayList<BlockPos> blocksForTag = new ArrayList<>(rainyAuraCapProvider.map(RainyAuraStorage::getMapOfBlocks).get().keySet());
            ArrayList<Integer> entitiesForTag = new ArrayList<>(rainyAuraCapProvider.map(RainyAuraStorage::getEntities).get().keySet());
            for (int i = 0; i < blocksForTag.size(); i++) {
                BlockPos pos = blocksForTag.get(i);
                CompoundTag tag1 = new CompoundTag();
                tag1.putInt("x", pos.getX());
                tag1.putInt("y", pos.getY());
                tag1.putInt("z", pos.getZ());
                listTag.add(tag1);
            }
            tag.put("blocksnear", listTag);

            ListTag listTagForEntity = new ListTag();
            for (int i = 0; i < entitiesForTag.size(); i++) {
                int entity1 = entitiesForTag.get(i);
                CompoundTag tag2 = new CompoundTag();
                tag2.putInt("id", entity1);
                listTagForEntity.add(tag2);
            }
            tag.put("entitynear", listTagForEntity);


            if (getMode.get() == 3) {
                //nothing to do, talisman is off
            }
            if (getMode.get() == 2) {
                //passively collecting water
                if (entityWithAnItem.isInWaterRainOrBubble()) {
                    stack.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(capa -> {
                        capa.addEnergy(1);
                    });
                }
            }
            if (getMode.get() == 1) {
                //protection mode
                if (entityWithAnItem.isInWaterRainOrBubble()) {
                    stack.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(capa -> {
                        capa.addEnergy(1);
                    });
                }
                if (entityWithAnItem.isOnFire()) {
                    if (getEnergy.get() >= EnergyForDifferentActions.WATER_SHIELD.amount * modifier) {
                        entityWithAnItem.clearFire();
                        if (!(entityWithAnItem instanceof LivingEntity)) {
                            stack.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(capa -> {
                                capa.reduceEnergy(EnergyForDifferentActions.EXTINGUISHING_ENTITY.amount * modifier);
                            });
                        } else {
                            if (((LivingEntity) entityWithAnItem).hasEffect(ModEffects.HELLISH_FLAMES.get()))
                                ((LivingEntity) entityWithAnItem).removeEffect(ModEffects.HELLISH_FLAMES.get());
                            ((LivingEntity) entityWithAnItem).addEffect(new MobEffectInstance(ModEffects.RAINY_AURA.get(), 100, 0, false, false));
                            stack.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(capa -> {
                                capa.reduceEnergy(EnergyForDifferentActions.EXTINGUISHING_ENTITY.amount * modifier);
                            });
                        }
                    }
                }
            }
            if (getMode.get() == 0) {
                //full mode
                if (entityWithAnItem.isInWaterRainOrBubble()) {
                    stack.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(capa -> {
                        capa.addEnergy(1);
                    });
                }
                if (entityWithAnItem.isOnFire()) {
                    if (getEnergy.get() >= EnergyForDifferentActions.WATER_SHIELD.amount * modifier) {
                        entityWithAnItem.clearFire();
                        if (!(entityWithAnItem instanceof LivingEntity)) {
                            stack.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(capa -> {
                                capa.reduceEnergy(EnergyForDifferentActions.EXTINGUISHING_ENTITY.amount * modifier);
                            });
                        } else {
                            if (((LivingEntity) entityWithAnItem).hasEffect(ModEffects.HELLISH_FLAMES.get()))
                                ((LivingEntity) entityWithAnItem).removeEffect(ModEffects.HELLISH_FLAMES.get());
                            ((LivingEntity) entityWithAnItem).addEffect(new MobEffectInstance(ModEffects.RAINY_AURA.get(), 100, 0, false, false));
                            stack.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(capa -> {
                                capa.reduceEnergy(EnergyForDifferentActions.WATER_SHIELD.amount * modifier);
                            });
                        }
                    }
                }
            }

            BlockPos checkBlock;
            int distanceXZY = 7;
            for (int x = entityWithAnItem.getBlockX() - distanceXZY; x < entityWithAnItem.getBlockX() + distanceXZY; x++) {
                for (int y = entityWithAnItem.getBlockY() - distanceXZY; y < entityWithAnItem.getBlockY() + distanceXZY; y++) {
                    for (int z = entityWithAnItem.getBlockZ() - distanceXZY; z < entityWithAnItem.getBlockZ() + distanceXZY; z++) {
                        checkBlock = new BlockPos(x, y, z);
                        Block block = entityWithAnItem.level.getBlockState(checkBlock).getBlock();
                        if (!getBlocksFromCap.get().containsKey(checkBlock) && (((block == Blocks.FIRE || block == Blocks.SOUL_FIRE) && getEnergy.get() >= EnergyForDifferentActions.EXTINGUISHING_THE_FIRE.amount * modifier) || (getEnergy.get() >= EnergyForDifferentActions.EXTINGUISHING_LAVA.amount * modifier && block == Blocks.LAVA))) {
                            BlockPos finalCheckBlock = checkBlock;
                            stack.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(blocksInCapa -> {
                                blocksInCapa.addBlockPos(finalCheckBlock, 0);
                            });
                            entityWithAnItem.level.playSound(null, finalCheckBlock.getX(), finalCheckBlock.getY(), finalCheckBlock.getZ(), SoundEvents.WEATHER_RAIN, SoundSource.AMBIENT, 0.5F, 1.0F);
                        }
                    }
                }
            }
            List<Entity> entityList = entityWithAnItem.level.getEntities(entityWithAnItem, new AABB(entityWithAnItem.getX() + 7, entityWithAnItem.getY() + 7, entityWithAnItem.getZ() + 7, entityWithAnItem.getX() - 7, entityWithAnItem.getY() - 7, entityWithAnItem.getZ() - 7));
            for (int i = 0; i < entityList.size(); i++) {
                if (entityList.get(i).isOnFire() && !getEntitiesFromCap.get().containsKey(entityList.get(i).getId()) && getEnergy.get() >= EnergyForDifferentActions.EXTINGUISHING_ENTITY.amount * modifier) {
                    int finalI = i;
                    stack.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(entitiesInCapa -> {
                        entitiesInCapa.addEntity(entityList.get(finalI).getId(), 0);

                    });
                }
            }


            System.out.println(getBlocksFromCap.get());
            HashMap<BlockPos, Integer> blocksFromCap20 = (HashMap<BlockPos, Integer>) getBlocksFromCap.get().clone();
            Iterator<BlockPos> iteratorBlock = blocksFromCap20.keySet().iterator();
            while (iteratorBlock.hasNext()) {
                BlockPos pos = iteratorBlock.next();
                if (pos != null) {
                    Block block = entityWithAnItem.level.getBlockState(pos).getBlock();

                    if (!(block == Blocks.FIRE || block == Blocks.SOUL_FIRE || block == Blocks.LAVA)) {
                        stack.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(blocks -> {
                            blocks.removeBlock(pos);
                        });
                    } else {
                        stack.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(capa -> {
                            System.out.print("Entering Lambda     ");
                            int timer = getBlocksFromCap.get().get(pos);
                            System.out.print("Here is the Timer : " + timer + "     ");
                            if (timer > 22) {
                                capa.removeBlock(pos);
                                if (capa.reduceEnergy(EnergyForDifferentActions.EXTINGUISHING_THE_FIRE.amount * modifier) && (block == Blocks.FIRE || block == Blocks.SOUL_FIRE)) {
                                    entityWithAnItem.level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.AMBIENT, 0.2F, 1.0F);
                                    entityWithAnItem.level.removeBlock(pos, false);
                                }
                                if (block == Blocks.LAVA && (capa.reduceEnergy(EnergyForDifferentActions.EXTINGUISHING_LAVA.amount))) {
                                    entityWithAnItem.level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LAVA_EXTINGUISH, SoundSource.AMBIENT, 0.2F, 1.0F);
                                    entityWithAnItem.level.setBlockAndUpdate(pos, Blocks.OBSIDIAN.defaultBlockState());
                                }
                            } else {
                                System.out.print("      Timer Is not equal or more than 25       ");
                                capa.addBlockPos(pos, timer + 1);
                            }
                            if (Math.sqrt(entityWithAnItem.blockPosition().distToCenterSqr(pos.getX(), pos.getY(), pos.getZ())) > 12.2D) {
                                capa.removeBlock(pos);
                                System.out.print("    Block Is Too Far    ");
                            }
                            System.out.println();
                        });
                    }
                }
            }
            System.out.println(entitiesFromCap);
            HashMap<Integer, Integer> entitiesFromCap20 = (HashMap<Integer, Integer>) getEntitiesFromCap.get().clone();
            Iterator<Integer> iteratorBlockEntity = entitiesFromCap20.keySet().iterator();
            while (iteratorBlockEntity.hasNext()) {
                int id = iteratorBlockEntity.next();
                Entity entityInIterator = entityWithAnItem.level.getEntity(id);
                if (entityInIterator != null) {
                    if (!entityInIterator.isOnFire() || getEnergy.get() < EnergyForDifferentActions.EXTINGUISHING_ENTITY.amount * modifier || !entityInIterator.isAlive() || entityWithAnItem.distanceTo(Objects.requireNonNull(entityInIterator)) > 7) {
                        stack.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(blocks -> {
                            blocks.removeEntity(id);
                        });
                    } else {
                        stack.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(capa -> {
                            int timer = capa.getEntities().get(id);
                            if (timer <= 22) {
                                System.out.println("Timer is less than 22");
                                capa.addEntity(id, timer + 1);
                            } else {
                                System.out.println("Removing Entity, timer > 22");
                                capa.removeEntity(id);
                                if (capa.reduceEnergy(EnergyForDifferentActions.EXTINGUISHING_ENTITY.amount * modifier)) {
                                    entityInIterator.clearFire();
                                    entityWithAnItem.level.playSound(null, entityWithAnItem.getX(), entityWithAnItem.getY(), entityWithAnItem.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.AMBIENT, 0.2F, 1.0F);
                                }
                            }
                        });
                    }
                } else {
                    stack.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(capa -> {
                        capa.removeEntity(id);
                    });
                }
            }
        }
        if (level.isClientSide()) {
            if (stack.getOrCreateTag().getInt("actualmode") == 0) {
                CompoundTag tag = stack.getOrCreateTag();
                ListTag listTagForBlocks = tag.getList("blocksnear", Tag.TAG_COMPOUND);
                for (int i = 0; i < listTagForBlocks.size(); i++) {
                    CompoundTag tag1 = listTagForBlocks.getCompound(i);
                    BlockPos blockPos = new BlockPos(tag1.getInt("x"), tag1.getInt("y"), tag1.getInt("z"));
                    for (int g = 0; g < 2; g++) {
                        double x1 = random.nextDouble(0, 0.5D);
                        double z1 = random.nextDouble(0, 0.5D);
                        if (random.nextBoolean()) {
                            x1 = -x1;
                        }
                        if (random.nextBoolean()) {
                            z1 = -z1;
                        }
                        level.addParticle(ParticleTypes.FALLING_WATER, (double) blockPos.getX() + x1 + 0.5, (double) blockPos.getY() + random.nextDouble(1D, 2D), (double) blockPos.getZ() + z1 + 0.5, 0, 0, 0);
                    }
                }
                ListTag listTagForEntity = tag.getList("entitynear", Tag.TAG_COMPOUND);
                for (int i = 0; i < listTagForEntity.size(); i++) {
                    //            CompoundTag tag1 = listTagForEntity.getCompound(i);
                    //            enttitiesInside.add(listTagForEntity.getCompound(i).getInt("id"));
                    Entity entityInMode = level.getEntity(listTagForEntity.getCompound(i).getInt("id"));
                    if (entityInMode != null) {
                        for (int u = 0; u < 4; u++) {
                            double xDeltaForEntity = random.nextDouble(entityInMode.getBbWidth() / 2, entityInMode.getBbWidth() / 2 + 0.7D);
                            double zDeltaForEntity = random.nextDouble(entityInMode.getBbWidth() / 2, entityInMode.getBbWidth() / 2 + 0.7D);
                            if (random.nextBoolean()) {
                                xDeltaForEntity = -xDeltaForEntity;
                            }
                            if (random.nextBoolean()) {
                                zDeltaForEntity = -zDeltaForEntity;
                            }
                            double yHeighForEntity = entityInMode.getBbHeight();
                            double yDeltaForEntity = random.nextDouble(yHeighForEntity + 0.5D);
                            if (random.nextBoolean()) {
                                yDeltaForEntity = random.nextDouble(yHeighForEntity + 0.7d, yHeighForEntity + 1.5D);
                            }
                            entityWithAnItem.level.addParticle(ParticleTypes.FALLING_WATER, entityInMode.getX() + xDeltaForEntity, entityInMode.getY() + yDeltaForEntity, entityInMode.getZ() + zDeltaForEntity, 0, 0, 0);

                        }
                    }
                }
            }
        }
    }

    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pLevel != null) {

        }
    }



    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {

        ItemStack itemstack = pPlayer.getItemInHand(pHand);

        HitResult hitresult = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.SOURCE_ONLY);
        if (hitresult.getType() == HitResult.Type.MISS && pPlayer.isShiftKeyDown()) {
            if (!pLevel.isClientSide())
                itemstack.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(RainyAuraStorage::changeMode);
            return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
        } else {
            if (hitresult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockpos = ((BlockHitResult) hitresult).getBlockPos();
                if (!pLevel.mayInteract(pPlayer, blockpos)) {
                    return InteractionResultHolder.pass(itemstack);
                }
                if (pLevel.getFluidState(blockpos).is(FluidTags.WATER)) {
                    pLevel.playSound(pPlayer, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                    pLevel.gameEvent(pPlayer, GameEvent.FLUID_PICKUP, blockpos);
                    if (!pLevel.isClientSide()) {
                        itemstack.getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(capa -> {
                            capa.addEnergy(EnergyForDifferentActions.EXTINGUISHING_LAVA.amount);
                        });
                        pLevel.removeBlock(blockpos, false);
                    }
                    return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
                }
            }
            return InteractionResultHolder.pass(itemstack);
        }
    }

    public enum EnergyForDifferentActions {
        ACTIVATION_MINIMUM(500),
        WATER_SHIELD(1000),
        EXTINGUISHING_LAVA(400),
        EXTINGUISHING_THE_FIRE(200),
        EXTINGUISHING_ENTITY(300);
        public long amount;

        EnergyForDifferentActions(long amount) {
            this.amount = amount;

        }
    }

    public int getModifierAccordingToWorld(Level level) {
        return level.dimension() == Level.NETHER ? 2 : 1;
    }
}
