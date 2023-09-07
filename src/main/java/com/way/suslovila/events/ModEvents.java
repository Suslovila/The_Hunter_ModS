
package com.way.suslovila.events;

import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.entity.EntityShadowMonster.ShadowMonsterEntity;
import com.way.suslovila.entity.bag.BagProvider;
import com.way.suslovila.entity.bag.HunterBagEntity;
import com.way.suslovila.entity.bag.HunterBagEntityItemsStorage;
import com.way.suslovila.capability.EntityCapabilityProvider;
import com.way.suslovila.capability.EntityCapabilityStorage;
import com.way.suslovila.effects.ModEffects;
import com.way.suslovila.effects.WaterShieldEffect;
import com.way.suslovila.effects.rainyaura.RainyAuraCapProvider;
import com.way.suslovila.effects.rainyaura.RainyAuraStorage;
import com.way.suslovila.entity.ModEntityTypes;
import com.way.suslovila.entity.hunter.HunterEntity;
import com.way.suslovila.entity.hunter.appearance.HunterAppearanceFormEntity;
import com.way.suslovila.entity.hunter.teleport.HunterTeleportFormEntity;
import com.way.suslovila.entity.trap.TrapEntity;
import com.way.suslovila.item.RainyAuraTalisman.RainyAuraTalismanItem;
import com.way.suslovila.savedData.*;
import com.way.suslovila.savedData.clientSynch.ClientVictimData;
import com.way.suslovila.savedData.clientSynch.MessageIsVictim;
import com.way.suslovila.savedData.clientSynch.MessageWaterShield;
import com.way.suslovila.savedData.clientSynch.Messages;

import com.way.suslovila.sounds.HuntThemePlayer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

import static com.way.suslovila.entity.projectile.explosionArrow.ExplosionArrow.random;
import static com.way.suslovila.item.bag.ItemHunterBag.putItemsInBackpackFromHunterStorage;
import static net.minecraft.world.entity.Entity.RemovalReason.DISCARDED;

