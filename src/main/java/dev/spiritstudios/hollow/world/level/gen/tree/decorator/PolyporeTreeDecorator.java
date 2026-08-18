package dev.spiritstudios.hollow.world.level.gen.tree.decorator;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.List;

public class PolyporeTreeDecorator extends TreeDecorator {
    public static final MapCodec<PolyporeTreeDecorator> CODEC = BlockStateProvider.CODEC
            .fieldOf("provider")
            .xmap(PolyporeTreeDecorator::new, decorator -> decorator.stateProvider);

    public final BlockStateProvider stateProvider;

    public PolyporeTreeDecorator(BlockStateProvider stateProvider) {
        this.stateProvider = stateProvider;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return HollowTreeDecoratorTypes.POLYPORE;
    }

    @Override
    public void place(Context context) {
        RandomSource random = context.random();
        List<BlockPos> logs = context.logs();
        
        for (BlockPos pos : logs) {
            Direction direction = Direction.from2DDataValue(random.nextInt(4));
            BlockPos polyporePos = pos.relative(direction);
            
            if (!context.isAir(polyporePos)) continue;
            
            BlockState state = stateProvider.getState(context.level(), random, polyporePos);

            state = state.trySetValue(BlockStateProperties.HORIZONTAL_FACING, direction);

            context.setBlock(polyporePos, state);
        }
    }
}
