package com.way.suslovila.event;

import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.item.RainyAuraTalisman.WaterShieldLayer;
import com.way.suslovila.potions.ModPotions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Iterator;

@Mod.EventBusSubscriber(modid = MysticalCreatures.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value= Dist.CLIENT)
public class ModEventBusEventsClient {
    @SubscribeEvent
    public static void LayersEvent(EntityRenderersEvent.AddLayers event){
        System.out.println("Loaded into event");
        for (String s : event.getSkins()) {
            event.getSkin(s).addLayer(new WaterShieldLayer(event.getSkin(s)));
        }
        //EntityModelSet entityModelSet = event.getEntityModels();
        //Iterator<EntityType<? extends Entity>> iterator = ForgeRegistries.ENTITIES.getValues().iterator();
//        while(iterator.hasNext()) {
//            EntityType<? extends Entity> type = iterator.next();
//            if (type.getCategory() != MobCategory.MISC) {
//                LivingEntityRenderer renderer = event.getRenderer(type);
//                renderer.addLayer(new WaterShieldLayer(renderer));
//            }
        //}
    }
    @SubscribeEvent
    static void entityRendererAddLayers(EntityRenderersEvent.AddLayers event) {
        //event.getSkins().forEach(skin -> renderer.addLayer(new GlassesRenderLayer(event.getSkin(skin))));

        for (EntityType type : ForgeRegistries.ENTITIES.getValues()) {
            if (Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(type) instanceof LivingEntityRenderer livingRenderer)
                livingRenderer.addLayer(new WaterShieldLayer(livingRenderer));
        }
    }
//    @SubscribeEvent
//    public void setup(FMLCommonSetupEvent event) {
//        event.enqueueWork(ModPotions::addPotionRecipes);
//    }
}
