package dev.spiritstudios.hollow.world.level.storage.loot;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.Hollow;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;

public final class HollowLootFunctionTypes {
	public static final MapCodec<SetCopperInstrumentFunction> SET_COPPER_INSTRUMENT = register("set_copper_instrument", SetCopperInstrumentFunction.CODEC);

	private static <T extends LootItemFunction> MapCodec<T> register(String id, MapCodec<T> codec) {
		return Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, Hollow.id(id), codec);
	}

	public static void init() {
		// NO-OP
	}
}
