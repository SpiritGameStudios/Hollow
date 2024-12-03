package dev.spiritstudios.hollow.block.entity;

import dev.spiritstudios.hollow.registry.HollowBlockEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.WardenAngerManager;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class EchoingPotBlockEntity extends BlockEntity {
    public int activeTime = 0;
    public long lastWobbleTime;
    public DecoratedPotBlockEntity.WobbleType lastWobbleType;

    public EchoingPotBlockEntity(BlockPos pos, BlockState state) {
        super(HollowBlockEntityTypes.ECHOING_POT_BLOCK_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, EchoingPotBlockEntity blockEntity) {
        if (blockEntity.activeTime <= 0) return;

        if (!world.isClient) {
            blockEntity.activeTime--;
            blockEntity.markDirty();
            world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);

            List<WardenEntity> wardens = world.getEntitiesByClass(
                    WardenEntity.class,
                    Box.from(pos.toCenterPos()).expand(16),
                    warden -> true
            );

            wardens.forEach(warden -> {
                WardenAngerManager angerManager = warden.getAngerManager();

                Optional<LivingEntity> target = angerManager.getPrimeSuspect();
                if (target.isEmpty()) return;

                angerManager.removeSuspect(target.get());
            });
        } else {
            Random random = world.getRandom();
            for (int i = 0; i < 2; ++i) {
                float x = 2.0F * random.nextFloat() - 1.0F;
                float y = 2.0F * random.nextFloat() - 1.0F;
                float z = 2.0F * random.nextFloat() - 1.0F;
                world.addParticle(
                        ParticleTypes.SCULK_SOUL,
                        (double) pos.getX() + 0.5 + (x * 0.25),
                        (double) pos.getY() + 1,
                        (double) pos.getZ() + 0.5 + (z * 0.25),
                        (x * 0.0075F),
                        (y * 0.075F),
                        (z * 0.0075F)
                );
            }

            if (world.random.nextInt(5) == 0)
                world.playSoundAtBlockCenter(pos, SoundEvents.PARTICLE_SOUL_ESCAPE.value(), SoundCategory.BLOCKS, 0.5F, 1.0F, false);
        }
    }

    public void use(PlayerEntity player, Hand hand) {
        if (!player.getStackInHand(hand).isOf(Items.ECHO_SHARD) || activeTime > 0) {
            wobble(DecoratedPotBlockEntity.WobbleType.NEGATIVE);
            player.getWorld().playSound(null, pos, SoundEvents.BLOCK_DECORATED_POT_INSERT_FAIL, SoundCategory.BLOCKS, 1.0F, 1.0F);
            player.getWorld().emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            return;
        }

        activeTime += 300;
        player.getStackInHand(hand).decrement(1);
        wobble(DecoratedPotBlockEntity.WobbleType.POSITIVE);
        player.getWorld().emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        player.getWorld().playSound(null, pos, SoundEvents.BLOCK_DECORATED_POT_INSERT, SoundCategory.BLOCKS, 1.0F, 1.0F);
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
