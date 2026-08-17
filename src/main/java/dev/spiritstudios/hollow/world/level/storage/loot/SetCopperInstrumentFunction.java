package dev.spiritstudios.hollow.world.level.storage.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.hollow.world.item.component.CopperInstrumentComponent;
import dev.spiritstudios.hollow.core.component.HollowDataComponents;
import dev.spiritstudios.hollow.registry.HollowRegistryKeys;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public class SetCopperInstrumentFunction extends LootItemConditionalFunction {
	public static final MapCodec<SetCopperInstrumentFunction> CODEC = RecordCodecBuilder.mapCodec(
			instance -> commonFields(instance)
					.apply(instance, SetCopperInstrumentFunction::new)
	);

	private SetCopperInstrumentFunction(List<LootItemCondition> conditions) {
		super(conditions);
	}

	@Override
	public MapCodec<? extends LootItemConditionalFunction> codec() {
		return HollowLootFunctionTypes.SET_COPPER_INSTRUMENT;
	}

	public static LootItemConditionalFunction.Builder<?> builder() {
		return simpleBuilder(SetCopperInstrumentFunction::new);
	}

	@Override
	public ItemStack run(ItemStack stack, LootContext context) {
		context.getLevel().registryAccess()
				.lookup(HollowRegistryKeys.COPPER_INSTRUMENT)
				.flatMap(registry -> registry.getRandom(context.getRandom()))
				.ifPresent(entry ->
						stack.set(HollowDataComponents.COPPER_INSTRUMENT, new CopperInstrumentComponent(entry)));

		return stack;
	}
}
