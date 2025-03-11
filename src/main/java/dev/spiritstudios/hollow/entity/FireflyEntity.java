package dev.spiritstudios.hollow.entity;

import dev.spiritstudios.hollow.registry.HollowBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LightType;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.EnumSet;

public class FireflyEntity extends PathAwareEntity implements Flutterer {
    private static final TrackedData<Integer> LIGHT_TICKS = DataTracker.registerData(FireflyEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public FireflyEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);

        this.moveControl = new FlightMoveControl(this, 360, true);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
    }

    @Override
    public boolean canBeLeashed() { return false; }

    private void setLightTicks(int ticks) {
        this.dataTracker.set(LIGHT_TICKS, MathHelper.clamp(ticks, 0, 15));
    }

    public int getLightTicks() {
        return this.dataTracker.get(LIGHT_TICKS);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(LIGHT_TICKS, 0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new FlyRandomlyGoal(this));
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world) {
            @Override
            public boolean isValidPosition(BlockPos pos) {
                return !this.world.getBlockState(pos.down()).isAir();
            }
        };

        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(false);
        birdNavigation.setCanPathThroughDoors(true);
        return birdNavigation;
    }

    public static DefaultAttributeContainer.Builder createFireflyAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 0.1F)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.5F)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5F);
    }

    @Override
    public void tick() {
        super.tick();

        if (getEntityWorld().isNight() || getEntityWorld().getLightLevel(this.getBlockPos()) < 5)
            this.setLightTicks(this.age % 5 == 0 && this.random.nextInt(5) == 0 ? this.getLightTicks() - this.random.nextBetween(10, 15) : this.getLightTicks() + 1);
        else
            this.setLightTicks(this.age % 5 == 0 && this.random.nextInt(5) == 0 ? this.getLightTicks() + this.random.nextBetween(5, 10) : this.getLightTicks() - 1);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.getItem() != HollowBlocks.JAR.asItem() || !this.isAlive())
            return super.interactMob(player, hand);

        player.setStackInHand(hand, itemStack.copyWithCount(itemStack.getCount() - 1));
        player.getInventory().offerOrDrop(new ItemStack(HollowBlocks.FIREFLY_JAR));

        this.discard();
        return ActionResult.SUCCESS;
    }

    @Override
    public boolean isInAir() {
        return !this.isOnGround();
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) { }

    @Override
    protected float getSoundVolume() {
        return 0F;
    }

    @Override
    protected void pushAway(Entity entity) {

    }

    public static boolean canSpawn(EntityType<FireflyEntity> entityType, ServerWorldAccess worldAccess, SpawnReason spawnReason, BlockPos pos, Random random) {
        if (pos.getY() < 63) return false;
        if (worldAccess.getLightLevel(LightType.BLOCK, pos) > 9)  return false;
        return FireflyEntity.canMobSpawn(entityType, worldAccess, spawnReason, pos, random);
    }

    @Override
    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        return world.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    public boolean collidesWith(Entity other) {
        return false;
    }

    @Override
    protected void tickCramming() { }

    static class FlyRandomlyGoal extends Goal {
        private final FireflyEntity firefly;

        public FlyRandomlyGoal(FireflyEntity firefly) {
            this.firefly = firefly;
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }


        protected Vec3d getWanderTarget() {
            Vec3d rotation = firefly.getRotationVec(0.0F);
            Vec3d target = AboveGroundTargeting.find(firefly, 8, 7, rotation.x, rotation.z, MathHelper.HALF_PI, 3, 1);
            if (target != null) return target;

            return NoPenaltySolidTargeting.find(firefly, 8, 4, -2, rotation.x, rotation.z, MathHelper.HALF_PI);
        }

        @Override
        public boolean canStart() {
            return firefly.navigation.isIdle() && firefly.random.nextInt(10) == 0;
        }

        @Override
        public void start() {
            Vec3d target = this.getWanderTarget();
            if (target == null) return;
            firefly.navigation.startMovingAlong(firefly.navigation.findPathTo(BlockPos.ofFloored(target), 1), 0.5F);
        }

        @Override
        public boolean shouldContinue() {
            return firefly.navigation.isFollowingPath();
        }
    }
}
