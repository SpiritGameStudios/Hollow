package dev.spiritstudios.hollow;

import com.bawnorton.mixinsquared.api.MixinCanceller;

import java.util.List;

public class SpecterMixinCanceller implements MixinCanceller {
    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        return mixinClassName.equals("dev.spiritstudios.specter.mixin.core.client.NarratorMixin");
    }
}
