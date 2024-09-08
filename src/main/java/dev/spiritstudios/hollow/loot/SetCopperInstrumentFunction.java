package dev.spiritstudios.hollow.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.hollow.item.CopperHornItem;
import dev.spiritstudios.hollow.registry.HollowDataComponentRegistrar;
import net.minecraft.item.Instrument;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.function.LootFunctionTypes;
import net.minecraft.loot.function.SetInstrumentLootFunction;
import net.minecraft.registry.tag.TagKey;

import java.util.List;

public class SetCopperInstrumentFunction extends ConditionalLootFunction {
    public static final MapCodec<SetCopperInstrumentFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> addConditionsField(instance).apply(instance, SetCopperInstrumentFunction::new)
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
        if (stack.getItem() instanceof CopperHornItem) {
            HollowDataComponentRegistrar.CopperInstrument[] values = HollowDataComponentRegistrar.CopperInstrument.values();
            stack.set(
                    HollowDataComponentRegistrar.COPPER_INSTRUMENT,
                    values[context.getRandom().nextInt(values.length)]
            );
        }
        return stack;
    }
}
