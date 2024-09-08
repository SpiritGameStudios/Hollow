package dev.spiritstudios.hollow.block.entity;

import dev.spiritstudios.hollow.registry.HollowBlockEntityRegistrar;
import dev.spiritstudios.specter.api.block.entity.InventoryBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.stream.IntStream;

public class JarBlockEntity extends InventoryBlockEntity {
    public JarBlockEntity(BlockPos pos, BlockState state) {
        super(HollowBlockEntityRegistrar.JAR_BLOCK_ENTITY, pos, state, 17);
    }

    public void use(World world, BlockPos pos, PlayerEntity player, Hand hand) {
        if (!inventory.isEmpty() && !world.isClient) world.playSound(
                null,
                pos,
                SoundEvents.ENTITY_ITEM_PICKUP,
                SoundCategory.PLAYERS,
                0.2f,
                ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F
        );

        if (player.getStackInHand(hand).isEmpty()) {
            int slot = IntStream.iterate(inventory.size() - 1, i -> i >= 0, i -> i - 1).filter(i -> !inventory.get(i).isEmpty()).findFirst().orElse(-1);
            if (slot == -1) return;

            ItemStack stack = inventory.get(slot);

            player.setStackInHand(hand, stack.copy());
            inventory.set(inventory.indexOf(stack), ItemStack.EMPTY);
            this.markDirty();
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

        setStack(slot, player.getStackInHand(hand));
        this.markDirty();
        player.setStackInHand(hand, ItemStack.EMPTY);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (world != null)
            world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), Block.NOTIFY_ALL);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
}
