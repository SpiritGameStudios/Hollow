package dev.spiritstudios.hollow.world.level.block;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.apache.commons.lang3.function.TriFunction;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public record HollowLogCollection<T>(
        T hollowLog,
        T strippedHollowLog
) {
    public static HollowLogCollection<String> fromName(String baseName, String logSuffix) {
        return new HollowLogCollection<>(
                "hollow_" + baseName + logSuffix,
                "stripped_hollow_" + baseName + logSuffix
        );
    }

    public static HollowLogCollection<String> log(String baseName) {
        return fromName(baseName, "_log");
    }

    public static HollowLogCollection<String> stem(String baseName) {
        return fromName(baseName, "_stem");
    }

    public static <Id> HollowLogCollection<Block> registerBlocks(
            HollowLogCollection<Id> ids,
            TriFunction<Id, Function<BlockBehaviour.Properties, Block>, BlockBehaviour.Properties, Block> register,
            Block log,
            Block strippedLog
    ) {
        return new HollowLogCollection<>(
                register.apply(
                        ids.hollowLog,
                        properties -> new HollowLogBlock(properties, log, false),
                        BlockBehaviour.Properties.ofFullCopy(log)
                ),
                register.apply(
                        ids.strippedHollowLog,
                        properties -> new HollowLogBlock(properties, strippedLog, true),
                        BlockBehaviour.Properties.ofFullCopy(strippedLog)
                )
        );
    }

    public static <Id> HollowLogCollection<Item> registerBlockItems(
            HollowLogCollection<Id> ids,
            HollowLogCollection<Block> blocks,
            BiFunction<Id, Block, Item> register
    ) {
        return new HollowLogCollection<>(
                register.apply(ids.hollowLog, blocks.hollowLog),
                register.apply(ids.strippedHollowLog, blocks.strippedHollowLog)
        );
    }

    public List<T> toList() {
        return List.of(this.hollowLog, this.strippedHollowLog);
    }

    public void forEach(Consumer<T> consumer) {
        consumer.accept(hollowLog);
        consumer.accept(strippedHollowLog);
    }

    public <U> HollowLogCollection<U> map(Function<T, U> mapper) {
        return new HollowLogCollection<>(
                mapper.apply(this.hollowLog),
                mapper.apply(this.strippedHollowLog)
        );
    }

    public static <T, U, R> HollowLogCollection<R> zipMap(HollowLogCollection<T> first, HollowLogCollection<U> second, BiFunction<T, U, R> operation) {
        return new HollowLogCollection<>(
                operation.apply(first.hollowLog, second.hollowLog),
                operation.apply(first.strippedHollowLog, second.strippedHollowLog)
        );
    }

    public static <T, U> void zipApply(HollowLogCollection<T> first, HollowLogCollection<U> second, BiConsumer<T, U> operation) {
        operation.accept(first.hollowLog, second.hollowLog);
        operation.accept(first.strippedHollowLog, second.strippedHollowLog);
    }
}
