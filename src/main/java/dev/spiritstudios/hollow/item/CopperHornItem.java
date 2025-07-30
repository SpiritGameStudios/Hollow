package dev.spiritstudios.hollow.item;

import dev.spiritstudios.hollow.component.CopperInstrument;
import dev.spiritstudios.hollow.component.CopperInstrumentComponent;
import dev.spiritstudios.hollow.component.HollowDataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Optional;

public class CopperHornItem extends Item {
    public CopperHornItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        Optional<? extends RegistryEntry<CopperInstrument>> optional = this.getInstrument(itemStack, user.getRegistryManager());
        if (optional.isEmpty()) return ActionResult.FAIL;
        CopperInstrument instrument = optional.get().value();

        user.setCurrentHand(hand);
        playSound(world, user, instrument);

        user.getItemCooldownManager().set(itemStack, MathHelper.floor(instrument.useDuration() * 20.0F));
        user.incrementStat(Stats.USED.getOrCreateStat(this));

        return ActionResult.CONSUME;
    }

    // region Settings
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.TOOT_HORN;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return this.getInstrument(stack, user.getRegistryManager()).map(instrument ->
                MathHelper.floor(instrument.value().useDuration() * 20.0F))
                .orElse(0);
    }
    // endregion

    private Optional<RegistryEntry<CopperInstrument>> getInstrument(ItemStack stack, RegistryWrapper.WrapperLookup registries) {
        CopperInstrumentComponent instrumentComponent = stack.get(HollowDataComponentTypes.COPPER_INSTRUMENT);
        return instrumentComponent != null ? instrumentComponent.getInstrument(registries) : Optional.empty();
    }

    private static void playSound(World world, PlayerEntity player, CopperInstrument instrument) {
        RegistryEntry<SoundEvent> sound;

        if (player.isSneaking()) sound = instrument.bass();
		else if (player.getPitch() < -45) sound = instrument.call();
        else sound = instrument.melody();


        world.playSoundFromEntity(
                player, player,
                sound.value(), SoundCategory.RECORDS,
                instrument.range() / 16.0F, 1.0F
        );

        world.emitGameEvent(GameEvent.INSTRUMENT_PLAY, player.getPos(), GameEvent.Emitter.of(player));
    }
}
