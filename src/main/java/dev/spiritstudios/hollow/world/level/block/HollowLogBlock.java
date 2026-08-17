package dev.spiritstudios.hollow.world.level.block;

import dev.spiritstudios.hollow.data.LogTypeData;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.util.StringRepresentable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;

import java.util.function.Function;

public class HollowLogBlock extends RotatedPillarBlock implements SimpleWaterloggedBlock {
    public static final VoxelShape SHAPE_X = Shapes.or(
            box(0, 14, 0, 16, 16, 16),
            box(0, 0, 2, 16, 2, 14),
            box(0, 0, 0, 16, 14, 2),
            box(0, 0, 14, 16, 14, 16)
    );
    
    public static final VoxelShape SHAPE_Y = Shapes.or(
            box(0, 0, 0, 2, 16, 16),
            box(14, 0, 2, 16, 16, 14),
            box(2, 0, 0, 16, 16, 2),
            box(2, 0, 14, 16, 16, 16)
    );
    
    public static final VoxelShape SHAPE_Z = Shapes.or(
            box(0, 14, 0, 16, 16, 16),
            box(2, 0, 0, 14, 2, 16),
            box(0, 0, 0, 2, 14, 16),
            box(14, 0, 0, 16, 14, 16)
    );
    
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<Layer> LAYER = EnumProperty.create("layer", Layer.class);
    
    public final LogTypeData typeData;
    
    public HollowLogBlock(Properties settings, LogTypeData typeData) {
        super(settings);
        this.typeData = typeData;
        registerDefaultState(defaultBlockState()
                .setValue(AXIS, Direction.Axis.Y)
                .setValue(WATERLOGGED, false)
                .setValue(LAYER, Layer.NONE));
    }

    public static Function<BlockBehaviour.Properties, Block> of(Block block) {
        return settings -> new HollowLogBlock(
                settings,
                LogTypeData.byId(BuiltInRegistries.BLOCK.getKey(block))
        );
    }

    public static Function<BlockBehaviour.Properties, Block> ofWood(Block block) {
        return settings -> new HollowLogBlock(
                settings,
                LogTypeData.byIdWood(BuiltInRegistries.BLOCK.getKey(block))
        );
    }

    public static Function<BlockBehaviour.Properties, Block> ofStripped(Block block) {
        return settings -> new HollowLogBlock(
                settings,
                LogTypeData.byIdStripped(BuiltInRegistries.BLOCK.getKey(block))
        );
    }

    public static Function<BlockBehaviour.Properties, Block> ofStrippedWood(Block block) {
        return settings -> new HollowLogBlock(
                settings,
                LogTypeData.byIdStrippedWood(BuiltInRegistries.BLOCK.getKey(block))
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.AXIS, WATERLOGGED, LAYER);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return !state.getValue(WATERLOGGED) && state.getValue(AXIS) == Direction.Axis.Y;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState above = ctx.getLevel().getBlockState(ctx.getClickedPos().above());
        return this.defaultBlockState()
                .setValue(BlockStateProperties.AXIS, ctx.getClickedFace().getAxis())
                .setValue(WATERLOGGED, ctx.getLevel().getFluidState(ctx.getClickedPos()).is(Fluids.WATER))
                .setValue(LAYER, Layer.get(above));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED))
            tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));

        if (direction == Direction.UP) return state.setValue(LAYER, Layer.get(neighborState));

        return super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(AXIS)) {
            case X -> SHAPE_X;
            case Y -> SHAPE_Y;
            default -> SHAPE_Z;
        };
    }

    public enum Layer implements StringRepresentable {
        NONE("none"),
        MOSS("moss"),
        PALE_MOSS("pale_moss"),
        SNOW("snow");

        private final String name;

        Layer(String name) {
            this.name = name;
        }

        public static Layer get(BlockState aboveState) {
            if (aboveState.is(Blocks.MOSS_BLOCK) || aboveState.is(Blocks.MOSS_CARPET))
                return MOSS;

            if (aboveState.is(Blocks.PALE_MOSS_BLOCK) || aboveState.is(Blocks.PALE_MOSS_CARPET))
                return PALE_MOSS;

            if (aboveState.is(Blocks.SNOW_BLOCK) || aboveState.is(Blocks.SNOW))
                return SNOW;

            return NONE;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
