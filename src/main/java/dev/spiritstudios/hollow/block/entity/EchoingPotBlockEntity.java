package dev.spiritstudios.hollow.block.entity;

import dev.spiritstudios.hollow.registry.HollowBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class EchoingPotBlockEntity extends BlockEntity {
    public int activeTime = 0;
    public long lastWobbleTime;
    public DecoratedPotBlockEntity.WobbleType lastWobbleType;

    public EchoingPotBlockEntity(BlockPos pos, BlockState state) {
        super(HollowBlockEntityTypes.ECHOING_POT, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, EchoingPotBlockEntity blockEntity) {

    }

    public void use(PlayerEntity player, Hand hand) {
        wobble(DecoratedPotBlockEntity.WobbleType.NEGATIVE);
        player.getWorld().playSound(null, pos, SoundEvents.BLOCK_DECORATED_POT_INSERT_FAIL, SoundCategory.BLOCKS, 1.0F, 1.0F);
        player.getWorld().emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

    }

    public void wobble(DecoratedPotBlockEntity.WobbleType wobbleType) {
        if (this.world != null && !this.world.isClient())
            this.world.addSyncedBlockEvent(this.getPos(), this.getCachedState().getBlock(), 1, wobbleType.ordinal());
    }

    @Override
    public boolean onSyncedBlockEvent(int type, int data) {
        if (this.world == null || type != 1 || data < 0 || data >= DecoratedPotBlockEntity.WobbleType.values().length)
            return super.onSyncedBlockEvent(type, data);

        this.lastWobbleTime = this.world.getTime();
        this.lastWobbleType = DecoratedPotBlockEntity.WobbleType.values()[data];
        return true;
    }

    // region NBT
    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putInt("ActiveTime", activeTime);
        super.writeNbt(nbt, registryLookup);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        activeTime = nbt.getInt("ActiveTime");
    }
    // endregion
}
