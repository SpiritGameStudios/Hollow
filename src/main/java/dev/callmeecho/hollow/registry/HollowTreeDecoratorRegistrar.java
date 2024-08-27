package dev.callmeecho.hollow.registry;

import dev.callmeecho.cabinetapi.registry.Registrar;
import dev.callmeecho.hollow.worldgen.BigBranchTreeDecorator;
import dev.callmeecho.hollow.worldgen.BranchTreeDecorator;
import dev.callmeecho.hollow.worldgen.PolyporeTreeDecorator;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

@SuppressWarnings("unused")
public class HollowTreeDecoratorRegistrar implements Registrar<TreeDecoratorType<?>> {
    @Override
    public Registry<TreeDecoratorType<?>> getRegistry() {
        return Registries.TREE_DECORATOR_TYPE;
    }
    
    public static final TreeDecoratorType<BranchTreeDecorator> BRANCH_TREE_DECORATOR = new TreeDecoratorType<>(BranchTreeDecorator.CODEC);
    public static final TreeDecoratorType<PolyporeTreeDecorator> POLYPORE_TREE_DECORATOR = new TreeDecoratorType<>(PolyporeTreeDecorator.CODEC);
    public static final TreeDecoratorType<BigBranchTreeDecorator> BIG_BRANCH_TREE_DECORATOR = new TreeDecoratorType<>(BigBranchTreeDecorator.CODEC);
}