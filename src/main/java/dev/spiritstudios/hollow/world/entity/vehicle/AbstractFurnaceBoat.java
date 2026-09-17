package dev.spiritstudios.hollow.world.entity.vehicle;

import dev.spiritstudios.hollow.advancements.triggers.HollowCriteriaTriggers;
import dev.spiritstudios.hollow.network.ServerboundPropelFurnaceBoatPayload;
import dev.spiritstudios.hollow.util.TickUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Ease;
import net.minecraft.util.Mth;
import net.minecraft.util.Prediction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoveSimulationType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class AbstractFurnaceBoat extends AbstractBoat {
	private static final EntityDataAccessor<Boolean> DATA_ID_FUEL = SynchedEntityData.defineId(AbstractFurnaceBoat.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DATA_ID_PROPELLED = SynchedEntityData.defineId(AbstractFurnaceBoat.class, EntityDataSerializers.BOOLEAN);

	private static final String FUEL_KEY = "Fuel";

	private static final int FUEL_TICKS_PER_ITEM = TickUtils.fromMins(3);
	private static final int MAX_FUEL_TICKS = TickUtils.fromHrs(1);
	private static final int SPUTTER_OUT_TICKS = TickUtils.fromSecs(5);

	public static final float PROPULSION_SPEED = 0.04F;

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
				this.needsSync = true;
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
			this.setIsPropelled(false);
		}
	}

	@Override
	public boolean isClientAuthoritative() {
		return super.isClientAuthoritative() && !this.isPropelled();
	}

	@Override
	public MoveSimulationType getMoveSimulationType() {
		return MoveSimulationType.AUTHORITATIVE_SIDE_AND_SERVER;
	}

	private void tickPropulsion() {
		Vec3 velocity = this.getDeltaMovement();

		if (this.status == Status.IN_WATER && velocity.horizontalDistanceSqr() > Mth.EPSILON) {
			this.setIsPropelled(true);
		}

		if (this.isPropelled()) {
			float rad = this.getYRot() * Mth.DEG_TO_RAD;
			double propulsionSpeed = this.getPropulsionSpeed();

			this.setDeltaMovement(new Vec3(
				velocity.x + Mth.sin(-rad) * propulsionSpeed,
				velocity.y,
				velocity.z + Mth.cos(rad) * propulsionSpeed
			));
		}
	}

	private float getPropulsionSpeed() {
		float delta = (float) this.fuel / SPUTTER_OUT_TICKS;
		return PROPULSION_SPEED * Ease.outQuad(Mth.clamp(delta, 0.0F, 1.0F));
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

		if (this.canAddPassenger(player) && !player.isSecondaryUseActive() || !this.addFuel(this.level(), player, itemStack)) {
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
				player.drop(remainder, false, Prediction.PREDICTED);
			}
		}

		player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));

		return InteractionResult.SUCCESS;
	}

	public boolean addFuel(Level level, Player player, ItemStack itemStack) {
		if (!itemStack.has(DataComponents.COOKING_FUEL) || this.fuel >= MAX_FUEL_TICKS) {
			return false;
		}

		float duration = 300;
		this.fuel = Math.min(MAX_FUEL_TICKS, this.fuel + Mth.floor(duration));
		this.needsSync = true;

		return true;
	}

	private int getBurnDuration(ServerLevel level, Player player, ItemStack fuelItem) {
		LootContext lootContext = new LootContext.Builder(
			new LootParams.Builder(level)
				.withParameter(LootContextParams.TARGET_ENTITY, this)
				.withParameter(LootContextParams.INTERACTING_ENTITY, player)
				.withParameter(LootContextParams.TOOL, fuelItem)
				.create(LootContextParamSets.ENTITY_INTERACT)
			)
			.create(Optional.empty());

		return ResolvableInt.getFromItem(fuelItem, DataComponents.COOKING_FUEL, CookingFuel::burnTime, lootContext, 0);
	}

	private float getFuelScaleQuotient(ServerLevel level, Player player) {
		ItemStack coal = Items.COAL.getDefaultInstance();
		CookingFuel cookingFuel = coal.get(DataComponents.COOKING_FUEL);

		return cookingFuel == null ? 1.0F : (float) FUEL_TICKS_PER_ITEM / this.getBurnDuration(level, player, coal);
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
		this.entityData.set(DATA_ID_FUEL, fuel, true);
	}

	public void setIsPropelled(boolean propelled) {
		this.entityData.set(DATA_ID_PROPELLED, propelled, true);
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
		super.onSyncedDataUpdated(accessor);

		if (accessor == DATA_ID_PROPELLED && this.level().isClientSide()) {
			ClientPlayNetworking.send(new ServerboundPropelFurnaceBoatPayload(this.getId(), this.isPropelled()));
		}
	}

	public boolean isPoweredByFurnace() {
		return this.hasFuel() && this.isPropelled();
	}
}
