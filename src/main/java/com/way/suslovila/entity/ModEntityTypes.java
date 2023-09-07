package com.way.suslovila.entity;

import com.way.suslovila.MysticalCreatures;

import com.way.suslovila.entity.bag.HunterBagEntity;
import com.way.suslovila.entity.hunter.HunterEntity;
import com.way.suslovila.entity.hunter.appearance.HunterAppearanceFormEntity;
import com.way.suslovila.entity.hunter.pushAttack.PushAttackHunter;
import com.way.suslovila.entity.hunter.teleport.HunterTeleportFormEntity;
import com.way.suslovila.entity.projectile.explosionArrow.ExplosionArrow;
import com.way.suslovila.entity.projectile.ghostArrow.GhostArrow;
import com.way.suslovila.entity.projectile.speedArrow.SpeedArrow;
import com.way.suslovila.entity.shadowGrabEntity.ShadowGrabEntity;
import com.way.suslovila.entity.EntityShadowMonster.ShadowMonsterEntity;
import com.way.suslovila.entity.shadowgardenentity.ShadowGardenEntity;
import com.way.suslovila.entity.trap.TrapEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITIES, MysticalCreatures.MOD_ID);

    public static final RegistryObject<EntityType<HunterEntity>> HUNTER =
            ENTITY_TYPES.register("hunter",
                    () -> EntityType.Builder.of(HunterEntity::new, MobCategory.MONSTER)
                            .sized(0.8f, 3.75f)
                            .build(new ResourceLocation(MysticalCreatures.MOD_ID, "hunter").toString()));
    public static final RegistryObject<EntityType<HunterBagEntity>> HUNTER_BAG =
            ENTITY_TYPES.register("hunter_bag",
                    () -> EntityType.Builder.of(HunterBagEntity::new,MobCategory.MISC)
                            .sized(0.42f, 0.28f)
                            .build(new ResourceLocation(MysticalCreatures.MOD_ID, "hunter_bag").toString()));
    public static final RegistryObject<EntityType<TrapEntity>> TRAP =
            ENTITY_TYPES.register("trap",
                    () -> EntityType.Builder.of(TrapEntity::new, MobCategory.CREATURE)
                            .sized(0.75f, 0.05f)
                            .build(new ResourceLocation(MysticalCreatures.MOD_ID, "trap").toString()));

    public static final RegistryObject<EntityType<HunterTeleportFormEntity>> HUNTER_TELEPORT_FORM =
            ENTITY_TYPES.register("hunter_teleport_form",
                    () -> EntityType.Builder.of(HunterTeleportFormEntity::new,MobCategory.MISC)
                            .sized(0.8f, 3.75f)
                            .build(new ResourceLocation(MysticalCreatures.MOD_ID, "hunter_teleport_form").toString()));

    public static final RegistryObject<EntityType<HunterAppearanceFormEntity>> HUNTER_APPEAR_FORM =
            ENTITY_TYPES.register("hunter_appearance_form",
                    () -> EntityType.Builder.of(HunterAppearanceFormEntity::new,MobCategory.MISC)
                            .sized(0.8f, 3.75f)
                            .build(new ResourceLocation(MysticalCreatures.MOD_ID, "hunter_appearance_form").toString()));
    public static final RegistryObject<EntityType<SpeedArrow>> SPEED_ARROW =
            ENTITY_TYPES.register("speed_arrow",
                    () -> EntityType.Builder.of(SpeedArrow::new, MobCategory.MISC)
                            .sized(0.6f, 0.6f)
                            .build(new ResourceLocation(MysticalCreatures.MOD_ID, "speed_arrow").toString()));

    public static final RegistryObject<EntityType<ExplosionArrow>> EXPLOSION_ARROW =
            ENTITY_TYPES.register("explosion_arrow",
                    () -> EntityType.Builder.of(ExplosionArrow::new, MobCategory.MISC)
                            .sized(0.15f, 0.15f)
                            .build(new ResourceLocation(MysticalCreatures.MOD_ID, "explosion_arrow").toString()));

    public static final RegistryObject<EntityType<ShadowGrabEntity>> SHADOW_GRAB =
            ENTITY_TYPES.register("shadow_grab",
                    () -> EntityType.Builder.of(ShadowGrabEntity::new, MobCategory.MISC)
                            .sized(3f, 2f)
                            .build(new ResourceLocation(MysticalCreatures.MOD_ID, "shadow_grab").toString()));

    public static final RegistryObject<EntityType<ShadowGardenEntity>> SHADOW_GARDEN =
            ENTITY_TYPES.register("shadow_garden",
                    () -> EntityType.Builder.of(ShadowGardenEntity::new, MobCategory.MISC)
                            .sized(0.75f, 0.05f)
                            .build(new ResourceLocation(MysticalCreatures.MOD_ID, "shadow_garden").toString()));

    public static final RegistryObject<EntityType<PushAttackHunter>> HUNTER_PUSH =
            ENTITY_TYPES.register("hunter_push",
                    () -> EntityType.Builder.of(PushAttackHunter::new, MobCategory.MISC)
                            .sized(2f, 2f)
                            .build(new ResourceLocation(MysticalCreatures.MOD_ID, "hunter_push").toString()));
    public static final RegistryObject<EntityType<ShadowMonsterEntity>> SHADOW_MONSTER =
            ENTITY_TYPES.register("shadow_monster",
                    () -> EntityType.Builder.of(ShadowMonsterEntity::new, MobCategory.MISC)
                            .sized(13f, 25f)
                            .build(new ResourceLocation(MysticalCreatures.MOD_ID, "shadow_monster").toString()));
    public static final RegistryObject<EntityType<GhostArrow>> GHOST_ARROW =
            ENTITY_TYPES.register("ghost_arrow",
                    () -> EntityType.Builder.of(GhostArrow::new, MobCategory.MISC)
                            .sized(0.15f, 0.15f)
                            .build(new ResourceLocation(MysticalCreatures.MOD_ID, "ghost_arrow").toString()));
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);

    }





}


