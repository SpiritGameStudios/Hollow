package dev.spiritstudios.hollow.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.spiritstudios.hollow.world.level.block.GiantLilyPadBlock;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlaceOnWaterBlockItem.class)
public class PlaceOnWaterBlockItemMixin extends BlockItemMixin {
	@Override
	public boolean placeBlock(BlockPlaceContext context, BlockState placementState, Operation<Boolean> original) {
		return GiantLilyPadBlock.tryForm(context, placementState) || super.placeBlock(context, placementState, original);
	}
}
