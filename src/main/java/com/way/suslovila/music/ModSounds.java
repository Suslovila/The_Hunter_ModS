package com.way.suslovila.music;

import com.way.suslovila.MysticalCreatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MysticalCreatures.MOD_ID);
//    public static final RegistryObject<SoundEvent> HUNT_THEME = registerSoundEvent("hunt_theme");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(MysticalCreatures.MOD_ID, name)));
    }
    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
    //public static final Supplier<Music> HUNT_THEME_MUSIC = Suppliers.memoize(() -> new Music(HUNT_THEME.get(), 14760, 14761, true));
}
