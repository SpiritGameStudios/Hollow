package dev.spiritstudios.hollow.world.level.block.entity.pot;

import com.mojang.logging.LogUtils;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import dev.spiritstudios.hollow.world.level.block.entity.HollowBlockEntityTypes;
import dev.spiritstudios.hollow.world.level.block.pot.ScreamingVaseBlock;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.DOUBLE_BLOCK_HALF;

public class FallingPotBlockEntity extends PotBlockEntity {
	private static final Logger LOGGER = LogUtils.getLogger();

	public static final int FALL_DURATION = SharedConstants.TICKS_PER_SECOND;

	public @Nullable Entity fallCauser;
	public long fallStartedAtTick = -1;
	public boolean fallen = false;
	public Direction fallDirection = Direction.NORTH;

	public FallingPotBlockEntity(BlockPos pos, BlockState state) {
		super(HollowBlockEntityTypes.FALLING_POT, pos, state);
	}

	@Override
	public Direction getDirection() {
		return this.getBlockState().getValue(ScreamingVaseBlock.FACING);
	}

	@Override
	public void wobble(DecoratedPotBlockEntity.WobbleStyle wobbleType) {
		if (this.level != null && !this.level.isClientSide()) {
			this.level.blockEvent(
				this.getBlockPos(), this.getBlockState().getBlock(),
				DecoratedPotBlockEntity.EVENT_POT_WOBBLES, wobbleType.ordinal()
			);

			this.level.blockEvent(
				this.getBlockPos().above(), this.getBlockState().getBlock(),
				DecoratedPotBlockEntity.EVENT_POT_WOBBLES, wobbleType.ordinal()
			);
		}
	}

	public void startFalling(Direction dir, boolean top, BlockPos pos, @Nullable Entity fallCauser) {
		assert this.level != null;

		this.fallStartedAtTick = this.level.getGameTime();
		this.fallDirection = dir;
		this.fallCauser = fallCauser;

		if (top) {
			if (this.level.getBlockEntity(pos.above()) instanceof FallingPotBlockEntity pot) {
				pot.startFalling(dir, false, pos, fallCauser);
			} else {
				LOGGER.error("Missing top block entity for echoing vase at {}", pos.above());
			}
		}
	}


	public static void tick(Level level, BlockPos pos, BlockState state, FallingPotBlockEntity entity) {
		if (entity.fallStartedAtTick == -1) return;

		long fallTime = level.getGameTime() - entity.fallStartedAtTick;

		if (fallTime <= FallingPotBlockEntity.FALL_DURATION || entity.fallen) return;

		level.playLocalSound(pos, SoundEvents.DECORATED_POT_SHATTER, SoundSource.BLOCKS, 1, 1, true);

		level.setBlockAndUpdate(pos.above(), Blocks.AIR.defaultBlockState());
		level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

		level.addDestroyBlockEffect(pos.relative(entity.fallDirection), state);
		level.addDestroyBlockEffect(pos.relative(entity.fallDirection, 2), state);

		ScreamingVaseBlock.onBreakLower(level, pos, entity.fallCauser);
	}

	public void onEntityCollision(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (!state.is(HollowBlocks.SCREAMING_VASE) ||
			this.fallStartedAtTick != -1 ||
			state.getValue(DOUBLE_BLOCK_HALF).equals(DoubleBlockHalf.UPPER) ||
			!level.getBlockState(pos.above()).is(HollowBlocks.SCREAMING_VASE)
		) return;

		Direction fallDirection = Direction.getApproximateNearest(Vec3.atCenterOf(pos).subtract(entity.position()));

		BlockPos lowerPos = pos.relative(fallDirection);
		BlockPos upperPos = pos.relative(fallDirection, 2);

		if (!level.getBlockState(lowerPos).isAir() || !level.getBlockState(upperPos).isAir()) return;

		this.startFalling(Direction.getApproximateNearest(Vec3.atCenterOf(pos).subtract(entity.position())), true, pos, entity);

		if (!level.isClientSide()) {
			((ServerLevel) level).getChunkSource().blockChanged(this.getBlockPos());
		}
	}

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
		if (this.fallStartedAtTick != -1) {
			output.putInt("FallDir", this.fallDirection.ordinal());
		}

		super.saveAdditional(output);
	}

	@Override
	public void loadAdditional(ValueInput input) {
		super.loadAdditional(input);

		input.getInt("FallDir").ifPresent(fallDir -> {
			this.fallDirection = Direction.values()[fallDir];
			if (this.level != null && this.fallStartedAtTick == -1 && this.getBlockState().getValue(DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
				this.startFalling(this.fallDirection, true, this.getBlockPos(), null);
			}
		});
	}
}
