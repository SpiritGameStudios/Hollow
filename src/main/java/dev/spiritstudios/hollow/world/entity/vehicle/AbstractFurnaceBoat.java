package dev.spiritstudios.hollow.world.entity.vehicle;

import dev.spiritstudios.hollow.advancements.triggers.HollowCriteriaTriggers;
import dev.spiritstudios.hollow.util.TickUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public abstract class AbstractFurnaceBoat extends AbstractBoat {
	private static final EntityDataAccessor<Boolean> DATA_ID_FUEL = SynchedEntityData.defineId(AbstractFurnaceBoat.class, EntityDataSerializers.BOOLEAN);

	private static final String FUEL_KEY = "Fuel";
	private static final String CAN_PROPEL_KEY = "can_propel";

	private static final int FUEL_TICKS_PER_ITEM = TickUtils.ticksFromMins(2);
	private static final int MAX_FUEL_TICKS = TickUtils.ticksFromMins(30);

	public static final double PROPULSION_SPEED = 0.04;
	private static final Vec3 SMOKE_PARTICLE_POS = new Vec3(0.0, 1.1, -0.5);

	private int fuel = 0;
	private boolean canPropel = false;

	public AbstractFurnaceBoat(EntityType<? extends AbstractFurnaceBoat> type, Level level, Supplier<Item> dropItem) {
		super(type, level, dropItem);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder entityData) {
		super.defineSynchedData(entityData);
		entityData.define(DATA_ID_FUEL, false);
	}

	@Override
	public void baseTick() {
		super.baseTick();
		Level level = this.level();

		if (!level.isClientSide()) {
			if (this.fuel > 0) {
				this.fuel--;
			}

			this.setHasFuel(this.fuel > 0);
		}

		if (this.hasFuel()) {
			Vec3 particlePos = this.position().add(SMOKE_PARTICLE_POS.yRot(-this.getYRot() * Mth.DEG_TO_RAD));

			if (this.random.nextInt(4) == 0) {
				level.addParticle(ParticleTypes.LARGE_SMOKE, particlePos.x, particlePos.y, particlePos.z, 0.0, 0.0, 0.0);
			}

			this.tickPropulsion();
		}

		if (!this.hasFuel() || this.status != Status.IN_WATER) {
			this.canPropel = false;
		}
	}

	private void tickPropulsion() { // todo: fix players stopping movement when dismounting
		if (this.status == Status.IN_WATER && (this.inputUp || this.getFirstPassenger() != null && !(this.getFirstPassenger() instanceof Player))) {
			this.canPropel = true;
		}

		if (this.canPropel) {
			float radians = this.getYRot() * Mth.DEG_TO_RAD;
			this.setDeltaMovement(this.getDeltaMovement().add(
				Mth.sin(-radians) * PROPULSION_SPEED,
				0.0,
				Mth.cos(radians) * PROPULSION_SPEED
			));
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
			if (player instanceof ServerPlayer serverPlayer) {
				HollowCriteriaTriggers.PLAYER_FUELED_ENTITY.trigger(serverPlayer, itemStack, this);
			}

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
		output.putBoolean(CAN_PROPEL_KEY, this.canPropel);
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);

		this.fuel = input.getShortOr(FUEL_KEY, (short) 0);
		this.canPropel = input.getBooleanOr(CAN_PROPEL_KEY, false);
	}

	public boolean hasFuel() {
		return this.entityData.get(DATA_ID_FUEL);
	}

	protected void setHasFuel(boolean fuel) {
		this.entityData.set(DATA_ID_FUEL, fuel);
	}

	public boolean isPropelled() {
		return this.hasFuel() && this.canPropel;
	}
}