@Mod.EventBusSubscriber(modid = MysticalCreatures.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void checkIfEntityIsAVictimEvent(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().level.isClientSide() && event.getEntityLiving() instanceof Player) {
            if ((ClientVictimData.getVictim() != null) && event.getEntityLiving().level.getPlayerByUUID(ClientVictimData.getVictim()) != null) {
                if (Objects.equals(event.getEntityLiving().level.getPlayerByUUID(ClientVictimData.getVictim()), Minecraft.getInstance().player))
                    HuntThemePlayer.playBossMusic();
            }else if (random.nextInt(0, 5) == 4)
                    HuntThemePlayer.stopBossMusic();
                }


//            Messages.sendWaterShield(new MessageWaterShield(event.getEntityLiving().hasEffect(ModEffects.WATER_SHIELD.get())), event.getEntityLiving());
            LivingEntity entity = event.getEntityLiving();
        if (!entity.level.isClientSide()) {
            if(entity.hasEffect(ModEffects.WATER_SHIELD.get())){
              ModEvents.extinguishAll(entity.getBoundingBox(), entity);
              ModEvents.extinguishUnder(entity.getBoundingBox(), entity);
//                AABB entityAABB1 = entity.getBoundingBox();
//                AABB aabb = new AABB(entityAABB1.minX, entityAABB1.minY -1, entityAABB1.minZ, entityAABB1.maxX, entityAABB1.minY, entityAABB1.maxZ);
//                BlockPos posUnderEntity = new BlockPos(entity.getBlockX(), entity.getBlockY()-1, entity.getBlockZ());
//                if(entity.level.getBlockState(posUnderEntity).getMaterial() == Material.LAVA){
//                    entity.level.setBlockAndUpdate(posUnderEntity, Blocks.OBSIDIAN.defaultBlockState());
//                    entity.level.playSound(null, posUnderEntity.getX(), posUnderEntity.getY(), posUnderEntity.getZ(), SoundEvents.LAVA_EXTINGUISH, SoundSource.AMBIENT, 0.2F, 1.0F);
//                    for(int i = 0; i< 20; i++) {
//                        ((ServerLevel) entity.level).sendParticles(ParticleTypes.LARGE_SMOKE, posUnderEntity.getX(), posUnderEntity.getY()+1, posUnderEntity.getZ(), 1,  random.nextDouble(-0.2, 0.2), random.nextDouble(0, 0.2), random.nextDouble(-0.2, 0.2), random.nextDouble(-0.2, 0.2));
//                    }
                //}
                if(entity.isOnFire())
                entity.clearFire();
                //AABB entityAABB = entity.getBoundingBox();
                //entity.getBbWidth();
                // ((ServerLevel)entity.level).sendParticles(ParticleTypes.FALLING_WATER, entity.getX() + random.nextDouble(entityAABB.minX, entityAABB.maxX), entity.getY() + random.nextDouble(entityAABB.minY, entityAABB.maxY),  entity.getZ() + random.nextDouble(entityAABB.minZ, entityAABB.maxZ), 1, 0, 0, 0, 0);
            }
            if(entity.tickCount % 5 ==0 )
            Messages.sendWaterShield(new MessageWaterShield(entity.hasEffect(ModEffects.WATER_SHIELD.get()), entity.getId()), entity);

            if (!SaveVictim.get(entity.level).getVictim().equals("novictim") && (SaveVictim.get(entity.level).getVictim() != null) && ((ServerLevel)entity.level).getEntity(UUID.fromString(SaveVictim.get(entity.level).getVictim())) != null){
                Messages.sendWaterShield(new MessageIsVictim(entity.getUUID().equals(UUID.fromString(SaveVictim.get(entity.level).getVictim())), entity.getId()), entity);
//                if (entity.getUUID().equals(UUID.fromString(SaveVictim.get(entity.level).getVictim()))) {
//                    entity.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 2000, 3));
//                    if (entity instanceof Player && ((Player) event.getEntityLiving()).getSleepTimer() == 99) {
////                    ((ServerPlayer)event.getEntityLiving()).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.GUARDIAN_ELDER_EFFECT, this.isSilent() ? 0.0F : 1.0F));
//                    }
//                }
            }

        }
        else {
//            if (Minecraft.getInstance().getMusicManager().isPlayingMusic(ModSounds.HUNT_THEME_MUSIC.get()) && (event.getEntityLiving().getCapability(EntityCapabilityProvider.BLOCKS).isPresent() && !event.getEntityLiving().getCapability(EntityCapabilityProvider.BLOCKS).map(EntityCapabilityStorage::getIsVictim).get()))
//                Minecraft.getInstance().getMusicManager().stopPlaying();
//            else {
//                if (random.nextInt(400) == 300 && !Minecraft.getInstance().getMusicManager().isPlayingMusic(ModSounds.HUNT_THEME_MUSIC.get()) && (event.getEntityLiving().getCapability(EntityCapabilityProvider.BLOCKS).isPresent() && event.getEntityLiving().getCapability(EntityCapabilityProvider.BLOCKS).map(EntityCapabilityStorage::getIsVictim).get())) {
//                    Minecraft.getInstance().getMusicManager().stopPlaying();
//                    Minecraft.getInstance().getMusicManager().startPlaying(ModSounds.HUNT_THEME_MUSIC.get());
//                }
//            }
        }
//        if (entity.level.isClientSide() && event.getEntityLiving() instanceof Player && event.getEntityLiving().getCapability(EntityCapabilityProvider.BLOCKS).isPresent()) {
//            System.out.println(event.getEntityLiving().getCapability(EntityCapabilityProvider.BLOCKS).map(EntityCapabilityStorage::getHasWaterShield).get());
//        }
    }
