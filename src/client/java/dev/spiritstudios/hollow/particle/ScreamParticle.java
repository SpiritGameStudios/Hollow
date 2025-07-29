package dev.spiritstudios.hollow.particle;

import dev.spiritstudios.specter.api.core.math.Easing;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ScreamParticle extends SpriteBillboardParticle {
	public static final Quaternionf FLAT = new Quaternionf().rotationXYZ(
			-MathHelper.HALF_PI,
			0,
			0
	);

	protected ScreamParticle(ClientWorld clientWorld, SpriteProvider spriteProvider, double x, double y, double z) {
		super(clientWorld, x, y, z);

		this.setBoundingBoxSpacing(0.01F, 0.01F);
		this.setSprite(spriteProvider);
		this.scale = 10;
		this.maxAge = 50;

		this.collidesWithWorld = false;
		this.velocityMultiplier = 1.0F;
		this.gravityStrength = 0.0F;
	}


	@Override
	public float getSize(float tickDelta) {
		return (float) Easing.CIRC.out(Math.min(age + tickDelta, getMaxAge()), 0, scale, getMaxAge());
	}

	@Override
	public void render(VertexConsumer vertexConsumer, Camera camera, float tickProgress) {
		this.alpha = 1.0F - MathHelper.clamp(((float) this.age + tickProgress) / (float) this.maxAge, 0.0F, 1.0F);

		Quaternionf relativeRotation = getRotation(
				camera,
				0,
				new Vector3f((float) x, (float) y, (float) z)
		);

		this.renderQuad(
				vertexConsumer,
				new Vector3f(0, MathHelper.EPSILON, 0),
				camera,
				relativeRotation,
				tickProgress,
				getSize(tickProgress)
		);
	}

	@Override
	public void tick() {
		age++;

		this.lastX = this.x;
		this.lastY = this.y;
		this.lastZ = this.z;

		if (alpha < 0.0F) this.markDead();
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	public static class Factory implements ParticleFactory<SimpleParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public @Nullable Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			ScreamParticle particle = new ScreamParticle(
					world,
					this.spriteProvider,
					x, y, z
			);

			particle.setColor(1.0F, 1.0F, 0.75F);
			particle.setVelocity(velocityX, velocityY, velocityZ);

			return particle;
		}
	}

	public static Quaternionf getRotation(Camera camera, double z, Vector3f pos) {
		Quaternionf rotation = new Quaternionf(FLAT);

		Vector3f forward = new Vector3f(0, 0, -1).rotate(rotation);
		Vector3f cameraDelta = pos.sub(camera.getPos().toVector3f());

		if (forward.dot(cameraDelta) < 0) rotation.rotateY(MathHelper.PI);

		rotation.rotateZ((float) z);

		return rotation;
	}

	protected void renderQuad(VertexConsumer vertexConsumer, Vector3f offset, Camera camera, Quaternionf rotation, float tickDelta, float size) {
		Vec3d pos = camera.getPos();
		float x = (float) (MathHelper.lerp(tickDelta, this.lastX + offset.x, this.x + offset.x) - pos.getX());
		float y = (float) (MathHelper.lerp(tickDelta, this.lastY + offset.y, this.y + offset.y) - pos.getY());
		float z = (float) (MathHelper.lerp(tickDelta, this.lastZ + offset.z, this.z + offset.z) - pos.getZ());

		int brightness = this.getBrightness(tickDelta);

		this.quad(
				vertexConsumer,
				rotation,
				x,
				y,
				z,
				1.0F,
				-1.0F,
				size,
				this.getMaxU(),
				this.getMaxV(),
				brightness
		);

		this.quad(
				vertexConsumer,
				rotation,
				x,
				y,
				z,
				1.0F,
				1.0F,
				size,
				this.getMaxU(),
				this.getMinV(),
				brightness
		);

		this.quad(
				vertexConsumer,
				rotation,
				x,
				y,
				z,
				-1.0F,
				1.0F,
				size,
				this.getMinU(),
				this.getMinV(),
				brightness
		);

		this.quad(
				vertexConsumer,
				rotation,
				x,
				y,
				z,
				-1.0F,
				-1.0F,
				size,
				this.getMinU(),
				this.getMaxV(),
				brightness
		);
	}

	@Override
	protected int getBrightness(float tint) {
		return LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE;
	}

	private void quad(VertexConsumer vertexConsumer, Quaternionf quaternionf, float f, float g, float h, float i, float j, float k, float l, float m, int n) {
		Vector3f vector3f = (new Vector3f(i, j, 0.0F)).rotate(quaternionf).mul(k).add(f, g, h);
		vertexConsumer.vertex(vector3f.x(), vector3f.y(), vector3f.z()).texture(l, m).color(this.red, this.green, this.blue, this.alpha).light(n);
	}
}