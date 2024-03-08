package dev.callmeecho.hollow.main.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.callmeecho.hollow.main.block.PolyporeBlock;
import dev.callmeecho.hollow.main.registry.HollowTreeDecoratorRegistry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

public class PolyporeTreeDecorator extends TreeDecorator {
    public static final Codec<PolyporeTreeDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockStateProvider.TYPE_CODEC.fieldOf("state_provider").forGetter(decorator -> decorator.stateProvider)
    ).apply(instance, PolyporeTreeDecorator::new));

    public final BlockStateProvider stateProvider;

    public PolyporeTreeDecorator(BlockStateProvider stateProvider) {
        this.stateProvider = stateProvider;
    }


    @Override
    protected TreeDecoratorType<?> getType() {
        return HollowTreeDecoratorRegistry.POLYPORE_TREE_DECORATOR;
    }

    @Override
    public void generate(Generator generator) {
        Random random = generator.getRandom();
        ObjectArrayList<BlockPos> logs = generator.getLogPositions();
        
        for (BlockPos pos : logs) {
            Direction direction = Direction.fromHorizontal(random.nextInt(4));
            BlockPos polyporePos = pos.offset(direction);
            
            if (!generator.isAir(polyporePos)) continue;
            
            BlockState state = stateProvider.get(random, polyporePos);
            if (state.contains(Properties.HORIZONTAL_FACING)) state = state.with(Properties.HORIZONTAL_FACING, direction);
            if (state.contains(PolyporeBlock.POLYPORE_AMOUNT)) state = state.with(PolyporeBlock.POLYPORE_AMOUNT, random.nextBetween(1, 3));
            
            generator.replace(polyporePos, state);
        }
    }
}
