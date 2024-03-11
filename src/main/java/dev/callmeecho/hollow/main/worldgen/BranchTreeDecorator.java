package dev.callmeecho.hollow.main.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.callmeecho.hollow.main.registry.HollowTreeDecoratorRegistry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

public class BranchTreeDecorator extends TreeDecorator {
    public static final Codec<BranchTreeDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockStateProvider.TYPE_CODEC.fieldOf("state_provider").forGetter(decorator -> decorator.stateProvider)
    ).apply(instance, BranchTreeDecorator::new));
    
    public BranchTreeDecorator(BlockStateProvider stateProvider) {
        this.stateProvider = stateProvider;
    }
    
    public final BlockStateProvider stateProvider;
    
    @Override
    protected TreeDecoratorType<?> getType() {
        return HollowTreeDecoratorRegistry.BRANCH_TREE_DECORATOR;
    }

    @Override
    public void generate(Generator generator) {
        Random random = generator.getRandom();
        ObjectArrayList<BlockPos> logs = generator.getLogPositions();

        if (random.nextBetween(0, 4) == 0) return;
        

        boolean hasBranch = false;
        for (BlockPos pos : logs) {
            if (hasBranch) break;
            if (pos.getY() < (logs.get(0).getY() + logs.get(logs.size() - 1).getY()) / 2) continue;
            
            Direction direction = Direction.fromHorizontal(random.nextInt(4));
            BlockPos branch = pos.offset(direction);

            if (!generator.isAir(branch)) continue;
            
            BlockState state = stateProvider.get(random, branch);
            if (state.contains(Properties.AXIS)) state = state.with(Properties.AXIS, direction.getAxis());
            generator.replace(branch, state);
            hasBranch = true;
            
            if (random.nextBetween(0, 32) != 0) continue;
            
            BlockPos beeHive = branch.down();
            if (!generator.isAir(beeHive)) continue;
            
            Direction hiveDirection = direction;
            if (random.nextBoolean()) hiveDirection = random.nextBoolean() ? direction.rotateClockwise(Direction.Axis.Y) : direction.rotateCounterclockwise(Direction.Axis.Y);
            
            generator.replace(beeHive, Blocks.BEE_NEST.getDefaultState().with(BeehiveBlock.FACING, hiveDirection));
            generator.getWorld().getBlockEntity(beeHive, BlockEntityType.BEEHIVE).ifPresent(beehive -> {
                int beeCount = 2 + random.nextInt(2);
                
                for (int i = 0; i < beeCount; i++) {
                    NbtCompound nbtCompound = new NbtCompound();
                    nbtCompound.putString("id", "minecraft:bee");
                    beehive.addBee(nbtCompound, random.nextInt(599), false);
                }
            });
        }
    }
}
