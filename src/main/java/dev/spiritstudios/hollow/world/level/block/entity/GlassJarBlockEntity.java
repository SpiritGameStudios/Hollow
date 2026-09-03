package dev.spiritstudios.hollow.world.level.block.entity;

import com.mojang.logging.LogUtils;
import dev.spiritstudios.hollow.advancements.triggers.HollowCriteriaTriggers;
import dev.spiritstudios.hollow.tags.HollowBlockItemTags;
import dev.spiritstudios.hollow.tags.HollowItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.slf4j.Logger;

public class GlassJarBlockEntity extends NoMenuContainerBlockEntity {
	private static final Logger LOGGER = LogUtils.getLogger();

    private final NonNullList<ItemStack> items = NonNullList.withSize(15, ItemStack.EMPTY);

    public GlassJarBlockEntity(BlockPos pos, BlockState state) {
        super(HollowBlockEntityTypes.GLASS_JAR, pos, state);
    }

    public boolean tryUse(Player player, InteractionHand hand) {
		ItemStack itemInHand = player.getItemInHand(hand);
		return itemInHand.isEmpty() ? this.tryTakeItem(player, hand) : this.tryInsertItem(player, itemInHand);
    }

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	private boolean tryTakeItem(Player player, InteractionHand hand) {
		int slot = -1;

		for (int i = this.getContainerSize() - 1; i >= 0; i--) {
			if (!this.getItem(i).isEmpty()) {
				slot = i;
				break;
			}
		}

		if (slot != -1) {
			player.setItemInHand(hand, this.getItem(slot).copy());
			this.removeItem(slot, 1);

			return true;
		}

		return false;
	}

	private static boolean canInsertItem(ItemStack stack) {
		if (stack.is(HollowItemTags.CANNOT_PUT_IN_JAR)) {
			return false;
		}

		if (stack.is(HollowItemTags.PUT_IN_JAR_OVERRIDE)) {
			return true;
		}

		return !(stack.getItem() instanceof BlockItem);
	}

	private boolean tryInsertItem(Player player, ItemStack itemInHand) {
		if (!canInsertItem(itemInHand)) {
			return false;
		}

		int slot = -1;

		for (int i = 0; i < this.getContainerSize(); i++) {
			if (this.getItem(i).isEmpty()) {
				slot = i;
				break;
			}
		}

		if (slot != -1) {
			if (player instanceof ServerPlayer serverPlayer && itemInHand.is(HollowBlockItemTags.GLASS_JARS.item())) {
				HollowCriteriaTriggers.PLAYER_INSERT_JAR_IN_JAR.trigger(serverPlayer);
			}

			this.setItem(slot, itemInHand.split(1));

			return true;
		}

		return false;
	}

	public float getFillProgress() {
		return (float) this.count() / this.getContainerSize();
	}

	@Override
	public NonNullList<ItemStack> getItems() {
		return this.items;
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
		try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(this.problemPath(), LOGGER)) {
			TagValueOutput output = TagValueOutput.createWithContext(reporter, registryLookup);
			ContainerHelper.saveAllItems(output, this.items, true);
			return output.buildResult();
		}
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void setChanged() {
		super.setChanged();
		if (this.level != null) {
			BlockState blockState = this.getBlockState();
			this.level.sendBlockUpdated(this.getBlockPos(), blockState, blockState, Block.UPDATE_ALL);
		}
	}

	@Override
	protected void loadAdditional(final ValueInput input) {
		super.loadAdditional(input);
		this.clearContent();
		ContainerHelper.loadAllItems(input, this.items);
	}

	@Override
	protected void saveAdditional(final ValueOutput output) {
		super.saveAdditional(output);
		ContainerHelper.saveAllItems(output, this.items, true);
	}

	@Override
	public void preRemoveSideEffects(final BlockPos pos, final BlockState state) {
		if (this.level != null) {
			Containers.dropContents(this.level, pos, this.items);
		}
	}
}
