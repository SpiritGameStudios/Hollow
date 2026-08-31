package dev.spiritstudios.hollow.world.entity.vehicle;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class FurnaceBoat extends AbstractFurnaceBoat {
	public FurnaceBoat(EntityType<? extends AbstractFurnaceBoat> type, Level level, Supplier<Item> dropItem) {
		super(type, level, dropItem);
	}

	@Override
	protected double rideHeight(final EntityDimensions dimensions) {
		return dimensions.height() / 3.0F;
	}
}
