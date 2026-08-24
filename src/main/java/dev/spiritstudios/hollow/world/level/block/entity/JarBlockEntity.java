package dev.spiritstudios.hollow.world.level.block.entity;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Clearable;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class JarBlockEntity extends BlockEntity implements Clearable {
    private final NonNullList<ItemStack> items = NonNullList.withSize(17, ItemStack.EMPTY);

    public JarBlockEntity(BlockPos pos, BlockState state) {
        super(HollowBlockEntityTypes.JAR, pos, state);
    }

    public void use(Level level, BlockPos pos, Player player, InteractionHand hand) {
		ItemStack itemInHand = player.getItemInHand(hand);

	    if (itemInHand.isEmpty())
			this.tryTakeItem(player, hand);
		else
			this.tryInsertItem(level, player, pos, itemInHand, hand);
    }

	private void tryTakeItem(Player player, InteractionHand hand) {
		int slot = -1;

		for (int i = this.items.size() - 1; i >= 0; i--) {
			if (!this.items.get(i).isEmpty()) {
				slot = i;
				break;
			}
		}

		if (slot != -1)
			this.swapItem(player, slot, ItemStack.EMPTY, this.items.get(slot).copy(), hand);
	}

	private void tryInsertItem(Level level, Player player, BlockPos pos, ItemStack itemInHand, InteractionHand hand) {
		int slot = -1;

		for (int i = 0; i < this.items.size(); i++) {
			if (this.items.get(i).isEmpty()) {
				slot = i;
				break;
			}
		}

		if (slot == -1)
			return;

		this.swapItem(player, slot, itemInHand, ItemStack.EMPTY, hand);

		if (!level.isClientSide()) {
			level.playSound(
				null,
				pos,
				SoundEvents.ITEM_PICKUP,
				SoundSource.PLAYERS,
				0.2F,
				player.getRandom().triangle(1.0F, 0.7F) * 2.0F
			);
		}
	}

	public NonNullList<ItemStack> getItems() {
		return this.items;
	}

	private void swapItem(Player player, int slot, ItemStack inJar, ItemStack inHand, InteractionHand hand) {
		this.items.set(slot, inJar);
		this.markUpdated();

		player.setItemInHand(hand, inHand);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
		try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(this.problemPath(), Hollow.LOGGER)) {
			TagValueOutput output = TagValueOutput.createWithContext(reporter, registryLookup);
			ContainerHelper.saveAllItems(output, this.items, true);
			return output.buildResult();
		}
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	private void markUpdated() {
		this.setChanged();
		Level level = this.getLevel();
		if (level != null) {
			BlockState blockState = this.getBlockState();
			level.sendBlockUpdated(this.getBlockPos(), blockState, blockState, Block.UPDATE_ALL);
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

	@Override
	protected void applyImplicitComponents(final DataComponentGetter components) {
		super.applyImplicitComponents(components);
		components.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(this.getItems());
	}

	@Override
	protected void collectImplicitComponents(final DataComponentMap.Builder components) {
		super.collectImplicitComponents(components);
		components.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.getItems()));
	}

	@Override
	public void removeComponentsFromTag(final ValueOutput output) {
		output.discard("Items");
	}

	@Override
	public void clearContent() {
		this.items.clear();
	}

}
