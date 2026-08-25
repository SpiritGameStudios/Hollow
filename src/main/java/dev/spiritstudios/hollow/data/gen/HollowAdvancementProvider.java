package dev.spiritstudios.hollow.data.gen;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.tags.HollowDamageTypeTags;
import dev.spiritstudios.hollow.world.item.HollowItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.predicates.DamagePredicate;
import net.minecraft.advancements.predicates.DamageSourcePredicate;
import net.minecraft.advancements.predicates.TagPredicate;
import net.minecraft.advancements.triggers.EntityHurtPlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class HollowAdvancementProvider extends FabricAdvancementProvider {
	public HollowAdvancementProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
		super(output, registryLookup);
	}

	@Override
	public void generateAdvancement(HolderLookup.Provider registryLookup, Consumer<AdvancementHolder> consumer) {
		AdvancementHolder adventureRoot = createPlaceholder(Identifier.withDefaultNamespace("adventure/root"));

		new Advancement.Builder()
			.addCriterion(
				"get_hurt_by_sculk_jaw",
				EntityHurtPlayerTrigger.TriggerInstance.entityHurtPlayer(
					DamagePredicate.Builder.damageInstance()
						.type(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(HollowDamageTypeTags.IS_SCULK_JAW)))
				)
			)
			.parent(adventureRoot)
			.display(
				HollowItems.SCULK_JAW,
				Component.translatable("advancements.hollow.adventure.get_hurt_by_sculk_jaw.title"),
				Component.translatable("advancements.hollow.adventure.get_hurt_by_sculk_jaw.description"),
				null,
				AdvancementType.TASK,
				true,
				true,
				true // Hidden as not to spoil the surprise
			)
			.save(consumer, Hollow.id("adventure/get_hurt_by_sculk_jaw"));
	}
}
