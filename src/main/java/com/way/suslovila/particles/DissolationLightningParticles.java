package com.way.suslovila.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class DissolationLightningParticles extends TextureSheetParticle {
    Random random = new Random();
    public DissolationLightningParticles(ClientLevel level, double xCoord, double yCoord, double zCoord,
                                         SpriteSet spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);
        this.friction = 1F;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.gravity = 0;
        this.quadSize *= random.nextDouble(0.1D,1D);
        this.setSpriteFromAge(spriteSet);
        this.lifetime = 8;
        //this.setSpriteFromAge(spriteSet);

    }

    @Override
    public void tick() {
        super.tick();
        if(age >= this.lifetime){
            this.remove();
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
            return new DissolationLightningParticles(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}
