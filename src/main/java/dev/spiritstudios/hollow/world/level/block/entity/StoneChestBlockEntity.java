package dev.spiritstudios.hollow.world.level.block.entity;

import dev.spiritstudios.hollow.sound.HollowSoundEvents;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class StoneChestBlockEntity extends RandomizableNoMenuContainerBlockEntity {
    private final NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);

    public StoneChestBlockEntity(BlockPos pos, BlockState state) {
        super(HollowBlockEntityTypes.STONE_CHEST, pos, state);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    public void aboveBroken() {
        if (level == null) return;
        if (level.isClientSide()) return;

        unpackLootTable(null);
        Vec3 centerPos = Vec3.atCenterOf(worldPosition);
        items.stream()
                .filter(stack -> !stack.isEmpty())
                .map(stack -> new ItemEntity(level, centerPos.x(), centerPos.y() + 0.5, centerPos.z(), stack))
                .forEach(itemEntity -> level.addFreshEntity(itemEntity));

        ((ServerLevel) level).sendParticles(
                ParticleTypes.DUST_PLUME,
                (double) worldPosition.getX() + 0.5,
                (double) worldPosition.getY() + 0.9,
                (double) worldPosition.getZ() + 0.5,
                7, 0.0, 0.0, 0.0, 0.0
        );

        level.playSound(null, worldPosition, HollowSoundEvents.BLOCK_STONE_CHEST_EXTRACT, SoundSource.BLOCKS);
        
        items.clear();
    }

    public InteractionResult use(Player player, InteractionHand hand, Direction side) {
        if (level == null) return InteractionResult.TRY_WITH_EMPTY_HAND;
        if (player.getItemInHand(hand).isEmpty() || player.getItemInHand(hand).is(HollowBlocks.STONE_CHEST_LID.asItem()) && side.equals(Direction.UP))
            return InteractionResult.TRY_WITH_EMPTY_HAND;

        if (!level.isEmptyBlock(worldPosition.above())) return InteractionResult.TRY_WITH_EMPTY_HAND;

        if (level.isClientSide()) return InteractionResult.SUCCESS;

        int slot = -1;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isEmpty()) {
                slot = i;
                break;
            }
        }

        if (slot == -1) return InteractionResult.FAIL;

        setItem(slot, player.getItemInHand(hand));
        player.setItemInHand(hand, ItemStack.EMPTY);

        if (!items.isEmpty() && !player.level().isClientSide()) player.level().playSound(
                null,
                worldPosition,
                SoundEvents.ITEM_PICKUP,
                SoundSource.PLAYERS,
                0.2f,
                ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F
        );

        return InteractionResult.SUCCESS;
    }
}
