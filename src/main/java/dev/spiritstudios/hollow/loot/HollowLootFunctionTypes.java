package dev.spiritstudios.hollow.loot;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.Hollow;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class HollowLootFunctionTypes {
	public static final LootFunctionType<SetCopperInstrumentFunction> SET_COPPER_INSTRUMENT = register("set_copper_instrument", SetCopperInstrumentFunction.CODEC);

	private static <T extends LootFunction> LootFunctionType<T> register(String id, MapCodec<T> codec) {
		return Registry.register(Registries.LOOT_FUNCTION_TYPE, Hollow.id(id), new LootFunctionType<>(codec));
	}

	public static void init() {
		// NO-OP
	}
}
