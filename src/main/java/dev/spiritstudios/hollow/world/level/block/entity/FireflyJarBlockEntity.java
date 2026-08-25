package dev.spiritstudios.hollow.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Nameable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class FireflyJarBlockEntity extends BlockEntity implements Nameable {
	private static final String JEB_NAME = "jeb_";
	private static final String CUSTOM_NAME_KEY = "CustomName";
	private static final Component DEFAULT_NAME = Component.translatable("block.hollow.firefly_jar");

	@Nullable private Component name = null;

	public FireflyJarBlockEntity(BlockPos worldPosition, BlockState blockState) {
		super(HollowBlockEntityTypes.FIREFLY_JAR, worldPosition, blockState);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return this.saveWithoutMetadata(registries);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.storeNullable(CUSTOM_NAME_KEY, ComponentSerialization.CODEC, this.name);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.name = parseCustomNameSafe(input, CUSTOM_NAME_KEY);
	}

	@Override
	protected void applyImplicitComponents(DataComponentGetter components) {
		super.applyImplicitComponents(components);
		this.name = components.get(DataComponents.CUSTOM_NAME);
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder components) {
		super.collectImplicitComponents(components);
		components.set(DataComponents.CUSTOM_NAME, this.name);
	}

	@Override
	public void removeComponentsFromTag(ValueOutput output) {
		output.discard(CUSTOM_NAME_KEY);
	}

	@Override
	public Component getName() {
		return this.name != null ? this.name : DEFAULT_NAME;
	}

	@Override
	public @Nullable Component getCustomName() {
		return this.name;
	}

	public boolean isJeb() {
		return this.getPlainTextName().equals(JEB_NAME);
	}
}
