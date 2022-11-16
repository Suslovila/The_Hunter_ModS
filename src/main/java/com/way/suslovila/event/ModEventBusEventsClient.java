package com.way.suslovila.event;

import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.item.RainyAuraTalisman.WaterShieldLayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MysticalCreatures.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value= Dist.CLIENT)
public class ModEventBusEventsClient {
    @SubscribeEvent
    public static void LayersEvent(EntityRenderersEvent.AddLayers event){
        System.out.println("Loaded into event");
        for (String s : event.getSkins()) {
            event.getSkin(s).addLayer(new WaterShieldLayer(event.getSkin(s)));
        }
    }
}
