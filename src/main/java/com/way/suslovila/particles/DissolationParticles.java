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

public class DissolationParticles extends TextureSheetParticle {
    private final float uo;
    private final float vo;

    public DissolationParticles(ClientLevel level, double xCoord, double yCoord, double zCoord,
                                 double xd, double yd, double zd, double MaxSizeOfParticle, int TimeOfLife) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);
        this.friction = 0.8F;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.gravity = 0;
        this.quadSize *= 1.0D;
        BlockPos blockPos = new BlockPos(xCoord, yCoord, zCoord);
        BlockState blockState = level.getBlockState(blockPos);
        this.setSprite(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(blockState));
        this.lifetime = 60;
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
    public static class Provider implements ParticleProvider<DissolationParticles.DissolationParticleData> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(DissolationParticles.DissolationParticleData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            DissolationParticles particle = new DissolationParticles(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getMaxSizeOfParticle(), typeIn.getParticleLifeTime());
            particle.pickSprite(spriteSet);
            return particle;
        }
    }



    public static class DissolationParticleData implements ParticleOptions {
        public static final ParticleOptions.Deserializer<DissolationParticles.DissolationParticleData> DESERIALIZER = new ParticleOptions.Deserializer<DissolationParticles.DissolationParticleData>() {
            public DissolationParticles.DissolationParticleData fromCommand(ParticleType<DissolationParticles.DissolationParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                double MaxSizeOfParticle = (float) reader.readDouble();
                reader.expect(' ');
                int LifeTime = reader.readInt();
                return new DissolationParticles.DissolationParticleData(MaxSizeOfParticle, LifeTime);
            }

            public DissolationParticles.DissolationParticleData fromNetwork(ParticleType<DissolationParticles.DissolationParticleData> particleTypeIn, FriendlyByteBuf buffer) {
                return new DissolationParticles.DissolationParticleData(buffer.readDouble(), buffer.readInt());
            }
        };

        private final double MaxSizeOfParticle;
        private final int LifeTime;

        public DissolationParticleData(double MaxSizeOfParticle, int LifeTime) {
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
        public ParticleType<DissolationParticleData> getType() {
            return ParticleHandler.DISSOLATION_PARTICLE.get();
        }

        @OnlyIn(Dist.CLIENT)
        public double getMaxSizeOfParticle() {
            return this.MaxSizeOfParticle;
        }

        @OnlyIn(Dist.CLIENT)
        public int getParticleLifeTime() {
            return this.LifeTime;
        }

        public static Codec<DissolationParticles.DissolationParticleData> CODEC(ParticleType<DissolationParticles.DissolationParticleData> particleType) {
            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                            Codec.DOUBLE.fieldOf("MaxSizeOfParticle").forGetter(DissolationParticles.DissolationParticleData::getMaxSizeOfParticle),
                            Codec.INT.fieldOf("LifeTime").forGetter(DissolationParticles.DissolationParticleData::getParticleLifeTime)
                    ).apply(codecBuilder, DissolationParticles.DissolationParticleData::new)
            );
        }
    }
    public Particle updateSprite(BlockState state, BlockPos pos) { //FORGE: we cannot assume that the x y z of the particles match the block pos of the block.
        if (pos != null) // There are cases where we are not able to obtain the correct source pos, and need to fallback to the non-model data version
            this.setSprite(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getTexture(state, level, pos));
        return this;
    }
}
