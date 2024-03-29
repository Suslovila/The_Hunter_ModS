package com.way.suslovila;

import com.mojang.logging.LogUtils;
import com.way.suslovila.entity.bag.RenderHunterBagEntity;
import com.way.suslovila.entity.hunter.appearance.RenderHunterAppearanceFormEntity;
import com.way.suslovila.entity.hunter.pushAttack.RenderPushAttackHunter;
import com.way.suslovila.entity.hunter.teleport.RenderHunterTeleportFormEntity;
import com.way.suslovila.entity.projectile.explosionArrow.RenderExplosionArrow;
import com.way.suslovila.entity.projectile.ghostArrow.RenderGhostArrow;
import com.way.suslovila.entity.projectile.speedArrow.RenderSpeedArrow;
import com.way.suslovila.entity.shadowGrabEntity.RenderShadowGrab;
import com.way.suslovila.entity.EntityShadowMonster.RenderShadowMonster;
import com.way.suslovila.entity.shadowgardenentity.RenderShadowGarden;
import com.way.suslovila.event.ModEventBusEventsAll;
import com.way.suslovila.event.ServerProxy;
import com.way.suslovila.event.SideProxy;
import com.way.suslovila.event.client.ClientProxy;
import com.way.suslovila.item.bag.ItemHunterBag;
import com.way.suslovila.music.ModSounds;
import com.way.suslovila.particles.ModParticles;
import com.way.suslovila.particles.ParticleHandler;
import com.way.suslovila.entity.ModEntityTypes;

import com.way.suslovila.entity.hunter.RenderHunterEntity;
import com.way.suslovila.entity.trap.RenderTrapEntity;
import com.way.suslovila.item.ModItems;

import com.way.suslovila.sounds.MCSounds;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.*;
import org.slf4j.Logger;
import software.bernie.geckolib3.GeckoLib;

import java.util.function.BiConsumer;
import java.util.function.Consumer;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(MysticalCreatures.MOD_ID)
public class MysticalCreatures {
    //todo: доделать ориент
    ServerProxy PROXY;
    public static SimpleChannel NETWORK;
public static final double maxShadowParticleRadius = 1.5;

    public static final String MOD_ID = "mysticalcreatures";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final TagKey<Item> HOLDS_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("forge", "holds_items"));
    //curios:back
    public static final TagKey<Item> CURIOS_BACK = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("curios", "back"));
    //forge:soulbound
    public static final TagKey<Enchantment> SOULBOUND = TagKey.create(Registry.ENCHANTMENT_REGISTRY, new ResourceLocation("forge", "soulbound"));


    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MysticalCreatures.MOD_ID);
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MysticalCreatures.MOD_ID);
    private static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MysticalCreatures.MOD_ID);


    public static final RegistryObject<Item> COMMONBACKPACK = ITEMS.register("commonbackpack", () -> new ItemHunterBag(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_TOOLS).fireResistant()));




    public MysticalCreatures() {
        DistExecutor.safeRunForDist(
                () -> SideProxy.Client::new,
                () -> SideProxy.Server::new
        );
        setup();
        // Register the setup method for modloading
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the enqueueIMC method for modloading

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        //loading GeckoLib
        GeckoLib.initialize();
        ModEntityTypes.register(eventBus);
        ModSounds.register(eventBus);
        //ModEffects.register(eventBus);
        //ModPotions.register(eventBus);
        MCSounds.REG.register(eventBus);

        eventBus.addListener(this::clientSetup);
        ModItems.register(eventBus);
        bind(ForgeRegistries.CONTAINERS, ModItems::registerMenuTypes);

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
        CONTAINERS.register(bus);
        RECIPES.register(bus);
        ModParticles.register(eventBus);
        ParticleHandler.register(eventBus);
        PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
        PROXY.init(bus);


        //Configs



        bus.addListener(this::clientStuff);
        eventBus.addListener(ModEventBusEventsAll::init);





        bus.<FMLCommonSetupEvent>addListener(this::init);
        bus.<FMLLoadCompleteEvent>addListener(this::init);


    }

    private void clientSetup(final FMLClientSetupEvent event) {

        EntityRenderers.register(ModEntityTypes.HUNTER.get(), RenderHunterEntity::new);
        EntityRenderers.register(ModEntityTypes.TRAP.get(), RenderTrapEntity::new);
        EntityRenderers.register(ModEntityTypes.HUNTER_BAG.get(), RenderHunterBagEntity::new);
        EntityRenderers.register(ModEntityTypes.HUNTER_TELEPORT_FORM.get(), RenderHunterTeleportFormEntity::new);
        EntityRenderers.register(ModEntityTypes.HUNTER_APPEAR_FORM.get(), RenderHunterAppearanceFormEntity::new);
        EntityRenderers.register(ModEntityTypes.SPEED_ARROW.get(), RenderSpeedArrow::new);
        EntityRenderers.register(ModEntityTypes.EXPLOSION_ARROW.get(), RenderExplosionArrow::new);
        EntityRenderers.register(ModEntityTypes.SHADOW_GRAB.get(), RenderShadowGrab::new);
        EntityRenderers.register(ModEntityTypes.SHADOW_GARDEN.get(), RenderShadowGarden::new);
        EntityRenderers.register(ModEntityTypes.HUNTER_PUSH.get(), RenderPushAttackHunter::new);
        EntityRenderers.register(ModEntityTypes.SHADOW_MONSTER.get(), RenderShadowMonster::new);
        EntityRenderers.register(ModEntityTypes.GHOST_ARROW.get(), RenderGhostArrow::new);


    }

    public static void setup() {
        IEventBus bus = MinecraftForge.EVENT_BUS;

    }
    private static <T extends IForgeRegistryEntry<T>> void bind(IForgeRegistry<T> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(registry.getRegistrySuperType(),
                (RegistryEvent.Register<T> event) -> {
                    IForgeRegistry<T> forgeRegistry = event.getRegistry();
                    source.accept((t, rl) -> {
                        t.setRegistryName(rl);
                        forgeRegistry.register(t);
                    });
                });
    }


    private void clientStuff(final FMLClientSetupEvent event) {
        //MenuScreens.register(FILTERCONTAINER.get(), FilterGui::new);



    }

        private void init(FMLLoadCompleteEvent event) {
            final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
            PROXY.onLateInit(bus);

        }
    public void init(final FMLCommonSetupEvent event) {
        //event.enqueueWork(ModPotions::addPotionRecipes);
       // ModPotions.addPotionRecipes();
        PROXY.initNetwork();

    }
}
