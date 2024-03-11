package dev.callmeecho.hollow.main.block.entity;

import dev.callmeecho.cabinetapi.client.particle.ParticleSystem;
import dev.callmeecho.hollow.main.registry.HollowBlockEntityRegistry;
import dev.callmeecho.hollow.main.registry.HollowParticleRegistrar;
import dev.callmeecho.hollow.main.utils.ImplementedInventory;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
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
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class FireflyJarBlockEntity extends BlockEntity {
    private final ParticleSystem particleSystem;
    
    public FireflyJarBlockEntity(BlockPos pos, BlockState state) {
        super(HollowBlockEntityRegistry.JAR_BLOCK_ENTITY, pos, state);
        particleSystem = new ParticleSystem(
                new Vec3d(0, 0, 0),
                new Vec3d(0.5, 0.5, 0.5),
                new Vec3d(0.1, 0.25, 0.1), 
                1,
                1,
                false,
                HollowParticleRegistrar.FIREFLY_JAR);
    }
    
    public void createParticles(World world, BlockPos pos, Random random) {
        particleSystem.tick(world, pos);
    
//        world.addParticle(
//                HollowParticleRegistrar.FIREFLY_JAR,
//                (pos.getX() + 0.5) + (random.nextDouble() - 0.5) / 5.0F,
//                (pos.getY() + 0.5) + (2 * random.nextDouble() - 1) / 5.0F,
//                (pos.getZ() + 0.5) + (random.nextDouble() - 0.5) / 5.0F,
//                0,
//                0,
//                0);
    }
}
