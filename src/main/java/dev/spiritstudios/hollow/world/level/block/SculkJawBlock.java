package dev.spiritstudios.hollow.world.level.block;

import dev.spiritstudios.hollow.world.entity.HollowDamageTypes;
import dev.spiritstudios.hollow.world.entity.HollowEntityTypes;
import dev.spiritstudios.hollow.sounds.HollowSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SculkBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class SculkJawBlock extends SculkBlock {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public SculkJawBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        if (world.isClientSide() || entity.is(HollowEntityTypes.Tags.IMMUNE_TO_SCULK_JAW)) {
            super.stepOn(world, pos, state, entity);
            return;
        }

        if (!world.getBlockState(pos).getValue(ACTIVE)) {
            world.playSound(null, pos.above(), HollowSoundEvents.SCULK_JAW_BITE, SoundSource.BLOCKS, 1F, 0.6F);
            world.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
        }

        if (world.isClientSide()) {
            RandomSource random = world.getRandom();
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

        entity.hurtServer((ServerLevel) world, world.damageSources().source(HollowDamageTypes.SCULK_JAW), 1F);

        if (world.getGameTime() % 5 == 0) {
            world.playSound(
                    null,
                    pos.above(),
                    SoundEvents.SOUL_ESCAPE.value(),
                    SoundSource.BLOCKS,
                    1F,
                    1F
            );
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(ACTIVE);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        world.setBlockAndUpdate(pos, state.setValue(ACTIVE, false));
    }
}
