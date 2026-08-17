package dev.spiritstudios.hollow.world.item;

import dev.spiritstudios.hollow.data.component.CopperInstrument;
import dev.spiritstudios.hollow.data.component.CopperInstrumentComponent;
import dev.spiritstudios.hollow.data.component.HollowDataComponents;
import net.minecraft.SharedConstants;
import net.minecraft.core.Holder;
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

public class CopperHornItem extends Item {
    public CopperHornItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult use(Level level, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);

        CopperInstrumentComponent component = itemStack.get(HollowDataComponents.COPPER_INSTRUMENT);
        if (component == null) return InteractionResult.FAIL;

        CopperInstrument instrument = component.instrument().value();

        user.startUsingItem(hand);
        playSound(level, user, instrument);

        user.getCooldowns().addCooldown(itemStack, Mth.floor(instrument.useDuration() * SharedConstants.TICKS_PER_SECOND));
        user.awardStat(Stats.ITEM_USED.get(this));

        return InteractionResult.CONSUME;
    }

    // region Settings
    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.TOOT_HORN;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        CopperInstrumentComponent component = stack.get(HollowDataComponents.COPPER_INSTRUMENT);
        if (component == null) return 0;

        CopperInstrument instrument = component.instrument().value();

        return Mth.floor(instrument.useDuration() * SharedConstants.TICKS_PER_SECOND);
    }
    // endregion

    private static void playSound(Level world, Player player, CopperInstrument instrument) {
        Holder<SoundEvent> sound;

        if (player.isShiftKeyDown()) sound = instrument.bass();
		else if (player.getXRot() < -45) sound = instrument.call();
        else sound = instrument.melody();


        world.playSound(
                player, player,
                sound.value(), SoundSource.RECORDS,
                instrument.range() / 16.0F, 1.0F
        );

        world.gameEvent(GameEvent.INSTRUMENT_PLAY, player.position(), GameEvent.Context.of(player));
    }
}