//@SubscribeEvent
//public static void mouse(InputEvent.RawMouseEvent event){
//        LocalPlayer player = Minecraft.getInstance().player;
//    if(player != null && Minecraft.getInstance().player.isPassenger()){
//            if(player.getVehicle() instanceof ShadowGrabEntity){
//              //  event.setCanceled(true);
//
//            }
//    }
//}
    @SubscribeEvent
    public static void stuffForExplosionArrow(EntityLeaveWorldEvent event) {
//
//        if (event.getEntity() instanceof ExplosionArrow) {
//            BlockPos pos = event.getEntity().blockPosition();
//            ExplosionArrow arrow = (ExplosionArrow) event.getEntity();
//            for (int x = pos.getX() - 5; x < pos.getX() + 5; x++) {
//                for (int y = pos.getY() - 5; y < pos.getY() + 5; y++) {
//                    for (int z = pos.getZ() - 5; z < pos.getZ() + 5; z++) {
//
//                        BlockPos checkPos = new BlockPos(x, y, z);
//                        Vec3 vec = new Vec3(checkPos.getX() - arrow.getX(), checkPos.getY() - arrow.getY(), checkPos.getZ() - arrow.getZ());
//
//                        if (vec.length() <= 5 && !(arrow.level.getBlockState(checkPos).getBlock().getExplosionResistance() >= Blocks.OBSIDIAN.getExplosionResistance()) && !arrow.level.getBlockState(checkPos).isAir()) {
//                            if (!arrow.level.isClientSide()) {
//                                arrow.level.setBlockAndUpdate(checkPos, Blocks.AIR.defaultBlockState());
//                            } else {
////                               if(random.nextInt(8) == 7){
////                                   arrow.level.addParticle(ModParticles.DISSOLATION_LIGHTNING_PARTICLES.get(),
////                                           checkPos.getX(), checkPos.getY(), checkPos.getZ(),
////                                           0, 0,0);
//
//                                int g = 0;
//                                System.out.println(arrow.level.getBlockState(checkPos));
//                                while (g < 8) {
//                                    arrow.level.addParticle(ModParticles.DISSOLATION_PARTICLES.get(),
//                                            checkPos.getX(), checkPos.getY(), checkPos.getZ(),
//                                            random.nextDouble(-0.25d, 0.25d), random.nextDouble(-0.2d, 0.2d), random.nextDouble(-0.2d, 0.2d));
//                                    g++;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }

    @SubscribeEvent
    public static void ifPlayerAttacksHunter(AttackEntityEvent event) {
        if (!event.getTarget().level.isClientSide() && event.getTarget() instanceof Shulker) {
            SaveVictim.get(event.getPlayer().level).changeVictim("novictim");
        }
    }
    @SubscribeEvent
    public static void ifHunterIsAttackedWhenNotVulnarable(LivingHurtEvent event){
        if(!event.getEntityLiving().level.isClientSide() && event.getEntityLiving().hasEffect(ModEffects.WATER_SHIELD.get())){
            DamageSource source = event.getSource();
            if(source.isFire())
                event.setAmount(0);
            else{
             if (!source.isBypassInvul() && !source.isMagic() && !source.isBypassArmor())
              event.setAmount((float) (event.getAmount() - event.getAmount() * 0.15 * (source.isProjectile() ? 2: 1) * (event.getEntityLiving().getEffect(ModEffects.WATER_SHIELD.get()).getAmplifier() + 1)));
           }
       }
    }

    /* @SubscribeEvent
     public static void playerKilledByHunter(LivingDeathEvent event) {
         if (!event.getEntityLiving().level.isClientSide()) {
             if (event.getEntityLiving() instanceof Player && event.getEntityLiving().getLastHurtByMob() instanceof Zombie) {
                 NonNullList<ItemStack> playerItems = ((Player) (event.getEntityLiving())).getInventory().items;
                 NonNullList<ItemStack> playerArmor = ((Player) (event.getEntityLiving())).getInventory().armor;
                 NonNullList<ItemStack> playerHand = ((Player) (event.getEntityLiving())).getInventory().offhand;
                 HunterBagData.get(event.getEntityLiving().level).addItemsToBag(playerItems);
                 HunterBagData.get(event.getEntityLiving().level).addItemsToBag(playerArmor);
                 HunterBagData.get(event.getEntityLiving().level).addItemsToBag(playerHand);
                 Random random = new Random();
                 int chance = random.nextInt(10);
                 if (chance == 5) {
                     Player player = ((Player) (event.getEntityLiving()));
                     String name = (player.getName()).getString();
                     ItemStack skullOfPlayer = new ItemStack(Items.PLAYER_HEAD);
                     skullOfPlayer.getOrCreateTag().putString("SkullOwner", Objects.requireNonNull(ChatFormatting.stripFormatting(name)));
                     NonNullList<ItemStack> skull = NonNullList.create();
                     skull.add(skullOfPlayer);
                     HunterBagData.get(event.getEntityLiving().level).addItemsToBag(skull);
                 }

                 for (int i = 0; i < ((Player) (event.getEntityLiving())).getInventory().getContainerSize(); ++i) {
                     ItemStack itemstack = ((Player) (event.getEntityLiving())).getInventory().getItem(i);
                     if (!itemstack.isEmpty()) {
                         ((Player) (event.getEntityLiving())).getInventory().removeItemNoUpdate(i);
                     }
                 }

             }
         }

     }
 */
    @SubscribeEvent
    public static void IfHunterSpawnsEvent(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof HunterEntity && !event.getEntity().level.isClientSide()) {
            ServerLevel level = (ServerLevel)event.getEntity().level;
            HunterAmountData data = HunterAmountData.get(level);
            if (data.getPreviousHunter() != null && (level).getEntity(data.getPreviousHunter()) != null && level.getEntity(data.getPreviousHunter()).isAlive()) {
                HunterAmountData.get(level).killPreviousHunter(level);
            }
            HunterAmountData.get(level).setPreviousHunter(event.getEntity().getUUID());

//            if (HuntersHP.get(level).getHunterHP() > 0) {
//                ((HunterEntity)event.getEntity()).setHealth((float)HuntersHP.get(level).getHunterHP());
//            }
            //HunterEntity.possibleActions.put("vulnarable", (hunter)-> {((HunterEntity)hunter).setVulnarable(false);});
        }
    }
