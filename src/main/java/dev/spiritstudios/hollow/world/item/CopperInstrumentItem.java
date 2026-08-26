package dev.spiritstudios.hollow.world.item;

import dev.spiritstudios.hollow.core.component.HollowDataComponents;
import dev.spiritstudios.hollow.world.item.component.CopperInstrumentComponent;
import net.minecraft.SharedConstants;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class CopperInstrumentItem extends Item {
	public CopperInstrumentItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack itemStack = player.getItemInHand(hand);
		CopperInstrumentComponent component = itemStack.get(HollowDataComponents.COPPER_INSTRUMENT);

		if (component != null) {
			CopperInstrument instrument = component.instrument().value();

			player.startUsingItem(hand);
			play(level, player, instrument);

			player.getCooldowns()
				.addCooldown(itemStack, Mth.floor(instrument.useDuration() * SharedConstants.TICKS_PER_SECOND));
			player.awardStat(Stats.ITEM_USED.get(this));

			return InteractionResult.CONSUME;
		} else {
			return InteractionResult.FAIL;
		}
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity user) {
		CopperInstrumentComponent component = stack.get(HollowDataComponents.COPPER_INSTRUMENT);

		if (component != null) {
			CopperInstrument instrument = component.instrument().value();
			return Mth.floor(instrument.useDuration() * SharedConstants.TICKS_PER_SECOND);
		} else {
			return 0;
		}
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		return ItemUseAnimation.TOOT_HORN;
	}

	private static void play(Level level, Player player, CopperInstrument instrument) {
		// IF shifting, bass
		// ELSE IF looking up, call
		// ELSE melody
		SoundEvent soundEvent = player.isShiftKeyDown() ?
			instrument.bass().value() :
			player.getXRot() < -45 ?
				instrument.call().value() :
				instrument.melody().value();

		final float volume = instrument.range() / 16.0F;

		level.playSound(player, player, soundEvent, SoundSource.RECORDS, volume, 1.0F);
		level.gameEvent(GameEvent.INSTRUMENT_PLAY, player.position(), GameEvent.Context.of(player));
	}
}
