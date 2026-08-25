package dev.spiritstudios.hollow.client.color.item;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.world.level.block.FireflyJarBlock;
import net.minecraft.SharedConstants;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.awt.Color;

public class Jeb implements ItemTintSource {
	public static final MapCodec<Jeb> MAP_CODEC = MapCodec.unit(new Jeb());

	private static final float JEB_SATURATION = 0.5F;
	private static final int TIME = SharedConstants.TICKS_PER_SECOND * 5;

	@Override
	public int calculate(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
		Component customName = itemStack.getCustomName();

		if (level == null || customName == null || !customName.getString().equals(FireflyJarBlock.JEB_NAME))
			return CommonColors.WHITE;

		return getColor((float) (level.getGameTime() % TIME) / TIME + RandomSource.create(itemStack.hashCode()).nextFloat());
	}

	public static int getColor(float delta) {
		return Color.getHSBColor(Mth.frac(delta), JEB_SATURATION, 1.0F).getRGB() | 0xFF000000;
	}

	@Override
	public MapCodec<? extends ItemTintSource> type() {
		return MAP_CODEC;
	}
}
