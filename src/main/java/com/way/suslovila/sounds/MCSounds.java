package com.way.suslovila.sounds;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.way.suslovila.MysticalCreatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = MysticalCreatures.MOD_ID)
public final class MCSounds {
    private MCSounds() {
    }

    public static final DeferredRegister<SoundEvent> REG = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MysticalCreatures.MOD_ID);

    // Generic
    public static final RegistryObject<SoundEvent> HuntTheme = create("hunt_theme");
    public static final RegistryObject<SoundEvent> beastUnknownLanguage = create("beast_unknown_language");
    public static final RegistryObject<SoundEvent> beastClear = create("beast_clear");
    private static RegistryObject<SoundEvent> create(String name) {
        return REG.register(name, () -> new SoundEvent(new ResourceLocation(MysticalCreatures.MOD_ID, name)));
    }
}