//    @SubscribeEvent
//    public static void WaterShield(EntityEvent.EnteringSection event) {
////      if(event.getEntity() instanceof LivingEntity && !event.getEntity().level.isClientSide()) Messages.sendWaterShield(new MessageWaterShield(((LivingEntity)event.getEntity()).hasEffect(ModEffects.WATER_SHIELD.get()), event.getEntity().getId()), (LivingEntity)event.getEntity());
//
//
//    }

    @SubscribeEvent
    public static void IfHunterBagSpawnsEvent(EntityJoinWorldEvent event) {

            //Messages.sendWaterShield(new MessageWaterShield(((LivingEntity)event.getEntity()).hasEffect(ModEffects.WATER_SHIELD.get()), ((LivingEntity)event.getEntity()).getId()), ((LivingEntity)event.getEntity()));

        if (!event.getEntity().level.isClientSide()) {
            if (event.getEntity() instanceof HunterBagEntity) {
                event.getEntity().setNoGravity(false);
                System.out.println("No gravity");
                HunterBagEntity bagEntity = (HunterBagEntity) event.getEntity();
                ItemStack itemStack = MysticalCreatures.COMMONBACKPACK.get().getDefaultInstance();
                putItemsInBackpackFromHunterStorage(itemStack, bagEntity.level);
                bagEntity.getCapability(BagProvider.BAG).ifPresent(items -> {
                    items.setBag(itemStack);
                });
            }
        }
    }


    @SubscribeEvent
    public static void RightClickIfCaptured(PlayerInteractEvent.RightClickBlock event) {
        if (event.getPlayer().isPassenger() && event.getPlayer().getVehicle() instanceof TrapEntity) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void RightClickEntityIfCaptured(PlayerInteractEvent.EntityInteract event) {
        if (!event.getPlayer().level.isClientSide() && event.getPlayer().isPassenger() && event.getPlayer().getVehicle() instanceof TrapEntity) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void RightClickEntityIfCapturedSpec(PlayerInteractEvent.EntityInteractSpecific event) {
        if (!event.getPlayer().level.isClientSide() && event.getPlayer().isPassenger() && event.getPlayer().getVehicle() instanceof TrapEntity) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void playerKilled(LivingDropsEvent event) {
        DamageSource trap = new DamageSource(MysticalCreatures.MOD_ID + "shadow_trap");
        DamageSource clamp = new DamageSource(MysticalCreatures.MOD_ID + "pressure");
        if ((!event.getEntityLiving().level.isClientSide() && event.getEntityLiving() instanceof Player) && ((event.getEntityLiving().getLastHurtByMob() instanceof Zombie) || event.getSource().getMsgId() == trap.getMsgId() || event.getSource().getMsgId() == clamp.getMsgId())) {
            NonNullList<ItemStack> itemStacks = NonNullList.create();
            ArrayList<ItemEntity> items = (ArrayList<ItemEntity>) event.getDrops();
            for (int i = 0; i < items.size(); i++) {
                ItemStack item = items.get(i).getItem();
                itemStacks.add(item);
                items.get(i).remove(DISCARDED);
            }
            HunterBagData.get(event.getEntityLiving().level).addItemsToBag(itemStacks);
            Random random = new Random();
            int chance = random.nextInt(10);
            if (chance == 5) {
                Player player = ((Player) (event.getEntityLiving()));
                String name = (player.getName()).getString();
                ItemStack skullOfPlayer = new ItemStack(Items.PLAYER_HEAD);
                skullOfPlayer.getOrCreateTag().putString("SkullOwner", Objects.requireNonNull(ChatFormatting.stripFormatting(name)));
                NonNullList<ItemStack> skull = NonNullList.create();
                skull.add(skullOfPlayer);
                HunterBagData.get(event.getEntityLiving().level).addItemsToBag(skull);

            }
        }
    }

    @SubscribeEvent
    public static void ForbidTravelToDimension(EntityTravelToDimensionEvent event) {

        boolean t = false;
        if (event.getEntity() instanceof LivingEntity) {
            if (((LivingEntity) event.getEntity()).hasEffect(ModEffects.ENDER_SEAL.get())) {
                event.setCanceled(true);
                t = true;
            }
        }
        if (!t) {
            Level level = event.getEntity().level;
            double x = event.getEntity().getX();
            double y = event.getEntity().getY();
            double z = event.getEntity().getZ();
            boolean anyhasEffect = false;
            List<Entity> entities = level.getEntities(event.getEntity(), new AABB(x - 30.0D, y - 30.0D, z - 30.0D, x + 30.0D, y + 30.0D, z + 30.0D));
            for (int i = 0; i < entities.size(); i++) {
                if (entities.get(i) instanceof LivingEntity) {
                    if (((LivingEntity) entities.get(i)).hasEffect(ModEffects.ENDER_FRACTURE.get())) {
                        anyhasEffect = true;
                    }
                }
            }
            if (anyhasEffect) {
                DamageSource fracture = (new DamageSource("fracture")).bypassArmor().bypassInvul();
                event.getEntity().hurt(fracture, 5);
                BlockPos position = event.getEntity().blockPosition();
                BlockPos newPos = new BlockPos(position.getX(), position.getY() + ((double) event.getEntity().getDimensions(event.getEntity().getPose()).height), position.getZ());
                event.getEntity().level.levelEvent(2003, newPos, 0);
                event.setCanceled(true);
            }
        }
    }


    @SubscribeEvent
    public static void ForbidUseEnderPearl(EntityTeleportEvent event) {
        if (!event.getEntity().level.isClientSide()) {
            if (event.getEntity() instanceof LivingEntity) {

                if (((LivingEntity) event.getEntity()).hasEffect(ModEffects.ENDER_SEAL.get())) {
                    event.setCanceled(true);
                } else {
                    Level level = event.getEntity().level;
                    double x = event.getEntity().getX();
                    double y = event.getEntity().getY();
                    double z = event.getEntity().getZ();
                    boolean anyhasEffect = false;
                    List<Entity> entities = level.getEntities(event.getEntity(), new AABB(x - 30.0D, y - 30.0D, z - 30.0D, x + 30.0D, y + 30.0D, z + 30.0D), EntitySelector.LIVING_ENTITY_STILL_ALIVE);
                    for (int i = 0; i < entities.size(); i++) {
                        if (entities.get(i) instanceof LivingEntity) {
                            if (((LivingEntity) entities.get(i)).hasEffect(ModEffects.ENDER_FRACTURE.get())) {
                                anyhasEffect = true;
                            }
                        }
                    }
                    if (anyhasEffect) {
                        DamageSource fracture = (new DamageSource("fracture")).bypassArmor().bypassInvul();
                        event.getEntity().hurt(fracture, 5);
                        BlockPos position = event.getEntity().blockPosition();
                        BlockPos newPos = new BlockPos(position.getX(), position.getY() + ((double) event.getEntity().getDimensions(event.getEntity().getPose()).height), position.getZ());
                        event.getEntity().level.levelEvent(2003, newPos, 0);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public static void protectFromFireIfHasEffect(LivingAttackEvent event) {
        if ((event.getEntityLiving().hasEffect(ModEffects.PHOENIX_FEATHRING.get()) || event.getEntityLiving().hasEffect(ModEffects.WATER_SHIELD.get())) && event.getSource().isFire()) {
            event.setCanceled(true);
        }
    }


    @SubscribeEvent
    public static void rebirth(LivingDeathEvent event) {
        if (event.getEntityLiving().hasEffect(ModEffects.PHOENIX_FEATHRING.get())) {
            if (Objects.requireNonNull(event.getEntityLiving().getEffect(ModEffects.PHOENIX_FEATHRING.get())).getAmplifier() >= 2) {
                LivingEntity entity = event.getEntityLiving();
                Level level = event.getEntityLiving().level;
                int distance = 14;
                BlockPos checkPos;

                boolean flag = true;
                for (int x = event.getEntityLiving().getBlockX() - distance; x < event.getEntityLiving().getBlockX() + distance && flag; x++) {
                    for (int y = event.getEntityLiving().getBlockY() - distance; y < event.getEntityLiving().getBlockY() + distance && flag; y++) {
                        for (int z = event.getEntityLiving().getBlockZ() - distance; z < event.getEntityLiving().getBlockZ() + distance && flag; z++) {
                            checkPos = new BlockPos(x, y, z);
                            if (level.getBlockState(checkPos).getBlock() == Blocks.FIRE || level.getBlockState(checkPos).getBlock() == Blocks.SOUL_FIRE) {
                                event.setCanceled(true);
                                level.removeBlock(checkPos, false);
                                entity.teleportTo(x, y, z);
                                entity.setHealth(entity.getMaxHealth());
                                flag = false;
                                entity.removeAllEffects();
                                entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 1200, 4));
                                entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1200, 0));
                                entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1200, 1));
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void putItemsToHunterBagOnPlayerDeath(LivingDeathEvent event) {

    }
    @SubscribeEvent
    public static void spawnHunterBagOnHunterDeath(LivingDeathEvent event) {
        Level world = event.getEntityLiving().level;
        if(!world.isClientSide && event.getEntityLiving() instanceof HunterEntity){
            HunterBagEntity shadowMonster = new HunterBagEntity(ModEntityTypes.HUNTER_BAG.get(), world);
            shadowMonster.setPos(event.getEntityLiving().position());
            world.addFreshEntity(shadowMonster);
        }
    }
    @SubscribeEvent
    public static void Hunt(TickEvent.WorldTickEvent event) {
        //handling hunt theme music

        if (!event.world.isClientSide()) {
            if (SaveVictim.get(event.world).getVictim() == null) {
                SaveVictim.get(event.world).changeVictim("novictim");
            }
            if (event.phase == TickEvent.Phase.START) {
                Level level = event.world;
                    if (HuntTime.get(level).getHuntTime() <= 0 && !SaveVictim.get(level).getVictim().equals("novictim"))

                        SaveVictim.get(level).changeVictim("novictim");

                    if (!SaveVictim.get(level).getVictim().equals("novictim")){
                    if (level.dimension() == Level.OVERWORLD) {
                        HuntTime.get(event.world).reduceTime();
                    }
                    if (((ServerLevel) level).getEntity(UUID.fromString(SaveVictim.get(level).getVictim())) != null) {
                        int chance = 0;
                        if (HunterAmountData.get(level).getPreviousHunter() == null || ((ServerLevel) level).getEntity(HunterAmountData.get(level).getPreviousHunter()) == null || (((ServerLevel) level).getEntity(HunterAmountData.get(level).getPreviousHunter()) != null && !((ServerLevel) level).getEntity(HunterAmountData.get(level).getPreviousHunter()).isAlive()))
                            chance = 100;
                        else
                            chance = 3000;
                        if (event.world.random.nextInt(chance) == 50)
                            DelayBeforeSpawningHunter.get(level).changeTime(HunterTeleportFormEntity.lifeTime + HunterAppearanceFormEntity.lifeTime + 5);

                        if (DelayBeforeSpawningHunter.get(level).getHunterDelay() > 0)
                            DelayBeforeSpawningHunter.get(level).changeTime(-1);

                        if (DelayBeforeSpawningHunter.get(level).getHunterDelay() == 0) {
                            ModEvents.summonHunter(level);
                            DelayBeforeSpawningHunter.get(level).changeTime(-1);


                            System.out.println(DelayBeforeSpawningHunter.get(level.getServer().overworld()).getHunterDelay());
                        }
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public static void onAttachCapabilitiesBag(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof HunterBagEntity) {
            if (!event.getObject().getCapability(BagProvider.BAG).isPresent()) {
                // The bag does not already have this capability so we need to add the capability provider here
                event.addCapability(new ResourceLocation(MysticalCreatures.MOD_ID, "bag"), new BagProvider());
            }
        }
        if (event.getObject() != null) {
            if (!event.getObject().getCapability(RainyAuraCapProvider.BLOCKS).isPresent()) {
                event.addCapability(new ResourceLocation(MysticalCreatures.MOD_ID, "blocksforrainyaura"), new RainyAuraCapProvider());
            }
            if (!event.getObject().getCapability(EntityCapabilityProvider.BLOCKS).isPresent()) {
                event.addCapability(new ResourceLocation(MysticalCreatures.MOD_ID, "capaforentity"), new EntityCapabilityProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesItems(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() instanceof RainyAuraTalismanItem) {
            event.addCapability(new ResourceLocation(MysticalCreatures.MOD_ID, "rainyauratalisman"), new RainyAuraCapProvider());
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(HunterBagEntityItemsStorage.class);
        event.register(RainyAuraStorage.class);
        event.register(EntityCapabilityStorage.class);
    }



    @SubscribeEvent
    public static void clearRainyAuraCAPA(PotionEvent.PotionRemoveEvent event) {
        if (event.getPotion() instanceof WaterShieldEffect) {
            event.getEntityLiving().getCapability(RainyAuraCapProvider.BLOCKS).ifPresent(RainyAuraStorage::clearAll);
            System.out.println("Clearing CAPA");
        }
    }


    @SubscribeEvent
    public static void onAddPotionEffect(PotionEvent.PotionAddedEvent event) {
       if (event.getPotionEffect().getEffect().equals(ModEffects.WATER_SHIELD.get())) Messages.sendWaterShield(new MessageWaterShield(true, event.getEntityLiving().getId()), event.getEntityLiving());

    }

    @SubscribeEvent
    public static void  onRemovePotionEffect(PotionEvent.PotionRemoveEvent event) {
        if (Objects.requireNonNull(event.getPotionEffect()).getEffect().equals(ModEffects.WATER_SHIELD.get())) Messages.sendWaterShield(new MessageWaterShield(false, event.getEntityLiving().getId()), event.getEntityLiving());
    }

    @SubscribeEvent
    public static void  onPotionEffectExpire(PotionEvent.PotionExpiryEvent event) {
        boolean s = event.getEntityLiving().level.isClientSide();
      if (Objects.requireNonNull(event.getPotionEffect()).getEffect().equals(ModEffects.WATER_SHIELD.get())) Messages.sendWaterShield(new MessageWaterShield(false, event.getEntityLiving().getId()), event.getEntityLiving());
    }
private static void summonHunter(Level level) {
    Player victim = level.getPlayerByUUID(UUID.fromString(SaveVictim.get(level).getVictim()));
    if (victim != null) {
        int maxDistanceXZ = 30;
        int maxDistanceY = 15;
        ArrayList<BlockPos> speedArrowList = new ArrayList<BlockPos>();
        ArrayList<BlockPos> explArrowList = new ArrayList<BlockPos>();
        ArrayList<BlockPos> shadowLightningList = new ArrayList<BlockPos>();
        for (int x = victim.getBlockX() - maxDistanceXZ; (x < victim.getBlockX() + maxDistanceXZ); x++) {
            for (int z = victim.getBlockZ() - maxDistanceXZ; (z < victim.getBlockZ() + maxDistanceXZ); z++) {
                for (int y = victim.getBlockY() - maxDistanceY; (y < victim.getBlockY() + maxDistanceY); y++) {
                    if (!level.hasNearbyAlivePlayer(x, y, z, 23)) {
                        BlockPos checkPos = new BlockPos(x, y, z);
                        BlockPos down1block = new BlockPos(x, y - 1, z);
                        float brightness = level.hasChunkAt(checkPos.getX(), checkPos.getZ()) ? level.getBrightness(new BlockPos(checkPos.getX(), checkPos.getY(), checkPos.getZ())) : 0.0F;
                        boolean bool = level.clip(new ClipContext(new Vec3(victim.getX(), victim.getEyeY(), victim.getZ()), new Vec3(checkPos.getX(), checkPos.getY(), checkPos.getZ()), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, victim)).getType() == HitResult.Type.MISS;
                        if (((PreviousHunterPosition.get(level).getPreviousHunterPos() != null && Math.sqrt(PreviousHunterPosition.get(level).getPreviousHunterPos().distToCenterSqr(checkPos.getX(), checkPos.getY(), checkPos.getZ())) > 13) || PreviousHunterPosition.get(level).getPreviousHunterPos() == null) && level.getBlockState(down1block).getMaterial().blocksMotion() && !level.getBlockState(down1block).isAir() && level.getBlockState(checkPos).isAir() && level.getBlockState(new BlockPos(x, y + 1, z)).isAir() && level.getBlockState(new BlockPos(x, y + 2, z)).isAir() && level.getBlockState(new BlockPos(x, y + 3, z)).isAir() && brightness <= HunterEntity.maxLight) {
                            if (bool)
                                speedArrowList.add(checkPos);
                            else if (!HunterEntity.checkConditionForExplosionArrow(level, new BlockPos(checkPos.getX(), checkPos.getY() + 2, checkPos.getZ()), victim.eyeBlockPosition()))
                                explArrowList.add(checkPos);
                            else
                                shadowLightningList.add(checkPos);

                        }
                    }
                }
            }
        }
        ArrayList<ArrayList<BlockPos>> iterList = new ArrayList<>();
        iterList.add(speedArrowList);
        iterList.add(explArrowList);
        iterList.add(shadowLightningList);
        for (int k = 0; k < iterList.size(); k++) {
            ArrayList<BlockPos> iterableList = iterList.get(k);
            if (!iterableList.isEmpty()) {
                Vec3 viewVector = victim.getLookAngle();
                BlockPos finalPos;
                double maxAngle = 0;
                double floor = random.nextDouble(Math.PI/2+0.3, Math.PI);
                BlockPos bestBlockPos = null;
                for (int i = 0; i < iterableList.size(); i++) {
                    BlockPos pos = iterableList.get(i);
                    double angle = angleBetweenVec3(viewVector, victim.position().subtract(new Vec3(pos.getX(), pos.getY(), pos.getZ())).reverse());
                    if (angle > maxAngle && angle < floor) {
                        bestBlockPos = pos;
                        maxAngle = angle;
                    }
                }
                if (maxAngle != 0)
                    finalPos = bestBlockPos;
                else
                    finalPos = iterableList.get(random.nextInt(iterableList.size()));

                HunterAppearanceFormEntity appearanceForm = new HunterAppearanceFormEntity(ModEntityTypes.HUNTER_APPEAR_FORM.get(), level);
                appearanceForm.moveTo(finalPos.getX() + 0.5, finalPos.getY(), finalPos.getZ() + 0.5, 0, 0);
                level.addFreshEntity(appearanceForm);
                PreviousHunterPosition.get(level).setPreviousHunterPos(finalPos);

                return;
            }
        }
    }
}
public static double angleBetweenVec3(Vec3 vec1, Vec3 vec2){
    return Math.acos(vec1.dot(vec2)/vec1.length()/vec2.length());
}
    private static void extinguishAll(AABB pArea, LivingEntity entity) {
        int i = Mth.floor(pArea.minX);
        int j = Mth.floor(pArea.minY);
        int k = Mth.floor(pArea.minZ);
        int l = Mth.floor(pArea.maxX);
        int i1 = Mth.floor(pArea.maxY);
        int j1 = Mth.floor(pArea.maxZ);
        for(int rt = 0; rt < 2; rt++)
        ((ServerLevel)entity.level).sendParticles(ParticleTypes.FALLING_WATER, entity.getX() + random.nextDouble(-entity.getBbWidth()/2, entity.getBbWidth()/2), entity.getY() + random.nextDouble(0, entity.getBbHeight()/2),  entity.getZ() + random.nextDouble(-entity.getBbWidth()/2, entity.getBbWidth()/2), 1, 0, 0, 0, 0);
        for(int k1 = i; k1 <= l; ++k1) {
            for(int l1 = j; l1 <= i1; ++l1) {
                for(int i2 = k; i2  <= j1; ++i2) {
                    BlockPos blockpos = new BlockPos(k1, l1, i2);
                    BlockState blockstate = entity.level.getBlockState(blockpos);
                    if (!blockstate.isAir()){
                        if(blockstate.getMaterial() == Material.LAVA){
                            entity.level.setBlockAndUpdate(blockpos, Blocks.AIR.defaultBlockState());
                            entity.level.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.LAVA_EXTINGUISH, SoundSource.AMBIENT, 0.2F, 1.0F);
                        }
                        if(blockstate.getMaterial() == Material.FIRE){
                            entity.level.setBlockAndUpdate(blockpos, Blocks.AIR.defaultBlockState());
                            entity.level.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.AMBIENT, 0.2F, 1.0F);
                        }
                    }
                }
            }
        }
    }

    private static void extinguishUnder(AABB pArea, LivingEntity entity) {
        double delta = 0D;
        int i = Mth.floor(pArea.minX- delta);
        int j = Mth.floor(pArea.minY) - 1;
        int k = Mth.floor(pArea.minZ- delta);
        int l = Mth.floor(pArea.maxX+delta);
        int i1 = Mth.floor(pArea.minY);
        int j1 = Mth.floor(pArea.maxZ+delta);
        for(int k1 = i; k1 <= l; ++k1) {
            for(int l1 = j; l1 <= i1; ++l1) {
                for(int i2 = k; i2 <= j1; ++i2) {
                    BlockPos blockpos = new BlockPos(k1, l1, i2);
                    BlockState blockstate = entity.level.getBlockState(blockpos);
                    if (!blockstate.isAir()){
                        if(blockstate.getMaterial() == Material.LAVA){
                            entity.level.setBlockAndUpdate(blockpos, Blocks.OBSIDIAN.defaultBlockState());
                            entity.level.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.LAVA_EXTINGUISH, SoundSource.AMBIENT, 0.2F, 1.0F);
                            for(int p = 0; p< 20; p++) {
                                ((ServerLevel) entity.level).sendParticles(ParticleTypes.LARGE_SMOKE, blockpos.getX(), blockpos.getY()+1, blockpos.getZ(), 1,  random.nextDouble(-0.2, 0.2), random.nextDouble(0, 0.2), random.nextDouble(-0.2, 0.2), random.nextDouble(-0.2, 0.2));
                            }
                        }
                        if(blockstate.getMaterial() == Material.FIRE){
                            entity.level.setBlockAndUpdate(blockpos, Blocks.AIR.defaultBlockState());
                            entity.level.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.AMBIENT, 0.2F, 1.0F);
                        }
                    }
                }
            }
        }
    }
}












