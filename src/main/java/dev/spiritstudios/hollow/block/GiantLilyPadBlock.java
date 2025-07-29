package dev.spiritstudios.hollow.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.LilyPadBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GiantLilyPadBlock extends LilyPadBlock {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.5, 16.0);

    public static final EnumProperty<Piece> PIECE = EnumProperty.of("piece", Piece.class);
    public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;

    public GiantLilyPadBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(PIECE, Piece.NORTH_WEST).with(FACING, Direction.NORTH));
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();

        if (posInvalid(world, pos) || posInvalid(world, pos.east()) || posInvalid(world, pos.south()) || posInvalid(world, pos.east().south())
        ) return null;

        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing());
    }

    private boolean posInvalid(World world, BlockPos pos) {
        return (!world.isWater(pos.down()) && !world.getBlockState(pos.down()).isIn(BlockTags.ICE)) || !world.isAir(pos);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.isClient() || state.get(PIECE) != Piece.NORTH_WEST) return;

        world.setBlockState(pos.south(), state.with(PIECE, Piece.SOUTH_WEST));
        world.setBlockState(pos.east(), state.with(PIECE, Piece.NORTH_EAST));
        world.setBlockState(pos.south().east(), state.with(PIECE, Piece.SOUTH_EAST));
    }


    @Override
    protected void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
        super.onStateReplaced(state, world, pos, moved);

        for (BlockPos blockPos : getBlocks(pos, state)) {
            if (blockPos.equals(pos)) continue;
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.isOf(this)) world.breakBlock(blockPos, false);
        }
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
