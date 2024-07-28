package dev.callmeecho.hollow.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.PathUtil;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GiantLilyPadBlock extends LilyPadBlock {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.5, 16.0);

    public static final EnumProperty<Piece> PIECE = EnumProperty.of("piece", Piece.class);
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public GiantLilyPadBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(PIECE, Piece.NORTH_WEST).with(FACING, Direction.NORTH));
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        for (BlockPos blockPos : getBlocks(pos, state)) {
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.getBlock() == this) world.breakBlock(blockPos, blockPos.equals(pos));
        }

        return super.onBreak(world, pos, state, player);
    }

    public List<BlockPos> getBlocks(BlockPos pos, BlockState state) {
        List<BlockPos> blocks = new ArrayList<>();
        if (state.getBlock() != this) return blocks;

        BlockPos northWest = switch (state.get(PIECE)) {
            case NORTH_WEST -> pos;
            case NORTH_EAST -> pos.west();
            case SOUTH_WEST -> pos.north();
            case SOUTH_EAST -> pos.north().west();
        };

        blocks.add(northWest);
        blocks.add(northWest.south());
        blocks.add(northWest.east());
        blocks.add(northWest.south().east());

        return blocks;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) { builder.add(PIECE, FACING); }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) { return SHAPE; }

    public enum Piece implements StringIdentifiable {
        NORTH_WEST,
        NORTH_EAST,
        SOUTH_WEST,
        SOUTH_EAST;


        @Override
        public String asString() { return this.name().toLowerCase(Locale.ROOT); }
    }
}
