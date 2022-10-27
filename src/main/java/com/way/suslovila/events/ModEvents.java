package com.way.suslovila.events;

import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.bagentity.BagProvider;
import com.way.suslovila.bagentity.HunterBagEntity;
import com.way.suslovila.bagentity.HunterBagEntityItemsStorage;
import com.way.suslovila.effects.ModEffects;
import com.way.suslovila.entity.ModEntityTypes;
import com.way.suslovila.entity.hunter.HunterEntity;
import com.way.suslovila.entity.hunter.appearance.HunterAppearanceFormEntity;
import com.way.suslovila.entity.hunter.pushAttack.PushAttackHunter;
import com.way.suslovila.entity.projectile.speedArrow.SpeedArrow;
import com.way.suslovila.entity.trap.TrapEntity;
import com.way.suslovila.savedData.*;
import com.way.suslovila.savedData.IsTheVictim.MessagesBoolean;
import com.way.suslovila.savedData.IsTheVictim.PacketSyncVictimToClientBoolean;
import com.way.suslovila.savedData.clientSynch.Messages;
import com.way.suslovila.savedData.clientSynch.PacketSyncVictimToClient;
import com.way.suslovila.simplybackpacks.inventory.BackpackData;
import com.way.suslovila.simplybackpacks.items.BackpackItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

import static net.minecraft.world.entity.Entity.RemovalReason.DISCARDED;

@Mod.EventBusSubscriber(modid = MysticalCreatures.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void checkIfEntityIsAVictimEvent(LivingEvent.LivingUpdateEvent event) {
        if (!event.getEntityLiving().level.isClientSide()){
            if(!SaveVictim.get(event.getEntityLiving().level).getVictim().equals("novictim") && !(SaveVictim.get(event.getEntityLiving().level).getVictim() == null)){
            if(event.getEntityLiving().getUUID().equals(UUID.fromString(SaveVictim.get(event.getEntityLiving().level).getVictim()))) {
            event.getEntityLiving().addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 2000, 3));
        }
            }
        if (!event.getEntityLiving().level.isClientSide() && event.getEntityLiving() instanceof Player) {
             //System.out.println("Victim:" + SaveVictim.get(event.getEntityLiving().level).getVictim());

//            System.out.println("items in bag" + HunterBagData.get(event.getEntityLiving().level).getItemsInBag());
            //System.out.println("Hunter:" + HunterAmountData.get(event.getEntityLiving().level).getPreviousHunter());
        }
        if (!event.getEntityLiving().level.isClientSide() && event.getEntityLiving() instanceof HunterEntity && event.getEntityLiving().getUUID().equals(HunterAmountData.get(event.getEntityLiving().level).getPreviousHunter())) {
            event.getEntityLiving().addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 70, 3));
        }
        }
    }

    @SubscribeEvent
    public static void ifPlayerAttacksHunter(AttackEntityEvent event) {
        if (!event.getTarget().level.isClientSide() && event.getTarget() instanceof Zombie) {
            SaveVictim.get(event.getPlayer().level).changeVictim("novictim");
        }
    }
