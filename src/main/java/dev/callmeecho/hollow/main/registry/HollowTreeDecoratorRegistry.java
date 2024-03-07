package dev.callmeecho.hollow.main.registry;

import dev.callmeecho.cabinetapi.registry.Registrar;
import dev.callmeecho.hollow.main.worldgen.BranchTreeDecorator;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

@SuppressWarnings("unused")
public class HollowTreeDecoratorRegistry implements Registrar<TreeDecoratorType<?>> {
    @Override
    public Registry<TreeDecoratorType<?>> getRegistry() {
        return Registries.TREE_DECORATOR_TYPE;
    }
    
    public static final TreeDecoratorType<BranchTreeDecorator> BRANCH_TREE_DECORATOR = new TreeDecoratorType<>(BranchTreeDecorator.CODEC);
}
