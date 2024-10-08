package dev.spiritstudios.hollow.registry;

import dev.spiritstudios.hollow.worldgen.BigBranchTreeDecorator;
import dev.spiritstudios.hollow.worldgen.BranchTreeDecorator;
import dev.spiritstudios.hollow.worldgen.PolyporeTreeDecorator;
import dev.spiritstudios.specter.api.registry.registration.MinecraftRegistrar;
import dev.spiritstudios.specter.api.registry.registration.Registrar;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

@SuppressWarnings("unused")
public class HollowTreeDecoratorRegistrar implements MinecraftRegistrar<TreeDecoratorType<?>> {
    @Override
    public Registry<TreeDecoratorType<?>> getRegistry() {
        return Registries.TREE_DECORATOR_TYPE;
    }
    
    public static final TreeDecoratorType<BranchTreeDecorator> BRANCH_TREE_DECORATOR = new TreeDecoratorType<>(BranchTreeDecorator.CODEC);
    public static final TreeDecoratorType<PolyporeTreeDecorator> POLYPORE_TREE_DECORATOR = new TreeDecoratorType<>(PolyporeTreeDecorator.CODEC);
    public static final TreeDecoratorType<BigBranchTreeDecorator> BIG_BRANCH_TREE_DECORATOR = new TreeDecoratorType<>(BigBranchTreeDecorator.CODEC);

    @Override
    public Class<TreeDecoratorType<?>> getObjectType() {
        return Registrar.fixGenerics(TreeDecoratorType.class);
    }
}
