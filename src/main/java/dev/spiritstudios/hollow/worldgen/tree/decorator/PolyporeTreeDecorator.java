package dev.spiritstudios.hollow.worldgen.tree.decorator;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.block.PolyporeBlock;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

import java.util.List;

public class PolyporeTreeDecorator extends TreeDecorator {
    public static final MapCodec<PolyporeTreeDecorator> CODEC = BlockStateProvider.TYPE_CODEC
            .fieldOf("provider")
            .xmap(PolyporeTreeDecorator::new, decorator -> decorator.stateProvider);

    public final BlockStateProvider stateProvider;

    public PolyporeTreeDecorator(BlockStateProvider stateProvider) {
        this.stateProvider = stateProvider;
    }

    @Override
    protected TreeDecoratorType<?> getType() {
        return HollowTreeDecoratorTypes.POLYPORE_TREE_DECORATOR;
    }

    @Override
    public void generate(Generator generator) {
        Random random = generator.getRandom();
        List<BlockPos> logs = generator.getLogPositions();
        
        for (BlockPos pos : logs) {
            Direction direction = Direction.fromHorizontalQuarterTurns(random.nextInt(4));
            BlockPos polyporePos = pos.offset(direction);
            
            if (!generator.isAir(polyporePos)) continue;
            
            BlockState state = stateProvider.get(random, polyporePos);

            state = state.withIfExists(Properties.HORIZONTAL_FACING, direction);
            state = state.withIfExists(PolyporeBlock.POLYPORE_AMOUNT, random.nextBetween(1, 3));

            generator.replace(polyporePos, state);
        }
    }
}
