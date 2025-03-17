package dev.spiritstudios.hollow.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.spiritstudios.specter.impl.serialization.text.TextTranslationSupplier;
import net.minecraft.text.Text;
import net.minecraft.util.Language;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import xyz.nucleoid.server.translations.impl.language.SystemDelegatedLanguage;

@Mixin(SystemDelegatedLanguage.class)
public abstract class SystemDelegatedLanguageMixin implements TextTranslationSupplier {
    @Shadow @Final private Language vanilla;

    @ModifyReturnValue(method = "get*", at = @At("RETURN"))
    private String get(String original) {
        if (!(vanilla instanceof TextTranslationSupplier supplier))
            return null;

        Text text = supplier.specter_serialization$getText(original);
        return text != null ? text.getString() : original;
    }

    @Override
    public Text specter_serialization$getText(String key) {
        if (!(vanilla instanceof TextTranslationSupplier supplier))
            return null;

        return supplier.specter_serialization$getText(key);
    }
}
