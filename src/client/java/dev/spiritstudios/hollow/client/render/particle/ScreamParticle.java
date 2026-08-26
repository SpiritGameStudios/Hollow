package dev.spiritstudios.hollow.client.render.particle;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Ease;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.Quaternionf;

public class ScreamParticle extends SingleQuadParticle {
    protected ScreamParticle(ClientLevel level, TextureAtlasSprite sprite, double x, double y, double z) {
        super(level, x, y, z, sprite);

        this.setSize(0.01F, 0.01F);
        this.quadSize = 10;
        this.lifetime = 50;

        this.hasPhysics = false;
        this.friction = 1.0F;
        this.gravity = 0.0F;
    }


    @Override
    public float getQuadSize(float a) {
        return Ease.outCirc(Math.min(age + a, getLifetime()) / getLifetime()) * quadSize;
    }

    @Override
    public void extract(QuadParticleRenderState particleTypeRenderState, Camera camera, float partialTickTime) {
        this.alpha = 1.0F - Mth.clamp((this.age + partialTickTime) / this.lifetime, 0.0F, 1.0F);

		this.y += Mth.EPSILON;
		this.yo += Mth.EPSILON;
        Quaternionf rotation = new Quaternionf();
        rotation.rotationX(-Mth.HALF_PI);
        this.extractRotatedQuad(particleTypeRenderState, camera, rotation, partialTickTime);
        rotation.rotationYXZ((float) -Math.PI, Mth.HALF_PI, 0.0F);
        this.extractRotatedQuad(particleTypeRenderState, camera, rotation, partialTickTime);
		this.y -= Mth.EPSILON;
		this.yo -= Mth.EPSILON;
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    public void tick() {
        age++;

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (alpha < 0.0F) this.remove();
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet spriteProvider) {
            this.sprite = spriteProvider;
        }

        @Override
        public @org.jspecify.annotations.Nullable Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            ScreamParticle particle = new ScreamParticle(
                    level,
					this.sprite.get(random),
					x, y, z
            );

            particle.setColor(1.0F, 1.0F, 0.75F);
            particle.setParticleSpeed(xAux, yAux, zAux);

            return particle;
        }
    }


    @Override
    protected int getLightCoords(float a) {
        return LightCoordsUtil.withBlock(super.getLightCoords(a), 15);
    }
}
