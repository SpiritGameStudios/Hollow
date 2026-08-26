package dev.spiritstudios.hollow.world.level.block.entity.pot;

import dev.spiritstudios.hollow.world.level.block.entity.HollowBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jspecify.annotations.Nullable;

public class PotBlockEntity extends BlockEntity {
	public long wobbleStartedAtTick;
	public DecoratedPotBlockEntity.@Nullable WobbleStyle lastWobbleStyle;

	public PotBlockEntity(BlockPos pos, BlockState state) {
		this(HollowBlockEntityTypes.POT, pos, state);
	}

	public PotBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public Direction getDirection() {
		return this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
	}

	public void use(Player player, InteractionHand hand) {
		Level level = player.level();

		wobble(DecoratedPotBlockEntity.WobbleStyle.NEGATIVE);
		level.playSound(null, worldPosition, SoundEvents.DECORATED_POT_INSERT_FAIL, SoundSource.BLOCKS, 1.0F, 1.0F);
		level.gameEvent(player, GameEvent.BLOCK_CHANGE, worldPosition);
	}

	public void wobble(DecoratedPotBlockEntity.WobbleStyle wobbleType) {
		if (this.level != null && !this.level.isClientSide()) {
			this.level.blockEvent(
				this.getBlockPos(), this.getBlockState().getBlock(),
				DecoratedPotBlockEntity.EVENT_POT_WOBBLES, wobbleType.ordinal()
			);
		}
	}

	@Override
	public boolean triggerEvent(int event, int data) {
		if (this.level != null && event == DecoratedPotBlockEntity.EVENT_POT_WOBBLES && data >= 0 && data < DecoratedPotBlockEntity.WobbleStyle.values().length) {
			this.wobbleStartedAtTick = this.level.getGameTime();
			this.lastWobbleStyle = DecoratedPotBlockEntity.WobbleStyle.values()[data];
			return true;
		} else {
			return super.triggerEvent(event, data);
		}
	}
}
