package dev.spiritstudios.hollow.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.stream.IntStream;

public class JarBlockEntity extends NoMenuContainerBlockEntity {
    private final NonNullList<ItemStack> items = NonNullList.withSize(17, ItemStack.EMPTY);

    public JarBlockEntity(BlockPos pos, BlockState state) {
        super(HollowBlockEntityTypes.JAR, pos, state);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    public void use(Level world, BlockPos pos, Player player, InteractionHand hand) {
        var inventory = getItems();

        if (!inventory.isEmpty() && !world.isClientSide()) {
            world.playSound(
                    null,
                    pos,
                    SoundEvents.ITEM_PICKUP,
                    SoundSource.PLAYERS,
                    0.2f,
                    ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F
            );
        }

        if (player.getItemInHand(hand).isEmpty()) {
            int slot = IntStream.iterate(inventory.size() - 1, i -> i >= 0, i -> i - 1)
                    .filter(i -> !inventory.get(i).isEmpty()).findFirst()
                    .orElse(-1);

            if (slot == -1) return;

            ItemStack stack = inventory.get(slot);

            player.setItemInHand(hand, stack.copy());
            inventory.set(inventory.indexOf(stack), ItemStack.EMPTY);
            this.setChanged();
            return;
        }

        int slot = -1;
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).isEmpty()) {
                slot = i;
                break;
            }
        }
        if (slot == -1) return;

        setItem(slot, player.getItemInHand(hand));
        this.setChanged();
        player.setItemInHand(hand, ItemStack.EMPTY);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), Block.UPDATE_ALL);
        }
    }
}
