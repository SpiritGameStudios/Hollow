package dev.spiritstudios.hollow.world.item;

import dev.spiritstudios.hollow.sounds.HollowSoundEvents;
import dev.spiritstudios.hollow.tags.HollowBlockItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class JarItem extends BlockItem {
    public JarItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();
        if (!level.getBlockState(blockPos).is(HollowBlockItemTags.CONTAINS_COLLECTABLE_FIREFLIES.block())) {
            return super.useOn(context);
        }

        level.playSound(null, blockPos, HollowSoundEvents.JAR_USE_FIREFLIES, SoundSource.PLAYERS, 1.0F, 1.0F);

        return InteractionResult.SUCCESS.heldItemTransformedTo(ItemUtils.createFilledResult(
                context.getItemInHand(), context.getPlayer(),
                new ItemStack(HollowItems.JAR_OF_FIREFLIES)
        ));
    }
}
