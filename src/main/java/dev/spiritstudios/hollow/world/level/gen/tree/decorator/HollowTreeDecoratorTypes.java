package dev.spiritstudios.hollow.world.level.gen.tree.decorator;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.Hollow;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public final class HollowTreeDecoratorTypes {
    public static final TreeDecoratorType<BranchTreeDecorator> BRANCH = register("branch", BranchTreeDecorator.CODEC);
    public static final TreeDecoratorType<BigBranchTreeDecorator> BIG_BRANCH = register("big_branch", BigBranchTreeDecorator.CODEC);
    public static final TreeDecoratorType<PolyporeTreeDecorator> POLYPORE = register("polypore", PolyporeTreeDecorator.CODEC);

    private static <P extends TreeDecorator> TreeDecoratorType<P> register(String name, MapCodec<P> codec) {
        return Registry.register(BuiltInRegistries.TREE_DECORATOR_TYPE, Hollow.id(name), new TreeDecoratorType<>(codec));
    }

    public static void init() {
        // NO-OP
    }
}
