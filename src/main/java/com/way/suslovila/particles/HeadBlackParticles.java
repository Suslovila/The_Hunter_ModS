package com.way.suslovila.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Locale;

public class HeadBlackParticles extends TextureSheetParticle {
    protected AABB spaceToLive;
    double MaxParticleSize;
    int particleLifeTime;
    protected HeadBlackParticles(ClientLevel level, double xCoord, double yCoord, double zCoord,
                                 SpriteSet spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);
        //generate some stuff for tail particles:
        MaxParticleSize = random.nextDouble(0.28D, 0.3D);
        particleLifeTime = random.nextInt(7,17);



        this.friction = 0.8F;

       double deltaXSpeed = random.nextDouble(0.2D, 0.6D);
        double deltaYSpeed = random.nextDouble(0.2D, 0.6D);
        double deltaZSpeed = random.nextDouble(0.2D, 0.6D);


        this.xd = 0.1D + deltaXSpeed;
        this.yd = 0.1D + deltaYSpeed;
        this.zd = 0.1D + deltaZSpeed;


        spaceToLive = new AABB(xCoord-2.5D, yCoord, zCoord-2.5D, xCoord + 2.5D, yCoord + 5, zCoord + 2.5D);


        this.quadSize *= 0.1D;
        this.lifetime = random.nextInt(7, 40);
        this.setSpriteFromAge(spriteSet);
        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
    }

    @Override
    public void tick() {
        super.tick();
        this.level.addParticle(new TailBlackParticles.TailParticleData(this.MaxParticleSize, this.lifetime),
                this.xo, this.yo, this.zo,
                0, 0, 0);
        int chance1 = random.nextInt(3);
        int chance2 = random.nextInt(3);

            this.xd = this.xd + random.nextDouble(-0.3D, 0.3D);



            this.yd = this.yd + random.nextDouble(-0.3D, 0.3D);


            this.zd = this.zd + random.nextDouble(-0.3D, 0.3D);




        if(!spaceToLive.contains(this.xo + xd, this.yo+yd, this.zo+zd)) {
            if (!(this.xo + xd >= spaceToLive.minX)) {
                this.xd = 0;
                this.xo = spaceToLive.minX;
            }
            if (!(this.xo + xd <= spaceToLive.maxX)) {
                this.xd = 0;
                this.xo = spaceToLive.maxX;
            }
            if (!(this.yo + yd >= spaceToLive.minY)) {
                this.yd = 0;
                this.yo = spaceToLive.minY;
            }
            if (!(this.yo + yd <= spaceToLive.maxY)) {
                this.yd = 0;
                this.yo = spaceToLive.maxY;
            }
            if (!(this.zo + zd >= spaceToLive.minZ)) {
                this.zd = 0;
                this.zo = spaceToLive.minZ;
            }
            if (!(this.zo + zd <= spaceToLive.maxZ)) {
                this.zd = 0;
                this.zo = spaceToLive.maxZ;
            }
        }
        double Xnext = this.xd + xo;
        double Ynext = this.yd + yo;
        double Znext = this.zd + zo;

        double deltaX = Xnext - xo;
        double deltaY = Ynext - yo;
        double deltaZ = Znext - zo;

        for (int i = 1; i < 14; i++) {
            double XRandom = random.nextDouble(0.025D, 0.03D);
            double YRandom = random.nextDouble(0.025D, 0.03D);
            double ZRandom = random.nextDouble(0.025D, 0.03D);

            if (random.nextBoolean()) {
                XRandom = -XRandom;
            }
            if (random.nextBoolean()) {
                YRandom = -YRandom;
            }
            if (random.nextBoolean()) {
                ZRandom = -ZRandom;
            }


            this.level.addParticle(new TailBlackParticles.TailParticleData(this.MaxParticleSize, this.lifetime),
                    this.xo + deltaX * i / 13 + XRandom, this.yo + deltaY * i / 13 + YRandom, this.zo + deltaZ * i / 13 + ZRandom,
                    0, 0, 0);


        }
    }







    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
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
            return new HeadBlackParticles(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }

}