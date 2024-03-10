package dev.callmeecho.hollow.main.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public interface ImplementedInventory extends Inventory {
    DefaultedList<ItemStack> getItems();

    @Override
    default int size() {
        return getItems().size();
    }

    @Override
    default boolean isEmpty() {
        return getItems().isEmpty();
    }

    @Override
    default ItemStack getStack(int slot) {
        return getItems().get(slot);
    }

    @Override
    default ItemStack removeStack(int slot, int amount) {
        ItemStack stack = Inventories.splitStack(getItems(), slot, amount);
        this.notifyListeners();

        return stack;
    }

    @Override
    default ItemStack removeStack(int slot) {
        ItemStack stack = Inventories.removeStack(getItems(), slot);
        this.notifyListeners();
        return stack;
    }

    default void notifyListeners() {
        markDirty();

        if (getWorld() != null && !getWorld().isClient) {
            getWorld().updateListeners(getPos(), getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        }
    }
    @Override
    default void setStack(int slot, ItemStack stack) {
        getItems().set(slot, stack);
        this.notifyListeners();
    }
    @Override
    default void clear() {
        getItems().clear();
        this.notifyListeners();
    }

    @Override
    default boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Nullable
    World getWorld();

    BlockPos getPos();

    BlockState getCachedState();
}
