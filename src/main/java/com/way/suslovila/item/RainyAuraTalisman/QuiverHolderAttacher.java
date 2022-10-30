package com.way.suslovila.item.RainyAuraTalisman;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
//
//public class QuiverHolderAttacher extends CapabilityAttacher {
//    private static final Class<QuiverHolder> CAPABILITY_CLASS = QuiverHolder.class;
//    @CapabilityInject(QuiverHolder.class) // HAS to be public!
//    public static final Capability<QuiverHolder> QUIVER_HOLDER_CAPABILITY = null;
//    public static final ResourceLocation QUIVER_HOLDER_RL = new ResourceLocation(NyfsQuiver.MOD_ID, "quiver_holder");
//
//    public static QuiverHolder getQuiverHolderUnwrap(ItemStack player) {
//        return getQuiverHolder(player).orElse(null);
//    }
//
//    public static LazyOptional<QuiverHolder> getQuiverHolder(ItemStack player) {
//        return player.getCapability(QUIVER_HOLDER_CAPABILITY);
//    }
//
//    private static void attach(AttachCapabilitiesEvent<ItemStack> event, ItemStack player) {
//        if(player.getItem() instanceof QuiverItem) {
//            genericAttachCapability(event, new QuiverHolder(player), QUIVER_HOLDER_CAPABILITY, QUIVER_HOLDER_RL);
//        }
//    }
//
//    public static void register() {
//        CapabilityAttacher.registerCapability(CAPABILITY_CLASS);
//        CapabilityAttacher.registerAttacher(ItemStack.class,QuiverHolderAttacher::attach, QuiverHolderAttacher::getQuiverHolder);
//    }
//}