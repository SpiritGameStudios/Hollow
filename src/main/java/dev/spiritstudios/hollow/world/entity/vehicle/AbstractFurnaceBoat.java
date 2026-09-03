package dev.spiritstudios.hollow.world.entity.vehicle;

import com.mojang.logging.LogUtils;
import dev.spiritstudios.hollow.advancements.triggers.HollowCriteriaTriggers;
import dev.spiritstudios.hollow.network.ServerboundPropelFurnaceBoatPayload;
import dev.spiritstudios.hollow.util.TickUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.FuelValues;
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

	private static final int FUEL_TICKS_PER_ITEM = TickUtils.fromMins(3);
	private static final int MAX_FUEL_TICKS = TickUtils.fromHrs(1);

	public static final double PROPULSION_SPEED = 0.04;
	public static final double PROPULSION_SPEED_SLOW = 0.02;

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
				level.playLocalSound(animPos.x, animPos.y, animPos.z, SoundEvents.FURNACE_FIRE_CRACKLE, this.getSoundSource(), 1.0F, 1.0F, false);
			}

			this.tickPropulsion();
		}

		if (this.getFirstPassenger() instanceof ServerPlayer serverPlayer && this.isPoweredByFurnace()) {
			HollowCriteriaTriggers.PLAYER_PROPEL_FURNACE_BOAT.trigger(serverPlayer);
		}

		if (!this.hasFuel() || this.status != Status.IN_WATER) {
			this.sendIsPropelled(false);
		}
	}

	@Override
	public boolean isClientAuthoritative() {
		return super.isClientAuthoritative() && !this.isPropelled();
	}

	private void tickPropulsion() {
		if (this.status == Status.IN_WATER && this.getDeltaMovement().lengthSqr() > Mth.EPSILON) {
			this.sendIsPropelled(true);
		}

		if (this.isPropelled()) {
			float rad = this.getYRot() * Mth.DEG_TO_RAD;
			this.setDeltaMovement(this.getDeltaMovement().add(
				Mth.sin(-rad) * this.getPropulsionSpeed(),
				0.0,
				Mth.cos(rad) * this.getPropulsionSpeed()
			));
		}
	}

	private double getPropulsionSpeed() {
		return this.fuel <= TickUtils.fromSecs(5) ? PROPULSION_SPEED_SLOW : PROPULSION_SPEED;
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

		ItemStack itemStack = player.getItemInHand(hand);

		if (this.canAddPassenger(player) && !player.isSecondaryUseActive() || !this.addFuel(itemStack)) {
			return InteractionResult.PASS;
		}

		ItemStackTemplate remainderStack = itemStack.getCraftingRemainder();
		itemStack.consume(1, player);

		if (remainderStack != null) {
			ItemStack remainder = remainderStack.create();
			if (itemStack.isEmpty()) {
				player.setItemInHand(hand, remainder);
			}
			else if (!player.addItem(remainder)) {
				player.drop(remainder, false);
			}
		}

		player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));

		return InteractionResult.SUCCESS;
	}

	public boolean addFuel(ItemStack itemStack) {
		FuelValues fuelValues = this.level().fuelValues();

		if (!fuelValues.isFuel(itemStack) || this.fuel == MAX_FUEL_TICKS) {
			return false;
		}

		float duration = fuelValues.burnDuration(itemStack) * getFuelScaleQuotient(fuelValues);
		this.fuel = Math.min(MAX_FUEL_TICKS, this.fuel + Mth.floor(duration));

		if (this.fuel > MAX_FUEL_TICKS) {
			this.fuel = MAX_FUEL_TICKS;
		}

		return true;
	}

	private static float getFuelScaleQuotient(FuelValues fuelValues) {
		return (float) FUEL_TICKS_PER_ITEM / fuelValues.burnDuration(Items.COAL.getDefaultInstance());
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt(FUEL_KEY, this.fuel);
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.fuel = input.getIntOr(FUEL_KEY, 0);
	}

	public boolean hasFuel() {
		return this.entityData.get(DATA_ID_FUEL);
	}

	public boolean isPropelled() {
		return this.entityData.get(DATA_ID_PROPELLED);
	}

	public void setHasFuel(boolean fuel) {
		this.entityData.set(DATA_ID_FUEL, fuel);
	}

	public void setIsPropelled(boolean propelled) {
		this.entityData.set(DATA_ID_PROPELLED, propelled);
	}

	private void sendIsPropelled(boolean propelled) {
		if (this.isPropelled() == !propelled) {
			if (this.level().isClientSide()) {
				ClientPlayNetworking.send(new ServerboundPropelFurnaceBoatPayload(this.uuid, propelled));
			}
			else this.setIsPropelled(propelled);
		}
	}

	public boolean isPoweredByFurnace() {
		return this.hasFuel() && this.isPropelled();
	}
}
