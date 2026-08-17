package dev.spiritstudios.hollow.world.level.gen.tree.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public class BigBranchTreeDecorator extends TreeDecorator {
    public static final MapCodec<BigBranchTreeDecorator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockStateProvider.CODEC.fieldOf("provider").forGetter(decorator -> decorator.stateProvider),
            Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(decorator -> decorator.probability)
    ).apply(instance, BigBranchTreeDecorator::new));
    public final BlockStateProvider stateProvider;
    public final float probability;
    public BigBranchTreeDecorator(BlockStateProvider stateProvider, float probability) {
        this.stateProvider = stateProvider;
        this.probability = probability;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return HollowTreeDecoratorTypes.BIG_BRANCH;
    }

    @Override
    public void place(Context context) {
        RandomSource random = context.random();
        ObjectArrayList<BlockPos> logs = context.logs();

        if (random.nextFloat() > probability) return;

        for (BlockPos pos : logs) {
            if (pos.getY() < (logs.getFirst().getY() + logs.getLast().getY()) / 2) continue;

            Direction direction = Direction.from2DDataValue(random.nextInt(4));
            BlockPos branch = pos.relative(direction);

            if (!context.isAir(branch)) continue;

            BlockState state = stateProvider.getState(context.level(), random, branch);
            state = state.trySetValue(BlockStateProperties.AXIS, direction.getAxis());

            context.setBlock(branch, state);

            branch = branch.relative(direction);
            context.setBlock(branch, state);

            state = state.trySetValue(BlockStateProperties.AXIS, Direction.Axis.Y);

            for (int i = 0; i < 10; i++) {
                BlockPos upperBranch = branch.above();
                if (!context.isAir(upperBranch)) break;

                context.setBlock(upperBranch, state);
                branch = upperBranch;
            }

            break;
        }
    }
}
