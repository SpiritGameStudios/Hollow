package dev.spiritstudios.hollow.world.level.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.tags.HollowBlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;

import java.util.Map;

public class PolyporeBlock extends VegetationBlock implements BonemealableBlock {
    public static final IntegerProperty POLYPORE_AMOUNT = IntegerProperty.create("amount", 1, 3);

    public static final Map<Direction, VoxelShape> SHAPES_BY_DIRECTION = Shapes.rotateHorizontal(box(1, 1, 8, 15, 15, 16));
    public static final MapCodec<PolyporeBlock> CODEC = simpleCodec(PolyporeBlock::new);

    public PolyporeBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                .setValue(POLYPORE_AMOUNT, 1));
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING, POLYPORE_AMOUNT);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return !context.isSecondaryUseActive() && context.getItemInHand().is(this.asItem()) && state.getValue(POLYPORE_AMOUNT) < 3 || super.canBeReplaced(state, context);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockState = ctx.getLevel().getBlockState(ctx.getClickedPos());
        if (blockState.is(this)) return blockState.cycle(POLYPORE_AMOUNT);

        for (Direction direction : ctx.getNearestLookingDirections()) {
            if (direction.getAxis().isHorizontal()) {
                BlockState blockState2 = defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction.getOpposite());
                if (blockState2.canSurvive(ctx.getLevel(), ctx.getClickedPos())) return blockState2;
            }
        }

        return null;
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return true;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        if (state.getValue(POLYPORE_AMOUNT) < 3)
            world.setBlock(pos, state.cycle(POLYPORE_AMOUNT), Block.UPDATE_CLIENTS);
        else popResource(world, pos, new ItemStack(this));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES_BY_DIRECTION.get(state.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        BlockPos blockPos = pos.relative(direction.getOpposite());
        BlockState blockState = world.getBlockState(blockPos);

        return blockState.isFaceSturdy(world, blockPos, direction) &&
                blockState.is(HollowBlockTags.POLYPORE_PLACEABLE_ON);
    }

    @Override
    protected MapCodec<? extends VegetationBlock> codec() {
        return CODEC;
    }
}
