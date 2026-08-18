package dev.spiritstudios.hollow.world.level.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import org.jetbrains.annotations.Nullable;

public class CattailStemBlock extends GrowingPlantBodyBlock implements LiquidBlockContainer {
    public static final MapCodec<CattailStemBlock> CODEC = simpleCodec(CattailStemBlock::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty BOTTOM = BlockStateProperties.BOTTOM;

    public CattailStemBlock(BlockBehaviour.Properties settings) {
        super(settings, Direction.UP, CattailBlock.SHAPE, true);
        registerDefaultState(defaultBlockState().setValue(BOTTOM, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED, BOTTOM);
    }

    @Override
    protected MapCodec<CattailStemBlock> codec() {
        return CODEC;
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return HollowBlocks.CATTAIL;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState below = ctx.getLevel().getBlockState(ctx.getClickedPos().below());

        return super.getStateForPlacement(ctx)
                .setValue(WATERLOGGED, ctx.getLevel().isWaterAt(ctx.getClickedPos()))
                .setValue(BOTTOM, !below.is(this) && !below.is(getHeadBlock()));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (direction != Direction.DOWN)
            return super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random)
                    .trySetValue(WATERLOGGED, world.isWaterAt(pos));

        return super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random)
                .trySetValue(BOTTOM, !neighborState.is(this) && !neighborState.is(getHeadBlock()))
                .trySetValue(WATERLOGGED, world.isWaterAt(pos));
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean canPlaceLiquid(@Nullable LivingEntity filler, BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidState) {
        return false;
    }
}
