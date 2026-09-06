package dev.spiritstudios.hollow.client.data.gen;

import com.supermartijn642.fusion.api.model.predicates.blockstate.DefaultBlockStateModelPredicates;
import com.supermartijn642.fusion.api.provider.FusionBlockModelModifierProvider;
import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class HollowFusionBlockModelModifierProvider extends FusionBlockModelModifierProvider {
	private static final ModelEntry MOSS_OVERHANG = overhangModelEntry("moss", Blocks.MOSS_CARPET, Blocks.MOSS_BLOCK);
	private static final ModelEntry PALE_MOSS_OVERHANG = overhangModelEntry("pale_moss", Blocks.PALE_MOSS_CARPET, Blocks.PALE_MOSS_BLOCK);

	public HollowFusionBlockModelModifierProvider(FabricPackOutput output) {
		super(Hollow.MODID, output);
	}

	@Override
	protected void generate() {
		ModifierBuilder builder = this.modifier(Hollow.id("hollow_log_layers"));

		HollowBlocks.HOLLOW_LOG.forEach(builder::target);
		HollowBlocks.STRIPPED_HOLLOW_LOG.forEach(builder::target);

		builder.appendModelSeries(MOSS_OVERHANG, PALE_MOSS_OVERHANG);
	}

	private static ModelEntry overhangModelEntry(String name, Block... blocks) {
		return ModelEntry.of(Hollow.id("block/" + name + "_overhang"))
			.conditions(DefaultBlockStateModelPredicates.matchBlock(0, 1, 0, blocks));
	}
}
