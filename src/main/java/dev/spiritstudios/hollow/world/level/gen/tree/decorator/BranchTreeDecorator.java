package dev.spiritstudios.hollow.world.level.gen.tree.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.HashSet;
import java.util.Set;

public class BranchTreeDecorator extends TreeDecorator {
    public static final MapCodec<BranchTreeDecorator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockStateProvider.CODEC.fieldOf("provider").forGetter(decorator -> decorator.stateProvider),
            Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(decorator -> decorator.probability),
            Codec.INT.fieldOf("max_amount").forGetter(decorator -> decorator.maxAmount)
    ).apply(instance, BranchTreeDecorator::new));

    public BranchTreeDecorator(BlockStateProvider stateProvider, float probability, int maxAmount) {
        this.stateProvider = stateProvider;
        this.probability = probability;
        this.maxAmount = maxAmount;
    }

    private final BlockStateProvider stateProvider;
    private final float probability;
    private final int maxAmount;

    @Override
    protected TreeDecoratorType<?> type() {
        return HollowTreeDecoratorTypes.BRANCH;
    }

    @Override
    public void place(Context context) {
        RandomSource random = context.random();
        ObjectArrayList<BlockPos> logs = context.logs();

        if (random.nextFloat() > probability) return;

        int amount = 0;

        Set<Integer> branches = new HashSet<>();
        for (BlockPos pos : logs) {
            if (amount >= maxAmount) break;

            if (pos.getY() < (logs.getFirst().getY() + logs.getLast().getY()) / 2) continue;

            Direction direction = Direction.from2DDataValue(random.nextInt(4));
            BlockPos branch = pos.relative(direction);

            if (branches.contains(pos.getY() + 1) || branches.contains(pos.getY() - 1)) continue;

            if (!context.isAir(branch)) continue;

            BlockState state = stateProvider.getState(context.level(), random, branch);
            state = state.trySetValue(BlockStateProperties.AXIS, direction.getAxis());
            context.setBlock(branch, state);
            branches.add(branch.getY());

            amount++;

            if (random.nextIntBetweenInclusive(0, 32) == 0) generateBeehive(context, branch, direction, random);
        }
    }

    private static void generateBeehive(Context generator, BlockPos branch, Direction direction, RandomSource random) {
        BlockPos beeHive = branch.below();
        if (!generator.isAir(beeHive)) return;

        Direction hiveDirection = direction;
        if (random.nextBoolean())
            hiveDirection = random.nextBoolean() ? direction.getClockWise(Direction.Axis.Y) : direction.getCounterClockWise(Direction.Axis.Y);

        generator.setBlock(beeHive, Blocks.BEE_NEST.defaultBlockState().setValue(BeehiveBlock.FACING, hiveDirection));
        generator.level().getBlockEntity(beeHive, BlockEntityTypes.BEEHIVE).ifPresent(beehive -> {
            int beeCount = 2 + random.nextInt(2);

            for (int i = 0; i < beeCount; i++) {
                BeehiveBlockEntity.Occupant beeData = BeehiveBlockEntity.Occupant.create(random.nextInt(599));
                beehive.storeBee(beeData);
            }
        });
    }
}
