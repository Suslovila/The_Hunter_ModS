package com.way.suslovila;


import com.way.suslovila.item.ModItems;
import com.way.suslovila.item.bag.HunterBagScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MysticalCreatures.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ForgeClientInitializer {
    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent evt) {
        // GUIs
        evt.enqueueWork(() -> {
            MenuScreens.register(ModItems.HUNTER_BAG_CONTAINER, HunterBagScreen::new);
        });
}
}