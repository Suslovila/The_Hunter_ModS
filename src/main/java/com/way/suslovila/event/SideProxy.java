package com.way.suslovila.event;


import com.mojang.blaze3d.platform.InputConstants;
import com.way.suslovila.effects.ModEffects;
import com.way.suslovila.potions.ModPotions;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

public class SideProxy {

    SideProxy() {
        IEventBus modEventBus = MinecraftForge.EVENT_BUS;
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.register(this);

//        modEventBus.register(new EventRegisterCapabilities());
//        modEventBus.register(new EventAttachCapabilities());
//        modEventBus.register(new EventRegisterCommands());
//        modEventBus.register(new EventNameFormat());
//        modEventBus.register(new EventPlayerTick());
//        modEventBus.register(new EventPlayerLoggedIn());
//        modEventBus.register(new EventRecipesUpdated());
//
//        modEventBus.register(new FarmersHoeEvents());
//        modEventBus.register(new CurseBreakEvents());
//
//        modEventBus.register(new AlchemistEvents());
//        modEventBus.register(new BuilderEvents());
//        modEventBus.register(new DiggerEvents());
//        modEventBus.register(new FarmerEvents());
//        modEventBus.register(new FishermanEvents());
//        modEventBus.register(new EnchanterEvents());
//        modEventBus.register(new HunterEvents());
//        modEventBus.register(new LumberjackEvents());
//        modEventBus.register(new MinerEvents());
//        modEventBus.register(new SmithEvents());
//
//        modEventBus.register(new DoubleJumpEvents());
//
//        ModItems.ITEMS.register(eventBus);
         //ModBlocks.BLOCKS.register(eventBus);

        ModPotions.registerVanillaPotions();
        ModPotions.POTIONS.register(eventBus);
        ModEffects.register(eventBus);
//        ModRecipes.RECIPE_SERIALIZERS.register(eventBus);
//        ModEntities.ENTITY_TYPES.register(eventBus);
//        ModMenuTypes.MENU_TYPES.register(eventBus);

//        modEventBus.addListener(EventClone::onDeath);
//        modEventBus.addListener(EventServerAboutToStart::onServerStarted);
//        eventBus.addListener(ModDataGenerator::gatherData);
//
//        ModPackets.init();
//        HeadData.loadHeadData();

       // ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
    }

//    @SubscribeEvent
//    public void setup(FMLCommonSetupEvent event) {
//        event.enqueueWork(ModPotions::addPotionRecipes);
//    }
    @SubscribeEvent
    public void load(FMLLoadCompleteEvent event) {
        event.enqueueWork(ModPotions::addPotionRecipes);
    }

    public static class Server extends SideProxy {
        public Server() {

        }

    }

    public static class Client extends SideProxy {

//        public static final KeyMapping OPEN_GUI_KEYBIND = new KeyMapping("keys.jobsplus.open_gui", KeyConflictContext.UNIVERSAL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_J, "keys.jobsplus.category");
//        public static final KeyMapping VEIN_MINER_KEYBIND = new KeyMapping("keys.jobsplus.vein_miner", KeyConflictContext.UNIVERSAL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, "keys.jobsplus.category");
//        public static final KeyMapping DOUBLE_JUMP_KEYBIND = new KeyMapping("keys.jobsplus.double_jump", KeyConflictContext.UNIVERSAL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_SPACE, "keys.jobsplus.category");

        public Client() {
//            IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
//            eventBus.addListener(this::clientStuff);
        }

        private void clientStuff(final FMLClientSetupEvent event) {

        }
    }
}