//    @SubscribeEvent
//    public static void ifHunterIsAttackedWhenNotVulnarable(LivingHurtEvent event){
//        if(!event.getEntityLiving().level.isClientSide()){
//            if(event.getEntityLiving() instanceof HunterEntity){
//                if(!(((HunterEntity)event.getEntityLiving()).getVulnarble())){
//                    System.out.println("Event Running:" + event.getEntityLiving().getHealth());
//                    ((HunterEntity)event.getEntityLiving()).disappearInShadows();
//                }
//            }
//        }
//    }

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
            HunterAmountData data =  HunterAmountData.get(event.getEntity().level);
            if(data.getPreviousHunter() != null && ((ServerLevel)(event.getEntity().level)).getEntity(data.getPreviousHunter()) != null) {
                HunterAmountData.get(event.getEntity().level).KillPreviousHunter((ServerLevel) event.getEntity().level);
            }
                HunterAmountData.get(event.getEntity().level).setPreviousHunter(event.getEntity().getUUID());

           if ((HuntersHP.get(event.getEntity().level).getHunterHP() != 0)) {
                ((HunterEntity) event.getEntity()).setHealth((float) HuntersHP.get(event.getEntity().level).getHunterHP());
           }
        }
    }


    @SubscribeEvent
    public static void IfHunterBagSpawnsEvent(EntityJoinWorldEvent event) {
        if (!event.getEntity().level.isClientSide()) {
            if (event.getEntity() instanceof HunterBagEntity) {
                event.getEntity().setNoGravity(false);
                System.out.println("No gravity");
                HunterBagEntity bagEntity = (HunterBagEntity) event.getEntity();
                Item item = MysticalCreatures.COMMONBACKPACK.get();
                ItemStack itemStack = item.getDefaultInstance();
                ItemStack copyOf = itemStack;
                itemStack = putItemsToBackpack(copyOf, bagEntity.level);
                ItemStack finalItemStack = itemStack;



                BackpackData data = BackpackItem.getData(finalItemStack);
                UUID uuid = data.getUuid();
                System.out.println(uuid);




                bagEntity.getCapability(BagProvider.BAG).ifPresent(items -> {
                    items.setBag(finalItemStack);
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
    public static void playerKilledrr(LivingDropsEvent event) {
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
    public static void ForbidUseFruit(EntityTeleportEvent.ChorusFruit event) {
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
    public static void ForbidTeleport(EntityTeleportEvent.EnderEntity event) {
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
    public static void ForbidUseEnderPearl(EntityTeleportEvent.EnderPearl event) {
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
        if ((event.getEntityLiving().hasEffect(ModEffects.PHOENIX_FEATHRING.get()) || event.getEntityLiving().hasEffect(ModEffects.RAINY_AURA.get()))  && event.getSource().isFire()) {
            event.setCanceled(true);
        }
    }


    @SubscribeEvent
    public static void rebirth(LivingDeathEvent event) {
        if (event.getEntityLiving().hasEffect(ModEffects.PHOENIX_FEATHRING.get())) {
            if (event.getEntityLiving().getEffect(ModEffects.PHOENIX_FEATHRING.get()).getAmplifier() >= 2) {
                LivingEntity entity = event.getEntityLiving();
                Level level = event.getEntityLiving().level;
                int distance = 14;
                BlockPos checkPos;

                boolean flag = true;
                for (int x = event.getEntityLiving().getBlockX() - distance; x < event.getEntityLiving().getBlockX() + distance; x++) {
                    if (flag) {
                        for (int y = event.getEntityLiving().getBlockY() - distance; y < event.getEntityLiving().getBlockY() + distance; y++) {
                            if (flag) {
                                for (int z = event.getEntityLiving().getBlockZ() - distance; z < event.getEntityLiving().getBlockZ() + distance; z++) {
                                    if (flag) {
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
                                        if (level.getBlockState(checkPos).getBlock() == Blocks.SOUL_CAMPFIRE || level.getBlockState(checkPos).getBlock() == Blocks.CAMPFIRE) {
                                            if (level.getBlockState(checkPos).getValue(BlockStateProperties.LIT)) {
                                                event.setCanceled(true);
                                                entity.teleportTo(x, y + 1, z);
                                                level.getBlockState(checkPos).setValue(BlockStateProperties.LIT, false);
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
                }
            }
        }
    }

    @SubscribeEvent
    public static void Hunt(TickEvent.WorldTickEvent event) {
//        SaveVictim saveVictim =SaveVictim.get(event.world);
//        saveVictim.tick(event.world);
        if(!event.world.isClientSide()){
//            System.out.println(HuntersHP.get(event.world).getHunterHP());
            SaveVictim.get(event.world).tick(event.world);
            if(SaveVictim.get(event.world).getVictim() == null){
                SaveVictim.get(event.world).changeVictim("novictim");
            }
        }
        if (!event.world.isClientSide() && event.phase == TickEvent.Phase.START) {
            if (event.world.dimension() == Level.OVERWORLD) {
                if (HuntTime.get(event.world).getHuntTime() > 0) {
//                    System.out.println(HuntTime.get(event.world).getHuntTime());
                    HuntTime.get(event.world).reduceTime();
                }
                if(HuntTime.get(event.world).getHuntTime() <= 0){
                    SaveVictim.get(event.world).changeVictim("novictim");
                }
            }
            int chance = event.world.random.nextInt(2400);
            if (chance == 500) {
                if(!SaveVictim.get(event.world).getVictim().equals("novictim") && SaveVictim.get(event.world).getVictim() != null){
                    Player victim = event.world.getPlayerByUUID(UUID.fromString(SaveVictim.get(event.world).getVictim()));
                    if (victim != null) {
                        if(event.world.random.nextInt(6) == 3) {
                            BlockPos checkpos;
                            int maxDistance = 20;
                            boolean flag = true;
                            for (int x = victim.getBlockX() + maxDistance; x < victim.getBlockX() + maxDistance; x++) {
                                if (flag) {
                                    for (int y = victim.getBlockY()-2; y < victim.getBlockY() + 2; y++) {
                                        if (flag) {
                                            for (int z = victim.getBlockZ() - maxDistance; z < victim.getBlockZ() + maxDistance; z++) {
                                                if (flag) {
                                                    if (Math.sqrt(victim.distanceToSqr(x, y, z)) > 8) {
                                                        checkpos = new BlockPos(x,y,z);
                                                        //trying to find walls
//                                                        if (!victim.level.getBlockState(checkpos).isAir() && victim.level.getBlockState(checkpos).)

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }


                            PushAttackHunter pushAttackHunter = new PushAttackHunter(ModEntityTypes.HUNTER_PUSH.get(), victim.level);

                        }
                        else{
                        BlockPos checkPos;
                        int maxDistance = 20;
                        boolean flag = true;
                        for (int x = victim.getBlockX() + maxDistance; x < victim.getBlockX() + maxDistance; x++) {
                            if (flag) {
                                for (int y = victim.getBlockY() - maxDistance; y < victim.getBlockY() + maxDistance; y++) {
                                    if (flag) {
                                        for (int z = victim.getBlockZ() - maxDistance; z < victim.getBlockZ() + maxDistance; z++) {
                                            if (flag) {
                                                if (Math.sqrt(victim.distanceToSqr(x, y, z)) > 8) {
                                                    checkPos = new BlockPos(x, y, z);
                                                    BlockPos up1block = new BlockPos(x, y + 1, z);
                                                    BlockPos up2block = new BlockPos(x, y + 2, z);
                                                    BlockPos up3block = new BlockPos(x, y + 1, z);
                                                    if (event.world.getBlockState(up1block).getBlock() == Blocks.AIR && event.world.getBlockState(up2block).getBlock() == Blocks.AIR && event.world.getBrightness(LightLayer.BLOCK, checkPos) <= 14) {
                                                        flag = false;

                                                    }
                                                }
                                            }
                                            }
                                        }
                                    }
                                }
                            }
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
    }
@SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            event.register(HunterBagEntityItemsStorage.class);
        }
    public static ItemStack putItemsToBackpack(ItemStack backpack, Level level){
        BackpackData data = BackpackItem.getData(backpack);
        data.putItemsInBackpackFromHunterStorage(level);
        return backpack;
    }
}












