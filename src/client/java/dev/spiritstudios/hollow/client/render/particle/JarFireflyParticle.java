package dev.spiritstudios.hollow.client.render.particle;

import dev.spiritstudios.hollow.client.color.item.Jeb;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class JarFireflyParticle extends SingleQuadParticle {
	private final boolean isJeb;
	private final float ageOffset;

	protected JarFireflyParticle(ClientLevel level, double x, double y, double z, boolean isJeb, TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);
		this.quadSize *= 0.75F;
		this.isJeb = isJeb;
		this.ageOffset = level.getRandom().nextFloat();
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    public int getLightCoords(float a) {
        return (int) (255.0F * getFadeAmount(this.getLifetimeProgress((float) this.age + a), 0.1F, 0.3F));
    }

    @Override
    public void tick() {
		if (this.age++ >= this.lifetime) {
			this.remove();
		}

		float lifetimeProgress = this.getLifetimeProgress((float) this.age);

	    if (this.isJeb) {
			int color = Jeb.getColor(Mth.frac(lifetimeProgress + this.ageOffset));
			this.rCol = ARGB.redFloat(color);
			this.gCol = ARGB.greenFloat(color);
			this.bCol = ARGB.blueFloat(color);
		}

		this.setAlpha(getFadeAmount(lifetimeProgress, 0.3F, 0.5F));
    }

    private float getLifetimeProgress(float currentAge) {
        return Mth.clamp(currentAge / (float) this.lifetime, 0.0F, 1.0F);
    }

    private static float getFadeAmount(float lifetimeProgress, float fadeInTime, float fadeOutTime) {
        if (lifetimeProgress >= 1.0F - fadeInTime) {
            return (1.0F - lifetimeProgress) / fadeInTime;
        } else if (lifetimeProgress <= fadeOutTime) {
			return lifetimeProgress / fadeOutTime;
		}

		return 1.0F;
    }

	public record Provider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {
		public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
			JarFireflyParticle particle = new JarFireflyParticle(level, x, y, z, xAux == 1.0, this.sprite.get(random));
			particle.setLifetime(random.nextIntBetweenInclusive(60, 100));
			particle.scale(1.5F);
			particle.setAlpha(0.0F);
			return particle;
		}
	}
}
