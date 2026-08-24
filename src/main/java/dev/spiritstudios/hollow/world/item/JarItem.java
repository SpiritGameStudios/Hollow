package dev.spiritstudios.hollow.world.item;

import dev.spiritstudios.hollow.sounds.HollowSoundEvents;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;

public class JarItem extends BlockItem {
    public JarItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();
		Player player = context.getPlayer();

		if (!level.getBlockState(blockPos).is(Blocks.FIREFLY_BUSH) || player == null) {
			return super.useOn(context);
		}

		level.setBlockAndUpdate(blockPos, HollowBlocks.SWITCHGRASS.defaultBlockState());
		level.gameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);
		level.playSound(null, blockPos, HollowSoundEvents.JAR_USE_FIREFLIES, SoundSource.PLAYERS, 1.0F, Mth.randomBetween(level.getRandom(), 0.8F, 1.0F));

	    return InteractionResult.SUCCESS.heldItemTransformedTo(ItemUtils.createFilledResult(
                context.getItemInHand(), player,
                new ItemStack(HollowItems.FIREFLY_JAR)
        ));
    }
}
