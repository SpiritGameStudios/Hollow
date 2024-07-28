package dev.callmeecho.hollow.worldgen;

import com.mojang.serialization.MapCodec;
import dev.callmeecho.hollow.registry.HollowTreeDecoratorRegistrar;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

public class BigBranchTreeDecorator extends TreeDecorator {
    public static final MapCodec<BigBranchTreeDecorator> CODEC = BlockStateProvider.TYPE_CODEC
            .fieldOf("provider")
            .xmap(BigBranchTreeDecorator::new, decorator -> decorator.stateProvider);

    public BigBranchTreeDecorator(BlockStateProvider stateProvider) {
        this.stateProvider = stateProvider;
    }

    public final BlockStateProvider stateProvider;

    @Override
    protected TreeDecoratorType<?> getType() {
        return HollowTreeDecoratorRegistrar.BIG_BRANCH_TREE_DECORATOR;
    }

    @Override
    public void generate(Generator generator) {
        Random random = generator.getRandom();
        ObjectArrayList<BlockPos> logs = generator.getLogPositions();

        if (random.nextBoolean()) return;

        boolean hasBranch = false;
        for (BlockPos pos : logs) {
            if (hasBranch) break;
            if (pos.getY() < (logs.getFirst().getY() + logs.getLast().getY()) / 2) continue;

            Direction direction = Direction.fromHorizontal(random.nextInt(4));
            BlockPos branch = pos.offset(direction);

            if (!generator.isAir(branch)) continue;

            BlockState state = stateProvider.get(random, branch);
            if (state.contains(Properties.AXIS)) state = state.with(Properties.AXIS, direction.getAxis());
            generator.replace(branch, state);
            branch = branch.offset(direction);
            generator.replace(branch, state);

            if (state.contains(Properties.AXIS)) state = state.with(Properties.AXIS, Direction.Axis.Y);

            for (int i = 0; i < 10; i++) {
                BlockPos branch2 = branch.up();
                if (!generator.isAir(branch2)) break;

                generator.replace(branch2, state);
                branch = branch2;
            }

            hasBranch = true;
        }
    }
}
