package dev.spiritstudios.hollow.block;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.registry.HollowSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SculkBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.Set;

public class SculkJawBlock extends SculkBlock {
    public static final RegistryKey<DamageType> SCULK_JAW_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(Hollow.MODID, "sculk_jaw"));

    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

    public SculkJawBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(ACTIVE, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.bypassesSteppingEffects() && !world.isClient() && !(entity instanceof WardenEntity)) {
            if (!world.getBlockState(pos).get(ACTIVE))
                world.playSound(null, pos.up(), HollowSoundEvents.SCULK_JAW_BITE, SoundCategory.BLOCKS, 1F, 0.6F);

            world.setBlockState(pos, state.with(ACTIVE, true));

            Vec3d centerPos = pos.toCenterPos();
            entity.teleport(
                    (ServerWorld) world,
                    centerPos.getX(),
                    centerPos.getY(),
                    centerPos.getZ(),
                    Set.of(),
                    entity.getYaw(),
                    entity.getPitch()
            );
        }

        super.onSteppedOn(world, pos, state, entity);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!state.get(ACTIVE)) return;

        if (world.isClient) {
            Random random = world.getRandom();
            for (int i = 0; i < 2; ++i) {
                float x = 2.0F * random.nextFloat() - 1.0F;
                float y = 2.0F * random.nextFloat() - 1.0F;
                float z = 2.0F * random.nextFloat() - 1.0F;
                world.addParticle(
                        ParticleTypes.SCULK_SOUL,
                        (double) pos.getX() + 0.5 + (x * 0.45),
                        (double) pos.getY() + 1,
                        (double) pos.getZ() + 0.5 + (z * 0.45),
                        (x * 0.0075F),
                        (y * 0.075F),
                        (z * 0.0075F)
                );
            }

            return;
        }

        DamageSource damageSource = new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(SCULK_JAW_DAMAGE_TYPE));
        entity.damage(damageSource, 1F);

        Vec3d centerPos = pos.toCenterPos();
        entity.teleport(
                (ServerWorld) world,
                centerPos.getX(),
                centerPos.getY(),
                centerPos.getZ(),
                Set.of(),
                entity.getYaw(),
                entity.getPitch()
        );

        if (world.getTime() % 5 == 0) {
            world.playSound(
                    null,
                    pos.up(),
                    SoundEvents.PARTICLE_SOUL_ESCAPE.value(),
                    SoundCategory.BLOCKS,
                    1F,
                    1F
            );
        }

        if (world.random.nextFloat() <= 0.0025F)
            world.setBlockState(pos, state.with(ACTIVE, false));
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(ACTIVE);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(ACTIVE)) world.setBlockState(pos, state.with(ACTIVE, false));
    }
}
