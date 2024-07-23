package dev.callmeecho.hollow.main.block.entity;

import dev.callmeecho.cabinetapi.util.DefaultedInventory;
import dev.callmeecho.cabinetapi.util.InventoryBlockEntity;
import dev.callmeecho.hollow.main.registry.HollowBlockEntityRegistrar;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class JarBlockEntity extends InventoryBlockEntity implements DefaultedInventory {
    public JarBlockEntity(BlockPos pos, BlockState state) {
        super(HollowBlockEntityRegistrar.JAR_BLOCK_ENTITY, pos, state, 17);
    }
    
    public void use(World world, BlockPos pos, PlayerEntity player, Hand hand) {
        if (!inventory.isEmpty() && !world.isClient) {
            world.playSound(
                    null,
                    pos,
                    SoundEvents.ENTITY_ITEM_PICKUP,
                    SoundCategory.PLAYERS,
                    0.2f,
                    ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F
            );
        }

        if (player.getStackInHand(hand).isEmpty()) {
            int slot = -1;
            for (int i = inventory.size() - 1; i >= 0; i--) {
                if (!inventory.get(i).isEmpty()) {
                    slot = i;
                    break;
                }
            }
            if (slot == -1) return;

            ItemStack stack = inventory.get(slot);

            player.setStackInHand(hand, stack.copy());
            inventory.set(inventory.indexOf(stack), ItemStack.EMPTY);
            notifyListeners();
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
        notifyListeners();
        player.setStackInHand(hand, ItemStack.EMPTY);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        this.inventory.clear();
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, this.inventory, registryLookup);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.writeNbt(nbt, this.inventory, registryLookup);
        super.writeNbt(nbt, registryLookup);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound nbtCompound = new NbtCompound();
        Inventories.writeNbt(nbtCompound, this.inventory, true, registryLookup);
        return nbtCompound;
    }
}
