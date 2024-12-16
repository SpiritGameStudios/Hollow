package dev.spiritstudios.hollow.registry;

import dev.spiritstudios.hollow.worldgen.decorator.BigBranchTreeDecorator;
import dev.spiritstudios.hollow.worldgen.decorator.BranchTreeDecorator;
import dev.spiritstudios.hollow.worldgen.decorator.PolyporeTreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

@SuppressWarnings("unused")
public final class HollowTreeDecoratorTypes {
    public static final TreeDecoratorType<BranchTreeDecorator> BRANCH_TREE_DECORATOR = new TreeDecoratorType<>(BranchTreeDecorator.CODEC);
    public static final TreeDecoratorType<PolyporeTreeDecorator> POLYPORE_TREE_DECORATOR = new TreeDecoratorType<>(PolyporeTreeDecorator.CODEC);
    public static final TreeDecoratorType<BigBranchTreeDecorator> BIG_BRANCH_TREE_DECORATOR = new TreeDecoratorType<>(BigBranchTreeDecorator.CODEC);
}
