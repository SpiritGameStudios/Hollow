package dev.callmeecho.hollow.main.block.entity;

import dev.callmeecho.cabinetapi.particle.ParticleSystem;
import dev.callmeecho.cabinetapi.misc.DefaultedInventory;
import dev.callmeecho.hollow.main.registry.HollowBlockEntityRegistry;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class StoneChestBlockEntity extends BlockEntity implements DefaultedInventory {
    public static final String LOOT_TABLE_KEY = "LootTable";
    public static final String LOOT_TABLE_SEED_KEY = "LootTableSeed";

    private static final ParticleSystem PARTICLE_SYSTEM = new ParticleSystem(
            new Vec3d(0.025F, 0.05F, 0.025F),
            new Vec3d(0.5, 1, 0.5),
            new Vec3d(0, 0, 0),
            15,
            1,
            true,
            ParticleTypes.LARGE_SMOKE
    );
    
    @Nullable
    protected Identifier lootTableId;
    protected long lootTableSeed;
    
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
    
    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    public StoneChestBlockEntity(BlockPos pos, BlockState state) {
        super(HollowBlockEntityRegistry.STONE_CHEST_BLOCK_ENTITY, pos, state);
    }

    public void checkLootInteraction(@Nullable PlayerEntity player) {
        if (world == null) return;
        if (this.lootTableId != null && this.world.getServer() != null) {
            LootTable lootTable = this.world.getServer().getLootManager().getLootTable(this.lootTableId);
            if (player instanceof ServerPlayerEntity) {
                Criteria.PLAYER_GENERATES_CONTAINER_LOOT.trigger((ServerPlayerEntity)player, this.lootTableId);
            }

            this.lootTableId = null;
            LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder((ServerWorld)this.world)
                    .add(LootContextParameters.ORIGIN, Vec3d.ofCenter(this.pos));
            if (player != null) {
                builder.luck(player.getLuck()).add(LootContextParameters.THIS_ENTITY, player);
            }

            // I would use the seed but that would make it the same for every chest in a structure.
            // I have no idea how mojang does it and I can't be bothered to look into it. So you get this hacky solution instead.
            lootTable.supplyInventory(this, builder.build(LootContextTypes.CHEST), world.getRandom().nextLong());
        }
    }

    public static void setLootTable(BlockView world, Random random, BlockPos pos, Identifier id) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof LootableContainerBlockEntity) {
            ((LootableContainerBlockEntity)blockEntity).setLootTable(id, random.nextLong());
        }
    }

    protected boolean deserializeLootTable(NbtCompound nbt) {
        if (nbt.contains("LootTable", NbtElement.STRING_TYPE)) {
            this.lootTableId = new Identifier(nbt.getString("LootTable"));
            this.lootTableSeed = nbt.getLong("LootTableSeed");
            return true;
        } else {
            return false;
        }
    }

    protected boolean serializeLootTable(NbtCompound nbt) {
        if (this.lootTableId == null) {
            return false;
        } else {
            nbt.putString("LootTable", this.lootTableId.toString());
            if (this.lootTableSeed != 0L) {
                nbt.putLong("LootTableSeed", this.lootTableSeed);
            }

            return true;
        }
    }

    public void setLootTable(Identifier id, long seed) {
        this.lootTableId = id;
        this.lootTableSeed = seed;
    }
    
    public void aboveBroken() {
        if (world == null) return;
        checkLootInteraction(null);
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

    public void use(PlayerEntity player, Hand hand) {
        if (player.getStackInHand(hand).isEmpty()) return;

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

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        inventory.clear();
        if (!this.deserializeLootTable(nbt)) {
            Inventories.readNbt(nbt, this.inventory);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (!this.serializeLootTable(nbt)) {
            Inventories.writeNbt(nbt, this.inventory);
        }
    }

    @Nullable
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
