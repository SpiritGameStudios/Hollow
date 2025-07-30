package dev.spiritstudios.hollow.block.entity;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.block.ScreamingVaseBlock;
import dev.spiritstudios.hollow.block.HollowBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class EchoingVaseBlockEntity extends BlockEntity {
	public static int TILT_TIME = 10;
	public static int FALL_TIME = 20;

	public int activeTime = 0;
	public long lastWobbleTime;
	public Entity fallCauser;
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

	public void setFalling(Direction dir, boolean top, World world, BlockPos pos, @Nullable Entity fallCauser) {
		this.fallTime = 1;
		this.fallDirection = dir;
		this.fallCauser = fallCauser;
		if (top) {
			BlockEntity be = world.getBlockEntity(pos.up());
			if (be instanceof EchoingVaseBlockEntity echoing) {
				echoing.setFalling(dir, false, world, pos, fallCauser);
			} else {
				Hollow.LOGGER.error("Missing top block entity for echoing vase at {}", pos.up());
			}
		}
	}

	public static void tick(World world, BlockPos pos, BlockState state, EchoingVaseBlockEntity entity) {
		if (entity.fallTime == 0) return;
		entity.fallTime++;

		if (entity.fallTime <= EchoingVaseBlockEntity.FALL_TIME || entity.fallen) return;

		world.playSoundAtBlockCenterClient(pos, SoundEvents.BLOCK_DECORATED_POT_SHATTER, SoundCategory.BLOCKS, 1, 1, true);

		world.setBlockState(pos.up(), Blocks.AIR.getDefaultState());
		world.setBlockState(pos, Blocks.AIR.getDefaultState());

		world.addBlockBreakParticles(pos.offset(entity.fallDirection), state);
		world.addBlockBreakParticles(pos.offset(entity.fallDirection, 2), state);

		ScreamingVaseBlock.onBreakLower(world, pos, state, entity.fallCauser);
	}

	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!state.isOf(HollowBlocks.SCREAMING_VASE) ||
				this.fallTime != 0 ||
				state.get(Properties.DOUBLE_BLOCK_HALF).equals(DoubleBlockHalf.UPPER) ||
				!world.getBlockState(pos.up()).isOf(HollowBlocks.SCREAMING_VASE)
		) return;

		Direction fallDirection = Direction.getFacing(pos.toCenterPos().subtract(entity.getPos()));

		BlockPos lowerPos = pos.offset(fallDirection);
		BlockPos upperPos = pos.offset(fallDirection, 2);

		if (!world.getBlockState(lowerPos).isAir() || !world.getBlockState(upperPos).isAir()) return;

		this.setFalling(Direction.getFacing(pos.toCenterPos().subtract(entity.getPos())), true, world, pos, entity);
		if (!world.isClient) {
			((ServerWorld) world).getChunkManager().markForUpdate(this.getPos());
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
		if (this.fallDirection != null && this.fallTime > 0) {
			nbt.putInt("FallDir", this.fallDirection.ordinal());
		}
		super.writeNbt(nbt, registryLookup);
	}

	@Override
	public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		activeTime = nbt.getInt("ActiveTime", 0);

		nbt.getInt("FallDir").ifPresent(fallDir -> {
			this.fallDirection = Direction.values()[fallDir];
			if (this.world != null && this.fallTime == 0 && this.getCachedState().get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
				this.setFalling(this.fallDirection, true, this.world, this.getPos(), null);
			}
		});
	}
	// endregion
}