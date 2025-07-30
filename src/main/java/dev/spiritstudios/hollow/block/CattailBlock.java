package dev.spiritstudios.hollow.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidFillable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

public class CattailBlock extends AbstractPlantStemBlock implements FluidFillable {
    public static final MapCodec<CattailBlock> CODEC = createCodec(CattailBlock::new);
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);

    public CattailBlock(AbstractBlock.Settings settings) {
        super(settings, Direction.UP, SHAPE, true, 0.14);
        setDefaultState(getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    protected int getGrowthLength(Random random) {
        return 1;
    }

    @Override
    protected boolean chooseStemState(BlockState state) {
        return state.isAir() || state.isOf(Blocks.WATER);
    }

    @Override
    protected MapCodec<CattailBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected Block getPlant() {
        return HollowBlocks.CATTAIL_STEM;
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        BlockState below = world.getBlockState(pos.down());

        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random)
                .withIfExists(WATERLOGGED, world.isWater(pos))
                .withIfExists(CattailStemBlock.BOTTOM, !below.isOf(this) && !below.isOf(getPlant()));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        BlockState below = ctx.getWorld().getBlockState(ctx.getBlockPos().down());

        return (fluidState.isIn(FluidTags.WATER) && fluidState.getLevel() == 8) || below.isOf(this) ?
                super.getPlacementState(ctx)
                        .with(WATERLOGGED, fluidState.isIn(FluidTags.WATER))
                        .withIfExists(CattailStemBlock.BOTTOM, !below.isOf(this) && !below.isOf(getPlant()))
                :
                null;
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(AGE) >= 25 || random.nextDouble() >= 0.25) return;
        int outOfWater = 0;
        BlockPos waterPos = pos;
        while (!world.isWater(waterPos)) {
            waterPos = waterPos.down();
            outOfWater++;
            if (outOfWater > 3) break;
        }

        if (outOfWater > 3) return;

        BlockPos blockPos = pos.offset(this.growthDirection);
        if (this.chooseStemState(world.getBlockState(blockPos)))
            world.setBlockState(
                    blockPos,
                    this.age(state, world.random)
                            .with(WATERLOGGED, world.isWater(blockPos))
            );
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        BlockPos blockPos = pos.offset(this.growthDirection);
        int age = Math.min(state.get(AGE) + 1, 25);
        int length = this.getGrowthLength(random);

        for (int i = 0; i < length && this.chooseStemState(world.getBlockState(blockPos)); i++) {
            world.setBlockState(
                    blockPos,
                    state.with(AGE, age).with(WATERLOGGED, world.isWater(blockPos))
            );
            blockPos = blockPos.offset(this.growthDirection);
            age = Math.min(age + 1, 25);
        }
    }

    @Override
    public boolean canFillWithFluid(@Nullable LivingEntity filler, BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
        return false;
    }
}
