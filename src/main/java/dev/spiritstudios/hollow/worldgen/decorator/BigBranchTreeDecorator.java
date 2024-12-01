package dev.spiritstudios.hollow.worldgen.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.hollow.registry.HollowTreeDecorators;
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
    public static final MapCodec<BigBranchTreeDecorator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockStateProvider.TYPE_CODEC.fieldOf("provider").forGetter(decorator -> decorator.stateProvider),
            Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(decorator -> decorator.probability)
    ).apply(instance, BigBranchTreeDecorator::new));
    public final BlockStateProvider stateProvider;
    public final float probability;
    public BigBranchTreeDecorator(BlockStateProvider stateProvider, float probability) {
        this.stateProvider = stateProvider;
        this.probability = probability;
    }

    @Override
    protected TreeDecoratorType<?> getType() {
        return HollowTreeDecorators.BIG_BRANCH_TREE_DECORATOR;
    }

    @Override
    public void generate(Generator generator) {
        Random random = generator.getRandom();
        ObjectArrayList<BlockPos> logs = generator.getLogPositions();

        if (random.nextFloat() > probability) return;

        for (BlockPos pos : logs) {
            if (pos.getY() < (logs.getFirst().getY() + logs.getLast().getY()) / 2) continue;

            Direction direction = Direction.fromHorizontal(random.nextInt(4));
            BlockPos branch = pos.offset(direction);

            if (!generator.isAir(branch)) continue;

            BlockState state = stateProvider.get(random, branch);
            state = state.withIfExists(Properties.AXIS, direction.getAxis());

            generator.replace(branch, state);

            branch = branch.offset(direction);
            generator.replace(branch, state);

            state = state.withIfExists(Properties.AXIS, Direction.Axis.Y);

            for (int i = 0; i < 10; i++) {
                BlockPos upperBranch = branch.up();
                if (!generator.isAir(upperBranch)) break;

                generator.replace(upperBranch, state);
                branch = upperBranch;
            }

            break;
        }
    }
}
