package dev.spiritstudios.hollow.client.render.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class JarFireflyParticle extends SingleQuadParticle {
    protected JarFireflyParticle(final ClientLevel level, final double x, final double y, final double z, final double xa, final double ya, final double za, final TextureAtlasSprite sprite) {
        super(level, x, y, z, xa, ya, za, sprite);
        this.speedUpWhenYMotionIsBlocked = true;
        this.friction = 0.9F;
        this.quadSize *= 0.75F;
        this.yd *= 0.8F;
        this.xd *= 0.8F;
        this.zd *= 0.8F;
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    public int getLightCoords(final float a) {
        return (int) (255.0F * getFadeAmount(this.getLifetimeProgress((float) this.age + a), 0.1F, 0.3F));
    }

    @Override
    public void tick() {
        super.tick();

        this.setAlpha(getFadeAmount(this.getLifetimeProgress((float) this.age), 0.3F, 0.5F));
        if (this.random.nextFloat() > 0.5F) {
            this.setParticleSpeed(
                    -0.05F + 0.1F * this.random.nextFloat(),
                    -0.05F + 0.1F * this.random.nextFloat(),
                    -0.05F + 0.1F * this.random.nextFloat()
            );
        }
    }

    private float getLifetimeProgress(final float currentAge) {
        return Mth.clamp(currentAge / (float) this.lifetime, 0.0F, 1.0F);
    }

    private static float getFadeAmount(final float lifetimeProgress, final float fadeInTime, final float fadeOutTime) {
        if (lifetimeProgress >= 1.0F - fadeInTime) {
            return (1.0F - lifetimeProgress) / fadeInTime;
        } else {
            return lifetimeProgress <= fadeOutTime ? lifetimeProgress / fadeOutTime : 1.0F;
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(final SpriteSet sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(final SimpleParticleType options, final ClientLevel level, final double x, final double y, final double z, final double xAux, final double yAux, final double zAux, final RandomSource random) {
            JarFireflyParticle particle = new JarFireflyParticle(level, x, y, z, 0.0F, 0.0F, 0.0F, this.sprite.get(random));
            particle.setLifetime(random.nextIntBetweenInclusive(200, 300));
            particle.scale(1.5F);
            particle.setAlpha(0.0F);
            return particle;
        }
    }
}
