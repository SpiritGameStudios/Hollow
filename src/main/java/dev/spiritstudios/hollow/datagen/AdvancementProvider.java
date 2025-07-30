package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.registry.HollowCriteria;
import dev.spiritstudios.hollow.item.HollowItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.TickCriterion;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AdvancementProvider extends FabricAdvancementProvider {
    protected AdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @SuppressWarnings("removal")
    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup wrapperLookup, Consumer<AdvancementEntry> consumer) {
        Advancement.Builder.createUntelemetered()
                .display(
                        HollowItems.FIREFLY_SPAWN_EGG,
                        Text.translatable("advancements.husbandry.witnessed_frog_poisoning.title"),
                        Text.translatable("advancements.husbandry.witnessed_frog_poisoning.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .parent(Identifier.ofVanilla("husbandry/root"))
                .criterion(
                        "witnessed_frog_poisoning",
                        HollowCriteria.FROG_POISONED.create(new TickCriterion.Conditions(Optional.empty()))
                )
                .build(consumer, "husbandry/witnessed_frog_poisoning");
    }
}
