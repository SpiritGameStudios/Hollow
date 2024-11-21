package dev.spiritstudios.hollow.worldgen.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.hollow.registry.HollowTreeDecoratorRegistrar;
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

public class BranchTreeDecorator extends TreeDecorator {
    public static final MapCodec<BranchTreeDecorator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockStateProvider.TYPE_CODEC.fieldOf("provider").forGetter(decorator -> decorator.stateProvider),
            Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(decorator -> decorator.probability)
    ).apply(instance, BranchTreeDecorator::new));
    
    public BranchTreeDecorator(BlockStateProvider stateProvider, float probability) {
        this.stateProvider = stateProvider;
        this.probability = probability;
    }
    
    public final BlockStateProvider stateProvider;
    public final float probability;
    
    @Override
    protected TreeDecoratorType<?> getType() {
        return HollowTreeDecoratorRegistrar.BRANCH_TREE_DECORATOR;
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

            if (random.nextBetween(0, 32) != 0) break;
            
            BlockPos beeHive = branch.down();
            if (!generator.isAir(beeHive)) continue;
            
            Direction hiveDirection = direction;
            if (random.nextBoolean()) hiveDirection = random.nextBoolean() ? direction.rotateClockwise(Direction.Axis.Y) : direction.rotateCounterclockwise(Direction.Axis.Y);
            
            generator.replace(beeHive, Blocks.BEE_NEST.getDefaultState().with(BeehiveBlock.FACING, hiveDirection));
            generator.getWorld().getBlockEntity(beeHive, BlockEntityType.BEEHIVE).ifPresent(beehive -> {
                int beeCount = 2 + random.nextInt(2);
                
                for (int i = 0; i < beeCount; i++) {
                    BeehiveBlockEntity.BeeData beeData = BeehiveBlockEntity.BeeData.create(random.nextInt(599));
                    beehive.addBee(beeData);
                }
            });

            break;
        }
    }
}
