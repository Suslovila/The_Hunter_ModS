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
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Locale;

public class TailBlackParticles extends TextureSheetParticle {

    private double MaxSizeOfParticle;
    private int LifeTime;
    protected TailBlackParticles(ClientLevel level, double xCoord, double yCoord, double zCoord,
                                  double xd, double yd, double zd, double MaxSizeOfParticle, int TimeOfLife) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);
        this.friction = 0.8F;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.quadSize *= 0.0D;
        this.MaxSizeOfParticle = MaxSizeOfParticle;
        this.lifetime = TimeOfLife;
        //this.setSpriteFromAge(spriteSet);
        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
    }

    @Override
    public void tick() {
        super.tick();
        if(age >= this.lifetime){
            this.remove();
        }

        this.fadeOut();

    }

    private void fadeOut() {
        if ((float) this.age / (float) this.lifetime <= 0.5f) this.quadSize = (float) MaxSizeOfParticle * (((float) age)/((float)lifetime * 0.5f));

        if((float) this.age / (float) this.lifetime > 0.5f) this.quadSize = (float) MaxSizeOfParticle * (-(((float) age - (float)lifetime * 0.5f)/((float)lifetime * 0.5f))  + 1);

    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<TailParticleData> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(TailParticleData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            TailBlackParticles particle = new TailBlackParticles(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getMaxSizeOfParticle(), typeIn.getParticleLifeTime());
            particle.pickSprite(spriteSet);
            return particle;
        }
    }



    public static class TailParticleData implements ParticleOptions {
        public static final Deserializer<TailParticleData> DESERIALIZER = new Deserializer<TailParticleData>() {
            public TailParticleData fromCommand(ParticleType<TailParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                double MaxSizeOfParticle = (float) reader.readDouble();
                reader.expect(' ');
                int LifeTime = reader.readInt();
                return new TailParticleData(MaxSizeOfParticle, LifeTime);
            }

            public TailParticleData fromNetwork(ParticleType<TailParticleData> particleTypeIn, FriendlyByteBuf buffer) {
                return new TailParticleData(buffer.readDouble(), buffer.readInt());
            }
        };

        private final double MaxSizeOfParticle;
        private final int LifeTime;

        public TailParticleData(double MaxSizeOfParticle, int LifeTime) {
            this.MaxSizeOfParticle = MaxSizeOfParticle;
            this.LifeTime = LifeTime;
        }

        @Override
        public void writeToNetwork(FriendlyByteBuf buffer) {
            buffer.writeDouble(this.MaxSizeOfParticle);
            buffer.writeInt(this.LifeTime);
        }

        @SuppressWarnings("deprecation")
        @Override
        public String writeToString() {
            return String.format(Locale.ROOT, "%s %.2f %b", Registry.PARTICLE_TYPE.getKey(this.getType()),
                    this.MaxSizeOfParticle, this.LifeTime);
        }

        @Override
        public ParticleType<TailParticleData> getType() {
            return ParticleHandler.TAIL_PARTICLE.get();
        }

        @OnlyIn(Dist.CLIENT)
        public double getMaxSizeOfParticle() {
            return this.MaxSizeOfParticle;
        }

        @OnlyIn(Dist.CLIENT)
        public int getParticleLifeTime() {
            return this.LifeTime;
        }

        public static Codec<TailParticleData> CODEC(ParticleType<TailParticleData> particleType) {
            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                            Codec.DOUBLE.fieldOf("MaxSizeOfParticle").forGetter(TailParticleData::getMaxSizeOfParticle),
                            Codec.INT.fieldOf("LifeTime").forGetter(TailParticleData::getParticleLifeTime)
                    ).apply(codecBuilder, TailParticleData::new)
            );
        }
    }
}