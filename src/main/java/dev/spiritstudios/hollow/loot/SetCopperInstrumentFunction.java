package dev.spiritstudios.hollow.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.hollow.component.CopperInstrumentComponent;
import dev.spiritstudios.hollow.component.HollowDataComponentTypes;
import dev.spiritstudios.hollow.registry.HollowRegistryKeys;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.function.LootFunctionTypes;
import net.minecraft.loot.function.SetInstrumentLootFunction;

import java.util.List;

public class SetCopperInstrumentFunction extends ConditionalLootFunction {
	public static final MapCodec<SetCopperInstrumentFunction> CODEC = RecordCodecBuilder.mapCodec(
			instance -> addConditionsField(instance)
					.apply(instance, SetCopperInstrumentFunction::new)
	);

	private SetCopperInstrumentFunction(List<LootCondition> conditions) {
		super(conditions);
	}

	public static ConditionalLootFunction.Builder<?> builder() {
		return builder(SetCopperInstrumentFunction::new);
	}

	@Override
	public LootFunctionType<SetInstrumentLootFunction> getType() {
		return LootFunctionTypes.SET_INSTRUMENT;
	}

	@Override
	public ItemStack process(ItemStack stack, LootContext context) {

		context.getWorld().getRegistryManager()
				.getOptional(HollowRegistryKeys.COPPER_INSTRUMENT)
				.flatMap(registry -> registry.getRandom(context.getRandom()))
				.ifPresent(entry ->
						stack.set(HollowDataComponentTypes.COPPER_INSTRUMENT, new CopperInstrumentComponent(entry)));

		return stack;
	}
}
