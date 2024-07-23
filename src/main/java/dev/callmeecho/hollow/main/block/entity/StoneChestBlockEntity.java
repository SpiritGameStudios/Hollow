package dev.callmeecho.hollow.main.block.entity;

import dev.callmeecho.cabinetapi.particle.ParticleSystem;
import dev.callmeecho.cabinetapi.util.LootableInventoryBlockEntity;
import dev.callmeecho.hollow.main.registry.HollowBlockEntityRegistrar;
import dev.callmeecho.hollow.main.registry.HollowBlockRegistrar;
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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class StoneChestBlockEntity extends LootableInventoryBlockEntity {
    private static final ParticleSystem PARTICLE_SYSTEM = new ParticleSystem(
            new Vec3d(0.025F, 0.05F, 0.025F),
            new Vec3d(0.5, 1, 0.5),
            new Vec3d(0, 0, 0),
            15,
            1,
            true,
            ParticleTypes.LARGE_SMOKE
    );

    public StoneChestBlockEntity(BlockPos pos, BlockState state) {
        super(HollowBlockEntityRegistrar.STONE_CHEST_BLOCK_ENTITY, pos, state, 27);
    }


    public void checkLootInteraction(@Nullable PlayerEntity player, boolean randomSeed) {
        if (world == null) return;
        if (this.lootTable != null && this.world.getServer() != null) {
            LootTable lootTable = this.world.getServer().getReloadableRegistries().getLootTable(this.lootTable);
            if (player instanceof ServerPlayerEntity) {
                Criteria.PLAYER_GENERATES_CONTAINER_LOOT.trigger((ServerPlayerEntity)player, this.lootTable);
            }

            this.lootTable = null;
            LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder((ServerWorld)this.world)
                    .add(LootContextParameters.ORIGIN, Vec3d.ofCenter(this.pos));
            if (player != null) {
                builder.luck(player.getLuck()).add(LootContextParameters.THIS_ENTITY, player);
            }

            lootTable.supplyInventory(
                    this,
                    builder.build(LootContextTypes.CHEST),
                    randomSeed ? world.getRandom().nextLong() : this.lootTableSeed
            );
        }
    }
    
    public void aboveBroken() {
        if (world == null) return;
        checkLootInteraction(null, true);
        Vec3d centerPos = pos.toCenterPos();
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) {
                ItemEntity itemEntity = new ItemEntity(world, centerPos.getX(), centerPos.getY() + 0.5, centerPos.getZ(), stack);
                world.spawnEntity(itemEntity);
            }
        }
        
        inventory.clear();
        notifyListeners();
        
        PARTICLE_SYSTEM.tick(world, pos);
    }

    public ItemActionResult use(PlayerEntity player, Hand hand, Direction side) {
        if (player.getStackInHand(hand).isEmpty()) return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        if (player.getStackInHand(hand).isOf(HollowBlockRegistrar.STONE_CHEST_LID.asItem()) && side.equals(Direction.UP)) return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;


        int slot = -1;
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).isEmpty()) {
                slot = i;
                break;
            }
        }

        if (slot == -1) return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;

        setStack(slot, player.getStackInHand(hand));
        notifyListeners();
        player.setStackInHand(hand, ItemStack.EMPTY);
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
