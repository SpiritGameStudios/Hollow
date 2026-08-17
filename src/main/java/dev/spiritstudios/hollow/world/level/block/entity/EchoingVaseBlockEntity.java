package dev.spiritstudios.hollow.world.level.block.entity;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.world.level.block.ScreamingVaseBlock;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.DOUBLE_BLOCK_HALF;

public class EchoingVaseBlockEntity extends BlockEntity {
	public static int TILT_TIME = 10;
	public static int FALL_TIME = 20;

	public int activeTime = 0;
	public long wobbleStartedAtTick;
	public Entity fallCauser;
	public int fallTime = 0;
	public boolean fallen = false;
	public Direction fallDirection = Direction.NORTH;
	public DecoratedPotBlockEntity.WobbleStyle lastWobbleStyle;

	public EchoingVaseBlockEntity(BlockPos pos, BlockState state) {
		super(HollowBlockEntityTypes.ECHOING_VASE, pos, state);
	}

	public Direction getDirection() {
		return this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
	}

	public void use(Player player, InteractionHand hand) {
		wobble(DecoratedPotBlockEntity.WobbleStyle.NEGATIVE);
		player.level().playSound(null, worldPosition, SoundEvents.DECORATED_POT_INSERT_FAIL, SoundSource.BLOCKS, 1.0F, 1.0F);
		player.level().gameEvent(player, GameEvent.BLOCK_CHANGE, worldPosition);

	}

	public void wobble(DecoratedPotBlockEntity.WobbleStyle wobbleType) {
		if (this.level == null || this.level.isClientSide()) return;

		this.level.blockEvent(this.getBlockPos(), this.getBlockState().getBlock(), 1, wobbleType.ordinal());

		this.level.blockEvent(this.getBlockPos().above(), this.getBlockState().getBlock(), 1, wobbleType.ordinal());
	}

	public void setFalling(Direction dir, boolean top, Level world, BlockPos pos, @Nullable Entity fallCauser) {
		this.fallTime = 1;
		this.fallDirection = dir;
		this.fallCauser = fallCauser;
		if (top) {
			BlockEntity be = world.getBlockEntity(pos.above());
			if (be instanceof EchoingVaseBlockEntity echoing) {
				echoing.setFalling(dir, false, world, pos, fallCauser);
			} else {
				Hollow.LOGGER.error("Missing top block entity for echoing vase at {}", pos.above());
			}
		}
	}

	public static void tick(Level world, BlockPos pos, BlockState state, EchoingVaseBlockEntity entity) {
		if (entity.fallTime == 0) return;
		entity.fallTime++;

		if (entity.fallTime <= EchoingVaseBlockEntity.FALL_TIME || entity.fallen) return;

		world.playLocalSound(pos, SoundEvents.DECORATED_POT_SHATTER, SoundSource.BLOCKS, 1, 1, true);

		world.setBlockAndUpdate(pos.above(), Blocks.AIR.defaultBlockState());
		world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

		world.addDestroyBlockEffect(pos.relative(entity.fallDirection), state);
		world.addDestroyBlockEffect(pos.relative(entity.fallDirection, 2), state);

		ScreamingVaseBlock.onBreakLower(world, pos, state, entity.fallCauser);
	}

	public void onEntityCollision(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (!state.is(HollowBlocks.SCREAMING_VASE) ||
				this.fallTime != 0 ||
				state.getValue(DOUBLE_BLOCK_HALF).equals(DoubleBlockHalf.UPPER) ||
				!world.getBlockState(pos.above()).is(HollowBlocks.SCREAMING_VASE)
		) return;

		Direction fallDirection = Direction.getApproximateNearest(Vec3.atCenterOf(pos).subtract(entity.position()));

		BlockPos lowerPos = pos.relative(fallDirection);
		BlockPos upperPos = pos.relative(fallDirection, 2);

		if (!world.getBlockState(lowerPos).isAir() || !world.getBlockState(upperPos).isAir()) return;

		this.setFalling(Direction.getApproximateNearest(Vec3.atCenterOf(pos).subtract(entity.position())), true, world, pos, entity);
		if (!world.isClientSide()) {
			((ServerLevel) world).getChunkSource().blockChanged(this.getBlockPos());
		}
	}

	@Override
	public boolean triggerEvent(int event, int data) {
		if (this.level != null && event == 1 && data >= 0 && data < DecoratedPotBlockEntity.WobbleStyle.values().length) {
			this.wobbleStartedAtTick = this.level.getGameTime();
			this.lastWobbleStyle = DecoratedPotBlockEntity.WobbleStyle.values()[data];
			return true;
		} else {
			return super.triggerEvent(event, data);
		}
	}

	// region NBT
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(final HolderLookup.Provider registries) {
		return this.saveCustomOnly(registries);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		output.putInt("ActiveTime", activeTime);
		if (this.fallDirection != null && this.fallTime > 0) {
			output.putInt("FallDir", this.fallDirection.ordinal());
		}

		super.saveAdditional(output);
	}

	@Override
	public void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		activeTime = input.getIntOr("ActiveTime", 0);

		input.getInt("FallDir").ifPresent(fallDir -> {
			this.fallDirection = Direction.values()[fallDir];
			if (this.level != null && this.fallTime == 0 && this.getBlockState().getValue(DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
				this.setFalling(this.fallDirection, true, this.level, this.getBlockPos(), null);
			}
		});
	}
	// endregion
}