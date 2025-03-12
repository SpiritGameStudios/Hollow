package dev.spiritstudios.hollow.block.entity;

import dev.spiritstudios.hollow.registry.HollowBlockEntityTypes;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class EchoingVaseBlockEntity extends BlockEntity {
    public static int TILT_TIME = 60;
    public static int FALL_TIME = 80;

    public int activeTime = 0;
    public long lastWobbleTime;
    public int fallTime = 0;
    public boolean fallen = false;
    public Direction fallDirection = Direction.NORTH;
    public DecoratedPotBlockEntity.WobbleType lastWobbleType;

    public EchoingVaseBlockEntity(BlockPos pos, BlockState state) {
        super(HollowBlockEntityTypes.ECHOING_VASE, pos, state);
    }

    public void use(PlayerEntity player, Hand hand) {
        wobble(DecoratedPotBlockEntity.WobbleType.NEGATIVE);
        player.getWorld().playSound(null, pos, SoundEvents.BLOCK_DECORATED_POT_INSERT_FAIL, SoundCategory.BLOCKS, 1.0F, 1.0F);
        player.getWorld().emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

    }

    public void wobble(DecoratedPotBlockEntity.WobbleType wobbleType) {
        if (this.world == null || this.world.isClient()) return;

        this.world.addSyncedBlockEvent(this.getPos(), this.getCachedState().getBlock(), 1, wobbleType.ordinal());

        this.world.addSyncedBlockEvent(this.getPos().up(), this.getCachedState().getBlock(), 1, wobbleType.ordinal());
    }

    public void setFalling(Direction dir, boolean top, World world, BlockPos pos) {
        this.fallTime = 1;
        this.fallDirection = dir;
        if (top) {
            Objects.requireNonNull((EchoingVaseBlockEntity) world.getBlockEntity(pos.up())).setFalling(dir, false, world, pos);
        }
    }

    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (state.isOf(HollowBlocks.SCREAMING_VASE) && this.fallTime == 0 && this.getCachedState().get(Properties.DOUBLE_BLOCK_HALF).equals(DoubleBlockHalf.LOWER)) {
            this.setFalling(Direction.getFacing(pos.toCenterPos().subtract(entity.getPos())), true, world, pos);
        }
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
