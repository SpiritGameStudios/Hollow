package dev.callmeecho.hollow.block.entity;

import dev.callmeecho.cabinetapi.particle.ParticleSystem;
import dev.callmeecho.hollow.registry.HollowBlockEntityRegistrar;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class EchoingPotBlockEntity extends BlockEntity {
    private static final ParticleSystem PARTICLE_SYSTEM = new ParticleSystem(
            new Vec3d(0.007F, 0.07F, 0.007F),
            new Vec3d(0.5, 1, 0.5),
            new Vec3d(0.1, 0, 0.1),
            2,
            1,
            true,
            ParticleTypes.SCULK_SOUL
    );
    
    public int activeTime = 0;
    
    public EchoingPotBlockEntity(BlockPos pos, BlockState state) {
        super(HollowBlockEntityRegistrar.ECHOING_POT_BLOCK_ENTITY, pos, state);
    }

    public void use(PlayerEntity player, Hand hand) {
        if (player.getStackInHand(hand).isOf(Items.ECHO_SHARD) && activeTime <= 0) {
            activeTime += 300;
            player.getStackInHand(hand).decrement(1);
        }
    }
    
    public static void tick(World world, BlockPos pos, BlockState state, EchoingPotBlockEntity blockEntity) {
        if (blockEntity.activeTime > 0) {
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
                PARTICLE_SYSTEM.tick(world, pos);
                if (world.random.nextInt(5) == 0)
                    world.playSoundAtBlockCenter(pos, SoundEvents.PARTICLE_SOUL_ESCAPE.value(), SoundCategory.BLOCKS, 0.5F, 1.0F, false);
            }
        }
    }

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
        nbt.putInt("activeTime", activeTime);
        super.writeNbt(nbt, registryLookup);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        activeTime = nbt.getInt("activeTime");
    }
}
