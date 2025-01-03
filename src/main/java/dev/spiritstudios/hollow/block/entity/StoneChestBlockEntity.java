package dev.spiritstudios.hollow.block.entity;

import dev.spiritstudios.hollow.registry.HollowBlockEntityTypes;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import dev.spiritstudios.hollow.registry.HollowSoundEvents;
import dev.spiritstudios.specter.api.block.entity.LootableInventoryBlockEntity;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class StoneChestBlockEntity extends LootableInventoryBlockEntity {
    public StoneChestBlockEntity(BlockPos pos, BlockState state) {
        super(HollowBlockEntityTypes.STONE_CHEST_BLOCK_ENTITY, pos, state, 27);
    }

    public void checkLootInteraction(@Nullable PlayerEntity player, boolean randomSeed) {
        if (world == null) return;

        MinecraftServer server = world.getServer();
        if (this.lootTable == null || server == null) return;

        LootTable lootTable = server.getReloadableRegistries().getLootTable(this.lootTable);
        if (player instanceof ServerPlayerEntity serverPlayer)
            Criteria.PLAYER_GENERATES_CONTAINER_LOOT.trigger(serverPlayer, this.lootTable);
        this.lootTable = null;

        LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder((ServerWorld)this.world)
                .add(LootContextParameters.ORIGIN, Vec3d.ofCenter(this.pos));

        if (player != null) builder.luck(player.getLuck()).add(LootContextParameters.THIS_ENTITY, player);

        lootTable.supplyInventory(
                this,
                builder.build(LootContextTypes.CHEST),
                randomSeed ? world.getRandom().nextLong() : this.lootTableSeed
        );
    }
    
    public void aboveBroken() {
        if (world == null) return;
        if (world.isClient()) return;
        checkLootInteraction(null, true);
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

    public ItemActionResult use(PlayerEntity player, Hand hand, Direction side) {
        if (world == null) return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (player.getStackInHand(hand).isEmpty() || player.getStackInHand(hand).isOf(HollowBlocks.STONE_CHEST_LID.asItem()) && side.equals(Direction.UP))
            return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (!world.isAir(pos.up())) return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;


        int slot = -1;
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).isEmpty()) {
                slot = i;
                break;
            }
        }

        if (slot == -1) return ItemActionResult.FAIL;

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

        return ItemActionResult.SUCCESS;
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
