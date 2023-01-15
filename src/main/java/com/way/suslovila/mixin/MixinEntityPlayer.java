package com.way.suslovila.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Player.class)
public abstract class MixinEntityPlayer
        extends Entity {

    public MixinEntityPlayer(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
}