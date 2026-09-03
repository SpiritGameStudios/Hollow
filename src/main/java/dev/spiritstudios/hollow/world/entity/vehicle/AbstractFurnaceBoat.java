package dev.spiritstudios.hollow.world.entity.vehicle;

import com.mojang.logging.LogUtils;
import dev.spiritstudios.hollow.advancements.triggers.HollowCriteriaTriggers;
import dev.spiritstudios.hollow.util.TickUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import java.util.function.Supplier;

public abstract class AbstractFurnaceBoat extends AbstractBoat {
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final EntityDataAccessor<Boolean> DATA_ID_FUEL = SynchedEntityData.defineId(AbstractFurnaceBoat.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DATA_ID_PROPELLED = SynchedEntityData.defineId(AbstractFurnaceBoat.class, EntityDataSerializers.BOOLEAN);

	private static final String FUEL_KEY = "Fuel";

	private static final int FUEL_TICKS_PER_ITEM = TickUtils.ticksFromMins(2);
	private static final int MAX_FUEL_TICKS = TickUtils.ticksFromMins(30);

	public static final double PROPULSION_SPEED = 0.04;
	private static final double INIT_PROPEL_SPEED_REQUIREMENT_SQR = 0.01;
	private static final Vec3 SMOKE_PARTICLE_POS = new Vec3(0.0, 1.1, -0.5);

	private int fuel = 0;

	public AbstractFurnaceBoat(EntityType<? extends AbstractFurnaceBoat> type, Level level, Supplier<Item> dropItem) {
		super(type, level, dropItem);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder entityData) {
		super.defineSynchedData(entityData);

		entityData.define(DATA_ID_FUEL, false);
		entityData.define(DATA_ID_PROPELLED, false);
	}

	@Override
	public void tick() {
		super.tick();

		Level level = this.level();

		if (!level.isClientSide()) {
			if (this.fuel > 0) {
				this.fuel--;
			}

			this.setHasFuel(this.fuel > 0);
		}

		if (this.hasFuel()) {
			Vec3 animPos = this.position().add(SMOKE_PARTICLE_POS.yRot(-this.getYRot() * Mth.DEG_TO_RAD));

			if (this.random.nextFloat() < 0.25F) {
				level.addParticle(ParticleTypes.LARGE_SMOKE, animPos.x, animPos.y, animPos.z, 0.0, 0.0, 0.0);
			}

			if (this.random.nextFloat() < 0.1F && !this.isSilent()) {
				level.playLocalSound(animPos.x, animPos.y, animPos.z, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
			}

			this.tickPropulsion();
		}

		if (this.getFirstPassenger() instanceof ServerPlayer serverPlayer && this.isPoweredByFurnace()) {
			HollowCriteriaTriggers.PLAYER_PROPEL_FURNACE_BOAT.trigger(serverPlayer);
		}

		if (!this.hasFuel() || this.status != Status.IN_WATER) {
			this.setIsPropelled(false);
		}
	}

	@Override
	protected void removePassenger(Entity passenger) {
		this.setIsPropelled(false);
		super.removePassenger(passenger);
	}

	private void tickPropulsion() {
		if (this.status == Status.IN_WATER && this.getDeltaMovement().lengthSqr() > INIT_PROPEL_SPEED_REQUIREMENT_SQR) {
			this.setIsPropelled(true);
		}

		if (this.isPropelled()) {
			float rad = this.getYRot() * Mth.DEG_TO_RAD;
			this.setDeltaMovement(this.getDeltaMovement().add(
				Mth.sin(-rad) * PROPULSION_SPEED,
				0.0,
				Mth.cos(rad) * PROPULSION_SPEED
			));
			this.syncPosition = true;
		}
	}

	@Override
	protected float getSinglePassengerXOffset() {
		return 0.15F;
	}

	@Override
	protected int getMaxPassengers() {
		return 1;
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
		InteractionResult superInteraction = super.interact(player, hand, location);

		if (superInteraction != InteractionResult.PASS) {
			return superInteraction;
		}

		if (this.canAddPassenger(player) && !player.isSecondaryUseActive()) {
			return InteractionResult.PASS;
		}

		ItemStack itemStack = player.getItemInHand(hand);

		if (this.addFuel(itemStack)) {
			itemStack.consume(1, player);
		}

		return InteractionResult.SUCCESS;
	}

	public boolean addFuel(ItemStack itemStack) {
		if (itemStack.is(ItemTags.FURNACE_MINECART_FUEL) && this.fuel + FUEL_TICKS_PER_ITEM <= MAX_FUEL_TICKS) {
			this.fuel += FUEL_TICKS_PER_ITEM;
			return true;
		}

		return false;
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putShort(FUEL_KEY, (short) this.fuel);
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.fuel = input.getShortOr(FUEL_KEY, (short) 0);
	}

	public boolean hasFuel() {
		return this.entityData.get(DATA_ID_FUEL);
	}

	public boolean isPropelled() {
		return this.entityData.get(DATA_ID_PROPELLED);
	}

	protected void setHasFuel(boolean fuel) {
		this.entityData.set(DATA_ID_FUEL, fuel, true);
	}

	protected void setIsPropelled(boolean propelled) {
		this.entityData.set(DATA_ID_PROPELLED, propelled, true);
	}

	public boolean isPoweredByFurnace() {
		return this.hasFuel() && this.isPropelled();
	}
}
