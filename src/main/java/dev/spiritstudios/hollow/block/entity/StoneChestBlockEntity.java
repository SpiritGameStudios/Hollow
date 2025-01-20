package dev.spiritstudios.hollow.block.entity;

import dev.spiritstudios.hollow.registry.HollowBlockEntityTypes;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import dev.spiritstudios.hollow.registry.HollowSoundEvents;
import dev.spiritstudios.specter.api.block.entity.LootableInventoryBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class StoneChestBlockEntity extends LootableInventoryBlockEntity {
    public StoneChestBlockEntity(BlockPos pos, BlockState state) {
        super(HollowBlockEntityTypes.STONE_CHEST_BLOCK_ENTITY, pos, state, 27);
    }

    public void aboveBroken() {
        if (world == null) return;
        if (world.isClient()) return;

        generateLoot(null);
        Vec3d centerPos = pos.toCenterPos();
        inventory.stream()
                .filter(stack -> !stack.isEmpty())
                .map(stack -> new ItemEntity(world, centerPos.getX(), centerPos.getY() + 0.5, centerPos.getZ(), stack))
                .forEach(itemEntity -> world.spawnEntity(itemEntity));

        ((ServerWorld) world).spawnParticles(
                ParticleTypes.DUST_PLUME,
                (double)pos.getX() + 0.5,
                (double)pos.getY() + 0.9,
                (double)pos.getZ() + 0.5,
                7, 0.0, 0.0, 0.0, 0.0
        );

        world.playSound(null, pos, HollowSoundEvents.STONE_CHEST_EXTRACT, SoundCategory.BLOCKS);
        
        inventory.clear();
    }

    public ActionResult use(PlayerEntity player, Hand hand, Direction side) {
        if (world == null) return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
        if (player.getStackInHand(hand).isEmpty() || player.getStackInHand(hand).isOf(HollowBlocks.STONE_CHEST_LID.asItem()) && side.equals(Direction.UP))
            return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;

        if (!world.isAir(pos.up())) return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;

        if (world.isClient()) return ActionResult.SUCCESS;

        int slot = -1;
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).isEmpty()) {
                slot = i;
                break;
            }
        }

        if (slot == -1) return ActionResult.FAIL;

        setStack(slot, player.getStackInHand(hand));
        player.setStackInHand(hand, ItemStack.EMPTY);

        if (!inventory.isEmpty() && !player.getWorld().isClient()) player.getWorld().playSound(
                null,
                pos,
                SoundEvents.ENTITY_ITEM_PICKUP,
                SoundCategory.PLAYERS,
                0.2f,
                ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F
        );

        return ActionResult.SUCCESS;
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.clear();
        if (!this.readLootTable(nbt)) Inventories.readNbt(nbt, this.inventory, registryLookup);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        if (!this.writeLootTable(nbt)) Inventories.writeNbt(nbt, this.inventory, registryLookup);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound nbtCompound = new NbtCompound();
        Inventories.writeNbt(nbtCompound, this.inventory, true, registryLookup);
        return nbtCompound;
    }
}
