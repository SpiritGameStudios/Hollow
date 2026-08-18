package dev.spiritstudios.hollow.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class HollowLogBlock extends RotatedPillarBlock implements SimpleWaterloggedBlock {
    public static final Map<Direction.Axis, VoxelShape> SHAPES = Shapes.rotateAllAxis(
            Shapes.or(
                    box(0, 14, 0, 16, 16, 16),
                    box(2, 0, 0, 14, 2, 16),
                    box(0, 0, 0, 2, 14, 16),
                    box(14, 0, 0, 16, 14, 16)
            )
    );

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<Layer> LAYER = EnumProperty.create("layer", Layer.class);

    public final Block log;
    public final boolean isStripped;

    public HollowLogBlock(Properties settings, Block log, boolean isStripped) {
        super(settings);
        this.log = log;
        this.isStripped = isStripped;
        this.registerDefaultState(
                this.defaultBlockState()
                        .setValue(AXIS, Direction.Axis.Y)
                        .setValue(WATERLOGGED, false)
                        .setValue(LAYER, Layer.NONE)
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
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return directionToNeighbour == Direction.UP ?
                state.setValue(LAYER, Layer.get(neighbourState)) :
                super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(AXIS));
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
            if (aboveState.is(Blocks.MOSS_BLOCK) || aboveState.is(Blocks.MOSS_CARPET)) {
                return MOSS;
            }

            if (aboveState.is(Blocks.PALE_MOSS_BLOCK) || aboveState.is(Blocks.PALE_MOSS_CARPET)) {
                return PALE_MOSS;
            }

            if (aboveState.is(BlockTags.SNOW)) {
                return SNOW;
            }

            return NONE;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
