package dev.spiritstudios.hollow.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

public class CattailStemBlock extends AbstractPlantBlock implements FluidFillable {
    public static final MapCodec<CattailStemBlock> CODEC = createCodec(CattailStemBlock::new);
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final BooleanProperty BOTTOM = Properties.BOTTOM;

    public CattailStemBlock(AbstractBlock.Settings settings) {
        super(settings, Direction.UP, VoxelShapes.fullCube(), true);
        setDefaultState(getDefaultState().with(BOTTOM, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WATERLOGGED, BOTTOM);
    }

    @Override
    protected MapCodec<CattailStemBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected AbstractPlantStemBlock getStem() {
        return HollowBlocks.CATTAIL;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState below = ctx.getWorld().getBlockState(ctx.getBlockPos().down());

        return super.getPlacementState(ctx)
                .with(WATERLOGGED, ctx.getWorld().isWater(ctx.getBlockPos()))
                .with(BOTTOM, !below.isOf(this) && !below.isOf(getStem()));
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction != Direction.DOWN) return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos).withIfExists(WATERLOGGED, world.isWater(pos));

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
                .withIfExists(BOTTOM, !neighborState.isOf(this) && !neighborState.isOf(getStem()))
                .withIfExists(WATERLOGGED, world.isWater(pos));
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public boolean canFillWithFluid(@Nullable PlayerEntity player, BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
        return false;
    }
}
