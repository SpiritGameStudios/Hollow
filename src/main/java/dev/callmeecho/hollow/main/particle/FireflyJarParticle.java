package dev.callmeecho.hollow.main.particle;

import dev.callmeecho.hollow.main.Hollow;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

public class FireflyJarParticle extends SpriteBillboardParticle {
    private final boolean xMover;
    private final boolean counterClockwise;
    
    protected FireflyJarParticle(ClientWorld clientWorld, double x, double y, double z, SpriteProvider spriteProvider) {
        super(clientWorld, x, y, z);
        this.maxAge = 400;
        this.red = 0.101960786f;
        this.green = 0.11764706f;
        this.blue = 0.105882354f;
        
        xMover = clientWorld.random.nextBoolean();
        counterClockwise = clientWorld.random.nextBoolean();
        
        this.alpha = 0;
        this.scale = 0.5F;
        this.setSpriteForAge(spriteProvider);
    }
    
    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }
    
    private int lightTicks;

    @Override
    public void tick() {
        super.tick();
        
        if (this.age % 5 == 0 && this.random.nextInt(5) == 0) {
            lightTicks -= this.random.nextBetween(10, 15);
        } else {
            lightTicks += 1;
        }

        lightTicks = MathHelper.clamp(lightTicks, 0, 15);

        int color = ColorHelper.Argb.lerp(MathHelper.clampedLerp(0.0F, 15.0F, (1.0F - lightTicks / 10.0F)) / 15.0F, 0xFF92CF40, 0xFF1A1E1B);

        this.red = ColorHelper.Argb.getRed(color) / 255f;
        this.green = ColorHelper.Argb.getGreen(color) / 255f;
        this.blue = ColorHelper.Argb.getBlue(color) / 255f;

        if (this.age < 20) {
            this.alpha += 0.05f;
        }

        if (this.age > this.maxAge - 20) {
            this.alpha -= 0.05f;
        }

        // Hover around in circles
        double newY = MathHelper.sin((float) this.age / 15) * 0.0025;
        double newXZ = MathHelper.cos((float) this.age / 15) * 0.0025;
        
        this.x += xMover ? newXZ : -newXZ;


    }
    

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ) {
            return new FireflyJarParticle(clientWorld, x, y, z, this.spriteProvider);
        }
    }
}
