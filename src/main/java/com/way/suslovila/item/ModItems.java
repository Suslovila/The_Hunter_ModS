package com.way.suslovila.item;

import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.entity.ModEntityTypes;

import com.way.suslovila.item.bag.ContainerHunterBag;
import com.way.suslovila.item.bag.HunterBag;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.function.TriFunction;

import java.util.function.BiConsumer;

import static com.way.suslovila.item.bag.ResourceLocationHelper.prefix;

public class ModItems {
    public static final MenuType<ContainerHunterBag> HUNTER_BAG_CONTAINER = createMenuType(ContainerHunterBag::fromNetwork);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MysticalCreatures.MOD_ID);
    public static final RegistryObject<HunterBag> HUNTER_BAG = ITEMS.register("hunter_bag", ()-> new HunterBag(new Item.Properties().stacksTo(1)));


    public static final RegistryObject<Item> HUNTER_SPAWN_EGG = ITEMS.register("hunter_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.HUNTER,0x948e8d, 0x3b3635,
                    new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));

    public static final RegistryObject<Item> TRAP_SPAWN_EGG = ITEMS.register("trap_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.TRAP,0x453e4d, 0x6b4242,
                    new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static <T extends AbstractContainerMenu> MenuType<T> createMenuType(TriFunction<Integer, Inventory, FriendlyByteBuf, T> constructor) {
        return IForgeMenuType.create(constructor::apply);
    }
    public static void registerMenuTypes(BiConsumer<MenuType<?>, ResourceLocation> consumer) {
        consumer.accept(HUNTER_BAG_CONTAINER, prefix("hunter_bag"));
    }
}
