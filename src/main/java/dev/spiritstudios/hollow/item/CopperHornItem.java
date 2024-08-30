package dev.spiritstudios.hollow.item;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.registry.HollowDataComponentRegistrar;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Instrument;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;
import java.util.Optional;

public class CopperHornItem extends Item {
    public CopperHornItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        HollowDataComponentRegistrar.CopperInstrument instrument = this.getInstrument(stack).orElse(null);
        if (instrument == null) return;

        MutableText text = Text.translatable("copper_horn.%s".formatted(instrument.asString()));
        tooltip.add(text.formatted(Formatting.GRAY));
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 80;
    }

    private static void playSound(World world, PlayerEntity player, HollowDataComponentRegistrar.CopperInstrument instrument) {
        SoundEvent sound;
        float pitch = 1.0F;
        float volume = 1.0F;
        if (player.isSneaking()) {
            sound = instrument.soundEvents.get("bass");
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
        } else if (player.getPitch() < -45) sound = instrument.soundEvents.get("call");
        else sound = instrument.soundEvents.get("melody");


        world.playSoundFromEntity(player, player, sound, SoundCategory.HOSTILE, volume, pitch);
        world.emitGameEvent(GameEvent.INSTRUMENT_PLAY, player.getPos(), GameEvent.Emitter.of(player));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        Optional<HollowDataComponentRegistrar.CopperInstrument> instrument = this.getInstrument(itemStack);
        if (instrument.isPresent()) {
            user.setCurrentHand(hand);
            playSound(world, user, instrument.get());
            user.getItemCooldownManager().set(this, 80);
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            return TypedActionResult.consume(itemStack);
        } else return TypedActionResult.fail(itemStack);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.TOOT_HORN;
    }

    private Optional<HollowDataComponentRegistrar.CopperInstrument> getInstrument(ItemStack stack) {
        HollowDataComponentRegistrar.CopperInstrument registryEntry = stack.get(HollowDataComponentRegistrar.COPPER_INSTRUMENT);
        if (registryEntry != null) return Optional.of(registryEntry);
        return Optional.empty();
    }
}
