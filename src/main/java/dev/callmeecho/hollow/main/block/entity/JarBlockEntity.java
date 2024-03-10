package dev.callmeecho.hollow.main.block.entity;

import dev.callmeecho.hollow.main.registry.HollowBlockEntityRegistry;
import dev.callmeecho.hollow.main.utils.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class JarBlockEntity extends BlockEntity implements ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(16, ItemStack.EMPTY);
    
    public JarBlockEntity(BlockPos pos, BlockState state) {
        super(HollowBlockEntityRegistry.JAR_BLOCK_ENTITY, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }
    
    public void use(World world, BlockPos pos, PlayerEntity player, Hand hand) {
        if (!inventory.isEmpty()) {
            world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
        }

        if (player.getStackInHand(hand).isEmpty()) {
            ItemStack stack = null;
            // extract item from jar
            for (ItemStack newStack : inventory) {
                if (!newStack.isEmpty()) {
                    stack = newStack;
                }
            }

            if (stack == null) return;

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

        setStack(slot, player.getStackInHand(hand));
        notifyListeners();
        player.setStackInHand(hand, ItemStack.EMPTY);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        inventory.clear();
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, inventory);
        super.writeNbt(nbt);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public World getWorld() {
        return super.getWorld();
    }

    @Override
    public BlockPos getPos() {
        return super.getPos();
    }

    @Override
    public BlockState getCachedState() {
        return super.getCachedState();
    }
}
