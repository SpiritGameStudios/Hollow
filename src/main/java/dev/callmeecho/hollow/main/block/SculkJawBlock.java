package dev.callmeecho.hollow.main.block;

import dev.callmeecho.cabinetapi.particle.ParticleSystem;
import dev.callmeecho.hollow.main.Hollow;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.Set;

public class SculkJawBlock extends SculkBlock {
    public static final RegistryKey<DamageType> SCULK_JAW_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(Hollow.MODID, "sculk_jaw"));
    
    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");
    private final ParticleSystem particleSystem;
    
    public SculkJawBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(ACTIVE, false));
        particleSystem = new ParticleSystem(
                new Vec3d(0.007F, 0.07F, 0.007F),
                new Vec3d(0.5, 1, 0.5),
                new Vec3d(0.45, 0, 0.45),
                1,
                1,
                true,
                ParticleTypes.SCULK_SOUL
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.bypassesSteppingEffects() && !world.isClient() && !(entity instanceof WardenEntity)) {
            if (!world.getBlockState(pos).get(ACTIVE))
                world.playSound(null, pos.up(), SoundEvents.ENTITY_GOAT_HORN_BREAK, SoundCategory.BLOCKS, 1F, 0.6F);
            
            world.setBlockState(pos, state.with(ACTIVE, true));

            Vec3d centerPos = pos.toCenterPos();
            entity.teleport(
                    (ServerWorld)world,
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
        if (!world.isClient() && state.get(ACTIVE)) {
            DamageSource damageSource = new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(SCULK_JAW_DAMAGE_TYPE));
            entity.damage(damageSource, 3F);
            
            Vec3d centerPos = pos.toCenterPos();
            entity.teleport(
                    (ServerWorld)world,
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

            particleSystem.tick(world, pos);

            if (world.random.nextFloat() <= 0.0025F)
                world.setBlockState(pos, state.with(ACTIVE, false));
        }
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
