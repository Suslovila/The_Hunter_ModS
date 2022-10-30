package com.way.suslovila.capability;

import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
//
//public abstract class CapabilityAttacher {
//    @SuppressWarnings("rawtypes")
//    private static final Capability.IStorage EMPTY_STORAGE = new Capability.IStorage() {
//        @Nullable
//        @Override
//        public INBT writeNBT(Capability capability, Object instance, Direction side) {return new CompoundNBT();}
//
//        @Override
//        public void readNBT(Capability capability, Object instance, Direction side, INBT nbt) {}
//    };
//
//    static {
//        MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, CapabilityAttacher::onAttachCapability);
//    }
//
//    @SuppressWarnings("unchecked")
//    protected static <T> void registerCapability(Class<T> capClass) {
//        CapabilityManager.INSTANCE.register(capClass, (Capability.IStorage<T>) EMPTY_STORAGE, () -> null);
//    }
//
//    private static final List<BiConsumer<AttachCapabilitiesEvent<ItemStack>, ItemStack>> capAttachers = new ArrayList<>();
//    private static final List<Function<ItemStack, LazyOptional<? extends INBTSavable<CompoundNBT>>>> capRetrievers = new ArrayList<>();
//
//
//    @SuppressWarnings("unchecked")
//    protected static <C extends INBTSavable<CompoundNBT>> void registerAttacher(Class<ItemStack> entityClass, BiConsumer<AttachCapabilitiesEvent<ItemStack>, ItemStack> attacher,
//                                                                                           Function<ItemStack, LazyOptional<C>> capRetriever) {
//        capAttachers.add((event, entity) -> {
//            if (entityClass.isInstance(entity))
//                attacher.accept(event, (ItemStack) entity);
//        });
//        capRetrievers.add(entity -> entityClass.isInstance(entity) ? capRetriever.apply((ItemStack) entity) : LazyOptional.empty());
//    }
//
//    protected static <I extends INBTSerializable<T>, T extends INBT> void genericAttachCapability(AttachCapabilitiesEvent<?> event, I impl, Capability<I> capability, ResourceLocation location) {
//        genericAttachCapability(event, impl, capability, location, true);
//    }
//
//    protected static <I extends INBTSerializable<T>, T extends INBT> void genericAttachCapability(AttachCapabilitiesEvent<?> event, I impl, Capability<I> capability, ResourceLocation location,
//            boolean save) {
//        LazyOptional<I> storage = LazyOptional.of(() -> impl);
//        ICapabilityProvider provider = getProvider(impl, storage, capability, save);
//        event.addCapability(location, provider);
//        event.addListener(storage::invalidate);
//    }
//
//    protected static <I extends INBTSerializable<T>, T extends INBT> ICapabilityProvider getProvider(I impl, LazyOptional<I> storage, Capability<I> capability, boolean save) {
//        if (capability == null)
//            throw new NullPointerException();
//        return save ? new ICapabilitySerializable<T>() {
//            @Nonnull
//            @Override
//            public <C> LazyOptional<C> getCapability(@Nonnull Capability<C> cap, @Nullable Direction side) {
//                return cap == capability ? storage.cast() : LazyOptional.empty();
//            }
//
//            @SuppressWarnings("unchecked")
//            @Override
//            public T serializeNBT() {
//                return impl instanceof INBTSavable ? (T) ((INBTSavable<?>) impl).serializeNBT(true) : impl.serializeNBT();
//            }
//
//            @SuppressWarnings("unchecked")
//            @Override
//            public void deserializeNBT(T nbt) {
//                if (impl instanceof INBTSavable) {
//                    ((INBTSavable<T>) impl).deserializeNBT(nbt, true);
//                } else {
//                    impl.deserializeNBT(nbt);
//                }
//            }
//        } : new ICapabilityProvider() {
//            @Nonnull
//            @Override
//            public <C> LazyOptional<C> getCapability(@Nonnull Capability<C> cap, @Nullable Direction side) {
//                return cap == capability ? storage.cast() : LazyOptional.empty();
//            }
//        };
//    }
//
//    private static void onAttachCapability(AttachCapabilitiesEvent<ItemStack> event) {
//        // Attaches the capabilities
//        capAttachers.forEach(attacher -> attacher.accept(event, event.getObject()));
//    }
//
//}