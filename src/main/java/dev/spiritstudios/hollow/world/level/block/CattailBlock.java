package dev.spiritstudios.hollow.world.level.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.tags.FluidTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import org.jetbrains.annotations.Nullable;

public class CattailBlock extends GrowingPlantHeadBlock implements LiquidBlockContainer {
    public static final MapCodec<CattailBlock> CODEC = simpleCodec(CattailBlock::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);

    public CattailBlock(BlockBehaviour.Properties settings) {
        super(settings, Direction.UP, SHAPE, true, 0.14);
        registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    protected int getBlocksToGrowWhenBonemealed(RandomSource random) {
        return 1;
    }

    @Override
    protected boolean canGrowInto(BlockState state) {
        return state.isAir() || state.is(Blocks.WATER);
    }

    @Override
    protected MapCodec<CattailBlock> codec() {
        return CODEC;
    }

    @Override
    protected Block getBodyBlock() {
        return HollowBlocks.CATTAIL_STEM;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        BlockState below = world.getBlockState(pos.below());

        return super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random)
                .trySetValue(WATERLOGGED, world.isWaterAt(pos))
                .trySetValue(CattailStemBlock.BOTTOM, !below.is(this) && !below.is(getBodyBlock()));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
        BlockState below = ctx.getLevel().getBlockState(ctx.getClickedPos().below());

        return (fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8) || below.is(this) ?
                super.getStateForPlacement(ctx)
                        .setValue(WATERLOGGED, fluidState.is(FluidTags.WATER))
                        .trySetValue(CattailStemBlock.BOTTOM, !below.is(this) && !below.is(getBodyBlock()))
                :
                null;
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (state.getValue(AGE) >= 25 || random.nextDouble() >= 0.25) return;
        int outOfWater = 0;
        BlockPos waterPos = pos;
        while (!world.isWaterAt(waterPos)) {
            waterPos = waterPos.below();
            outOfWater++;
            if (outOfWater > 3) break;
        }

        if (outOfWater > 3) return;

        BlockPos blockPos = pos.relative(this.growthDirection);
        if (this.canGrowInto(world.getBlockState(blockPos)))
            world.setBlockAndUpdate(
                    blockPos,
                    this.getGrowIntoState(state, world.getRandom())
                            .setValue(WATERLOGGED, world.isWaterAt(blockPos))
            );
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos blockPos = pos.relative(this.growthDirection);
        int age = Math.min(state.getValue(AGE) + 1, 25);
        int length = this.getBlocksToGrowWhenBonemealed(random);

        for (int i = 0; i < length && this.canGrowInto(world.getBlockState(blockPos)); i++) {
            world.setBlockAndUpdate(
                    blockPos,
                    state.setValue(AGE, age).setValue(WATERLOGGED, world.isWaterAt(blockPos))
            );
            blockPos = blockPos.relative(this.growthDirection);
            age = Math.min(age + 1, 25);
        }
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
