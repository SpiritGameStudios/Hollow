package dev.spiritstudios.hollow.item;

import dev.spiritstudios.hollow.component.CopperInstrument;
import dev.spiritstudios.hollow.registry.HollowDataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;
import java.util.Optional;

public class CopperHornItem extends Item {
    public CopperHornItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        Optional<CopperInstrument> instrument = this.getInstrument(itemStack);
        if (instrument.isEmpty()) return TypedActionResult.fail(itemStack);

        user.setCurrentHand(hand);
        playSound(world, user, instrument.get());

        user.getItemCooldownManager().set(itemStack.getItem(), 80);
        user.incrementStat(Stats.USED.getOrCreateStat(this));

        return TypedActionResult.consume(itemStack);
    }

    // region Settings
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.TOOT_HORN;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 80;
    }
    // endregion

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        CopperInstrument instrument = this.getInstrument(stack).orElse(null);
        if (instrument == null) return;

        MutableText text = Text.translatable("copper_horn.%s".formatted(instrument.asString()));
        tooltip.add(text.formatted(Formatting.GRAY));
    }

    private Optional<CopperInstrument> getInstrument(ItemStack stack) {
        return Optional.ofNullable(stack.get(HollowDataComponentTypes.COPPER_INSTRUMENT));
    }

    private static void playSound(World world, PlayerEntity player, CopperInstrument instrument) {
        SoundEvent sound;
        float pitch = 1.0F;
        float volume = 1.0F;
        if (player.isSneaking()) {
            sound = instrument.bass;
            switch (instrument) {
                case FEARLESS_RIVER_GIFT -> {
                    pitch = 1.07F;
                    volume = 1.1F;
                }
                case SWEET_MOON_LOVE -> {
                    pitch = 1.7F;
                    volume = 1.7F;
                }
            }
        } else if (player.getPitch() < -45) sound = instrument.call;
        else sound = instrument.melody;


        world.playSoundFromEntity(player, player, sound, SoundCategory.PLAYERS, volume, pitch);
        world.emitGameEvent(GameEvent.INSTRUMENT_PLAY, player.getPos(), GameEvent.Emitter.of(player));
    }
}
