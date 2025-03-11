package dev.spiritstudios.hollow.block;

import dev.spiritstudios.hollow.data.LogTypeData;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.Registries;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import java.util.function.Function;

public class HollowLogBlock extends PillarBlock implements Waterloggable {
    public static final VoxelShape SHAPE_X = VoxelShapes.union(
            createCuboidShape(0, 14, 0, 16, 16, 16),
            createCuboidShape(0, 0, 2, 16, 2, 14),
            createCuboidShape(0, 0, 0, 16, 14, 2),
            createCuboidShape(0, 0, 14, 16, 14, 16)
    );
    
    public static final VoxelShape SHAPE_Y = VoxelShapes.union(
            createCuboidShape(0, 0, 0, 2, 16, 16),
            createCuboidShape(14, 0, 2, 16, 16, 14),
            createCuboidShape(2, 0, 0, 16, 16, 2),
            createCuboidShape(2, 0, 14, 16, 16, 16)
    );
    
    public static final VoxelShape SHAPE_Z = VoxelShapes.union(
            createCuboidShape(0, 14, 0, 16, 16, 16),
            createCuboidShape(2, 0, 0, 14, 2, 16),
            createCuboidShape(0, 0, 0, 2, 14, 16),
            createCuboidShape(14, 0, 0, 16, 14, 16)
    );
    
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final EnumProperty<Layer> LAYER = EnumProperty.of("layer", Layer.class);
    
    public final LogTypeData typeData;
    
    public HollowLogBlock(Settings settings, LogTypeData typeData) {
        super(settings);
        this.typeData = typeData;
        setDefaultState(getDefaultState()
                .with(AXIS, Direction.Axis.Y)
                .with(WATERLOGGED, false)
                .with(LAYER, Layer.NONE));
    }

    public static Function<AbstractBlock.Settings, Block> of(Block block) {
        return settings -> new HollowLogBlock(
                settings,
                LogTypeData.byId(Registries.BLOCK.getId(block))
        );
    }

    public static Function<AbstractBlock.Settings, Block> ofStripped(Block block) {
        return settings -> new HollowLogBlock(
                settings,
                LogTypeData.byIdStripped(Registries.BLOCK.getId(block))
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.AXIS, WATERLOGGED, LAYER);
    }

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @Override
    protected boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return !state.get(WATERLOGGED) && state.get(AXIS) == Direction.Axis.Y;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState above = ctx.getWorld().getBlockState(ctx.getBlockPos().up());
        return this.getDefaultState()
                .with(Properties.AXIS, ctx.getSide().getAxis())
                .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).isOf(Fluids.WATER))
                .with(LAYER, Layer.get(above));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED))
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));

        if (direction == Direction.UP) return state.with(LAYER, Layer.get(neighborState));

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(AXIS)) {
            case X -> SHAPE_X;
            case Y -> SHAPE_Y;
            default -> SHAPE_Z;
        };
    }

    public enum Layer implements StringIdentifiable {
        NONE("none"),
        MOSS("moss"),
        SNOW("snow");

        private final String name;

        Layer(String name) {
            this.name = name;
        }

        public static Layer get(BlockState aboveState) {
            if (aboveState.isOf(Blocks.MOSS_BLOCK) || aboveState.isOf(Blocks.MOSS_CARPET))
                return MOSS;

            if (aboveState.isOf(Blocks.SNOW_BLOCK) || aboveState.isOf(Blocks.SNOW))
                return SNOW;

            return NONE;
        }

        @Override
        public String asString() {
            return name;
        }
    }
}
