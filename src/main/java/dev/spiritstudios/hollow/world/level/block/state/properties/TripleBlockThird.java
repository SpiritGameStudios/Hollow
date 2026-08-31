package dev.spiritstudios.hollow.world.level.block.state.properties;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;

public enum TripleBlockThird implements StringRepresentable {
	UPPER("upper", -2, -1),
	MIDDLE("middle", -1, 1),
	LOWER("lower", 1, 2);

	private final String path;
	private final int relativeY1, relativeY2;

	TripleBlockThird(String path, int relativeY1, int relativeY2) {
		this.path = path;
		this.relativeY1 = relativeY1;
		this.relativeY2 = relativeY2;
	}

	public BlockPos getRelativePos1(BlockPos pos) {
		return pos.relative(Direction.Axis.Y, this.relativeY1);
	}

	public BlockPos getRelativePos2(BlockPos pos) {
		return pos.relative(Direction.Axis.Y, this.relativeY2);
	}

	public BlockPos getLowerPos(BlockPos pos) {
		return switch (this) {
			case LOWER -> pos;
			case MIDDLE -> pos.below();
			case UPPER -> pos.below(2);
		};
	}

	@Override
	public String getSerializedName() {
		return this.path;
	}
}
