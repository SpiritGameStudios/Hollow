package dev.spiritstudios.hollow.world.level.block.pot;

import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jspecify.annotations.Nullable;

public interface TickingEntityBlock<T extends BlockEntity> extends EntityBlock {
	BlockEntityType<T> getType();

	@SuppressWarnings("unchecked")
	default <A extends BlockEntity> @Nullable BlockEntityTicker<A> validateTicker(BlockEntityType<A> actual, BlockEntityTicker<? super T> ticker) {
		return this.getType() == actual ? (BlockEntityTicker<A>) ticker : null;
	}
}
