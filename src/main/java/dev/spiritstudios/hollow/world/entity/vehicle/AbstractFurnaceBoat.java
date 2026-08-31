package dev.spiritstudios.hollow.world.entity.vehicle;

import java.util.function.Supplier;

import dev.spiritstudios.hollow.util.TickUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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

public abstract class AbstractFurnaceBoat extends AbstractBoat {
	private static final EntityDataAccessor<Boolean> DATA_ID_FUEL = SynchedEntityData.defineId(AbstractFurnaceBoat.class, EntityDataSerializers.BOOLEAN);
	private static final String FUEL_KEY = "Fuel";

	private static final int FUEL_TICKS_PER_ITEM = TickUtils.ticksFromMins(3);
	private static final int MAX_FUEL_TICKS = TickUtils.ticksFromMinsAndSecs(26, 40);

	private static final double ADDITIONAL_SPEED = 0.05;

	private int fuel = 0;

	public AbstractFurnaceBoat(EntityType<? extends AbstractFurnaceBoat> type, Level level, Supplier<Item> dropItem) {
		super(type, level, dropItem);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder entityData) {
		super.defineSynchedData(entityData);
		entityData.define(DATA_ID_FUEL, false);
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level().isClientSide()) {
			if (this.fuel > 0)
				this.fuel--;

			this.setHasFuel(this.fuel > 0);
		}

		if (this.hasFuel()) {
			this.getDeltaMovement().add(
				Mth.sin(Math.toRadians(-this.getYRot())) * ADDITIONAL_SPEED,
				0.0,
				Mth.cos(Math.toRadians(this.getYRot())) * ADDITIONAL_SPEED
			);

			if (this.random.nextInt(4) == 0) {
				this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 0.8, this.getZ(), 0.0, 0.0, 0.0);
			}
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
		ItemStack itemStack = player.getItemInHand(hand);
		if (this.addFuel(itemStack)) {
			itemStack.consume(1, player);
		}

		return InteractionResult.SUCCESS;
	}

	public boolean addFuel(ItemStack itemStack) {
		if (!itemStack.is(ItemTags.FURNACE_MINECART_FUEL) || this.fuel + FUEL_TICKS_PER_ITEM > MAX_FUEL_TICKS) {
			return false;
		}

		this.fuel += FUEL_TICKS_PER_ITEM;

		return true;
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

	protected boolean hasFuel() {
		return this.entityData.get(DATA_ID_FUEL);
	}

	protected void setHasFuel(boolean fuel) {
		this.entityData.set(DATA_ID_FUEL, fuel);
	}
}
