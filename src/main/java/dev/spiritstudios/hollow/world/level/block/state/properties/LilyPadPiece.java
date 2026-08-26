package dev.spiritstudios.hollow.world.level.block.state.properties;

import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;
import java.util.function.UnaryOperator;

@SuppressWarnings("Convert2MethodRef")
public enum LilyPadPiece implements StringRepresentable {
	NORTH_WEST(pos -> pos),
	NORTH_EAST(pos -> pos.west()),
	SOUTH_WEST(pos -> pos.north()),
	SOUTH_EAST(pos -> pos.north().west());

	private final UnaryOperator<BlockPos> northWestOffset;

	LilyPadPiece(UnaryOperator<BlockPos> northWestOffset) {
		this.northWestOffset = northWestOffset;
	}

	public BlockPos getNorthWest(BlockPos pos) {
		return this.northWestOffset.apply(pos);
	}

	public BlockPos[] getAllPositions(BlockPos pos) {
		BlockPos northWest = this.getNorthWest(pos);
		return new BlockPos[] {
			northWest,
			northWest.east(),
			northWest.south(),
			northWest.south().east()
		};
	}

	@Override
	public String getSerializedName() {
		return this.name().toLowerCase(Locale.ROOT);
	}
}
