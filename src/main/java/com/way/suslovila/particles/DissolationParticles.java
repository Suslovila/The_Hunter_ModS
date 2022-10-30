package com.way.suslovila.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Locale;
import java.util.Random;

public class DissolationParticles extends TextureSheetParticle {
    private final float uo;
    private final float vo;
    Random random = new Random();
    public DissolationParticles(ClientLevel level, double xCoord, double yCoord, double zCoord,
                                 double xd, double yd, double zd, double MaxSizeOfParticle, int TimeOfLife) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);
        this.friction = 1F;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.gravity = 0;
        this.quadSize *= random.nextDouble(0.1D,1D);
        BlockPos blockPos = new BlockPos(xCoord, yCoord, zCoord);
        BlockState blockState = level.getBlockState(blockPos);
        this.setSprite(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(blockState));
        this.lifetime = 50;
        //this.setSpriteFromAge(spriteSet);

        int i = Minecraft.getInstance().getBlockColors().getColor(blockState, level, blockPos, 0);
        this.rCol *= (float)(i >> 16 & 255) / 255.0F;
        this.gCol *= (float)(i >> 8 & 255) / 255.0F;
        this.bCol *= (float)(i & 255) / 255.0F;
        this.uo = this.random.nextFloat() * 3.0F;
        this.vo = this.random.nextFloat() * 3.0F;
    }

    @Override
    public void tick() {
        super.tick();
        if(age >= this.lifetime){
            this.remove();
        }

        this.fadeOut();

    }

    protected float getU0() {
        return this.sprite.getU((double)((this.uo + 1.0F) / 4.0F * 16.0F));
    }

    protected float getU1() {
        return this.sprite.getU((double)(this.uo / 4.0F * 16.0F));
    }

    protected float getV0() {
        return this.sprite.getV((double)(this.vo / 4.0F * 16.0F));
    }

    protected float getV1() {
        return this.sprite.getV((double)((this.vo + 1.0F) / 4.0F * 16.0F));
    }

    private void fadeOut() {
        this.alpha = (-(1/(float)lifetime) * age + 1);

    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.TERRAIN_SHEET;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new DissolationParticles(level, x, y, z, dx, dy, dz, 4, 4);
        }
    }
}
