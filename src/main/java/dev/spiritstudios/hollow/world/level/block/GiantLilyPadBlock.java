package dev.spiritstudios.hollow.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LilyPadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GiantLilyPadBlock extends LilyPadBlock {
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 1.5, 16.0);

    public static final EnumProperty<Piece> PIECE = EnumProperty.create("piece", Piece.class);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    public GiantLilyPadBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(PIECE, Piece.NORTH_WEST).setValue(FACING, Direction.NORTH));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Level world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();

        if (posInvalid(world, pos) || posInvalid(world, pos.east()) || posInvalid(world, pos.south()) || posInvalid(world, pos.east().south())
        ) return null;

        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    private boolean posInvalid(Level world, BlockPos pos) {
        return (!world.isWaterAt(pos.below()) && !world.getBlockState(pos.below()).is(BlockTags.ICE)) || !world.isEmptyBlock(pos);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.isClientSide() || state.getValue(PIECE) != Piece.NORTH_WEST) return;

        world.setBlockAndUpdate(pos.south(), state.setValue(PIECE, Piece.SOUTH_WEST));
        world.setBlockAndUpdate(pos.east(), state.setValue(PIECE, Piece.NORTH_EAST));
        world.setBlockAndUpdate(pos.south().east(), state.setValue(PIECE, Piece.SOUTH_EAST));
    }


    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean moved) {
        super.affectNeighborsAfterRemoval(state, world, pos, moved);

        for (BlockPos blockPos : getBlocks(pos, state)) {
            if (blockPos.equals(pos)) continue;
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.is(this)) world.destroyBlock(blockPos, false);
        }
    }

    public List<BlockPos> getBlocks(BlockPos pos, BlockState state) {
        List<BlockPos> blocks = new ArrayList<>();
        if (state.getBlock() != this) return blocks;

        BlockPos northWest = switch (state.getValue(PIECE)) {
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(PIECE, FACING); }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) { return SHAPE; }

    public enum Piece implements StringRepresentable {
        NORTH_WEST,
        NORTH_EAST,
        SOUTH_WEST,
        SOUTH_EAST;

        @Override
        public String getSerializedName() { return this.name().toLowerCase(Locale.ROOT); }
    }
}
