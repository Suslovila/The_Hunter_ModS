package com.way.suslovila.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;

public class TESTBlackHeadParticles extends TextureSheetParticle {
    double MaxParticleSize;
    MotionVariants actualMotion = null;
    int timerForParticleSpawn = 0;
    protected TESTBlackHeadParticles(ClientLevel level, double xCoord, double yCoord, double zCoord,
                                 SpriteSet spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);
        //generate some stuff for tail particles:
        MaxParticleSize = random.nextDouble(0.28D, 0.3D);




        this.friction = 0.8F;

        double deltaXSpeed = random.nextDouble(0.3D, 0.5D);
        double deltaYSpeed = random.nextDouble(0.3D, 0.5D);
        double deltaZSpeed = random.nextDouble(0.3D, 0.5D);


        this.xd = 0.1D + deltaXSpeed;
        this.yd = 0.1D + deltaYSpeed;
        this.zd = 0.1D + deltaZSpeed;




        this.quadSize *= 0.9D;
        this.lifetime = random.nextInt(80,160);
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

        if(actualMotion == null){
            timerForParticleSpawn = 0;
            if(random.nextInt(3) == 2){
                actualMotion = MotionVariants.values()[random.nextInt(4)];
            }
        }
        if(actualMotion != null){
            this.xd = Math.cos(lifetime);
            this.zd = Math.cos(lifetime);
            this.yd = Math.sin(lifetime);
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
            return new TESTBlackHeadParticles(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
public enum MotionVariants{
        UpLeftSide(1, new ArrayList<MotionVariants>()),
    UPRightSide(2,  new ArrayList<MotionVariants>( )),
    DownLeftSide(3,  new ArrayList<MotionVariants>()),
    DownRightSide(4,  new ArrayList<MotionVariants>());
    public int type;
        public ArrayList<MotionVariants> compatibleWith;
            MotionVariants(int var, ArrayList<MotionVariants> variants){
            this.type = var;
            compatibleWith = variants;
        }

    }
}