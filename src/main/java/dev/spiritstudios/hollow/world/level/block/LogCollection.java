package dev.spiritstudios.hollow.world.level.block;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.apache.commons.lang3.function.TriFunction;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public record LogCollection<T>(T oak, T spruce, T birch, T jungle, T acacia, T darkOak, T mangrove, T cherry, T paleOak,
                               T crimson, T warped) {
	public static final LogCollection<Boolean> IS_NETHER = new LogCollection<>(
		false,
		false,
		false,
		false,
		false,
		false,
		false,
		false,
		false,
		true,
		true
	);

	public static final LogCollection<Block> LOGS = new LogCollection<>(
		Blocks.OAK_LOG,
		Blocks.SPRUCE_LOG,
		Blocks.BIRCH_LOG,
		Blocks.JUNGLE_LOG,
		Blocks.ACACIA_LOG,
		Blocks.DARK_OAK_LOG,
		Blocks.MANGROVE_LOG,
		Blocks.CHERRY_LOG,
		Blocks.PALE_OAK_LOG,
		Blocks.CRIMSON_STEM,
		Blocks.WARPED_STEM
	);

	public static final LogCollection<Block> STRIPPED_LOGS = new LogCollection<>(
		Blocks.STRIPPED_OAK_LOG,
		Blocks.STRIPPED_SPRUCE_LOG,
		Blocks.STRIPPED_BIRCH_LOG,
		Blocks.STRIPPED_JUNGLE_LOG,
		Blocks.STRIPPED_ACACIA_LOG,
		Blocks.STRIPPED_DARK_OAK_LOG,
		Blocks.STRIPPED_MANGROVE_LOG,
		Blocks.STRIPPED_CHERRY_LOG,
		Blocks.STRIPPED_PALE_OAK_LOG,
		Blocks.STRIPPED_CRIMSON_STEM,
		Blocks.STRIPPED_WARPED_STEM
	);

	public static <B extends Block, Base, Id> LogCollection<Block> registerBlocks(
		LogCollection<Id> ids,
		LogCollection<Base> bases,
		TriFunction<Id, Function<BlockBehaviour.Properties, Block>, BlockBehaviour.Properties, Block> register,
		BiFunction<Base, BlockBehaviour.Properties, B> blockFactory,
		Function<Base, BlockBehaviour.Properties> propertiesSupplier
	) {
		return zipMap(
			bases, ids,
			(base, id) ->
				register.apply(id, p -> blockFactory.apply(base, p), propertiesSupplier.apply(base))
		);
	}

	public static <Id> LogCollection<Item> registerBlockItems(
		LogCollection<Id> ids,
		LogCollection<Block> blocks,
		BiFunction<Id, Block, Item> itemFactory
	) {
		return zipMap(blocks, ids, (block, id) -> itemFactory.apply(id, block));
	}

	public static <T, U, R> LogCollection<R> zipMap(LogCollection<T> first, LogCollection<U> second, BiFunction<T, U, R> operation) {
		return new LogCollection<>(
			operation.apply(first.oak, second.oak),
			operation.apply(first.spruce, second.spruce),
			operation.apply(first.birch, second.birch),
			operation.apply(first.jungle, second.jungle),
			operation.apply(first.acacia, second.acacia),
			operation.apply(first.darkOak, second.darkOak),
			operation.apply(first.mangrove, second.mangrove),
			operation.apply(first.cherry, second.cherry),
			operation.apply(first.paleOak, second.paleOak),
			operation.apply(first.crimson, second.crimson),
			operation.apply(first.warped, second.warped)
		);
	}

	public static <T, U> void zipApply(LogCollection<T> first, LogCollection<U> second, BiConsumer<T, U> operation) {
		operation.accept(first.oak, second.oak);
		operation.accept(first.spruce, second.spruce);
		operation.accept(first.birch, second.birch);
		operation.accept(first.jungle, second.jungle);
		operation.accept(first.acacia, second.acacia);
		operation.accept(first.darkOak, second.darkOak);
		operation.accept(first.mangrove, second.mangrove);
		operation.accept(first.cherry, second.cherry);
		operation.accept(first.paleOak, second.paleOak);
		operation.accept(first.crimson, second.crimson);
		operation.accept(first.warped, second.warped);
	}

	public <U> LogCollection<U> map(final Function<T, U> mapper) {
		return new LogCollection<>(
			mapper.apply(oak),
			mapper.apply(spruce),
			mapper.apply(birch),
			mapper.apply(jungle),
			mapper.apply(acacia),
			mapper.apply(darkOak),
			mapper.apply(mangrove),
			mapper.apply(cherry),
			mapper.apply(paleOak),
			mapper.apply(crimson),
			mapper.apply(warped)
		);
	}

	public void forEach(Consumer<T> consumer) {
		consumer.accept(oak);
		consumer.accept(spruce);
		consumer.accept(birch);
		consumer.accept(jungle);
		consumer.accept(acacia);
		consumer.accept(darkOak);
		consumer.accept(mangrove);
		consumer.accept(cherry);
		consumer.accept(paleOak);
		consumer.accept(crimson);
		consumer.accept(warped);
	}

	public List<T> toList() {
		ImmutableList.Builder<T> list = ImmutableList.builderWithExpectedSize(11);
		this.forEach(list::add);
		return list.build();
	}
}
