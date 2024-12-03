package dev.spiritstudios.hollow.block;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.HollowTags;
import dev.spiritstudios.hollow.registry.HollowDamageTypes;
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
        if (world.isClient() || entity.getType().isIn(HollowTags.IMMUNE_TO_SCULK_JAW)) {
            super.onSteppedOn(world, pos, state, entity);
            return;
        }

        if (!world.getBlockState(pos).get(ACTIVE))
            world.playSound(null, pos.up(), HollowSoundEvents.SCULK_JAW_BITE, SoundCategory.BLOCKS, 1F, 0.6F);

        world.setBlockState(pos, state.with(ACTIVE, true));

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

        entity.damage(world.getDamageSources().create(HollowDamageTypes.SCULK_JAW), 1F);

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
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(ACTIVE);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.setBlockState(pos, state.with(ACTIVE, false));
    }
}
