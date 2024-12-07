package dev.spiritstudios.hollow.worldgen.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.hollow.registry.HollowTreeDecoratorTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

import java.util.HashSet;
import java.util.Set;

public class BranchTreeDecorator extends TreeDecorator {
    public static final MapCodec<BranchTreeDecorator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockStateProvider.TYPE_CODEC.fieldOf("provider").forGetter(decorator -> decorator.stateProvider),
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
    protected TreeDecoratorType<?> getType() {
        return HollowTreeDecoratorTypes.BRANCH_TREE_DECORATOR;
    }

    @Override
    public void generate(Generator generator) {
        Random random = generator.getRandom();
        ObjectArrayList<BlockPos> logs = generator.getLogPositions();

        if (random.nextFloat() > probability) return;

        int amount = 0;

        Set<Integer> branches = new HashSet<>();
        for (BlockPos pos : logs) {
            if (amount >= maxAmount) break;

            if (pos.getY() < (logs.getFirst().getY() + logs.getLast().getY()) / 2) continue;

            Direction direction = Direction.fromHorizontal(random.nextInt(4));
            BlockPos branch = pos.offset(direction);

            if (branches.contains(pos.getY() + 1) || branches.contains(pos.getY() - 1)) continue;

            if (!generator.isAir(branch)) continue;

            BlockState state = stateProvider.get(random, branch);
            state = state.withIfExists(Properties.AXIS, direction.getAxis());
            generator.replace(branch, state);
            branches.add(branch.getY());

            amount++;

            if (random.nextBetween(0, 32) == 0) generateBeehive(generator, branch, direction, random);
        }
    }

    private static void generateBeehive(Generator generator, BlockPos branch, Direction direction, Random random) {
        BlockPos beeHive = branch.down();
        if (!generator.isAir(beeHive)) return;

        Direction hiveDirection = direction;
        if (random.nextBoolean())
            hiveDirection = random.nextBoolean() ? direction.rotateClockwise(Direction.Axis.Y) : direction.rotateCounterclockwise(Direction.Axis.Y);

        generator.replace(beeHive, Blocks.BEE_NEST.getDefaultState().with(BeehiveBlock.FACING, hiveDirection));
        generator.getWorld().getBlockEntity(beeHive, BlockEntityType.BEEHIVE).ifPresent(beehive -> {
            int beeCount = 2 + random.nextInt(2);

            for (int i = 0; i < beeCount; i++) {
                BeehiveBlockEntity.BeeData beeData = BeehiveBlockEntity.BeeData.create(random.nextInt(599));
                beehive.addBee(beeData);
            }
        });
    }
}
