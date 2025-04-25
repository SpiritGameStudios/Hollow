package dev.spiritstudios.hollow;

import dev.doublekekse.area_lib.component.AreaDataComponent;
import dev.doublekekse.area_lib.data.AreaSavedData;
import net.minecraft.nbt.NbtCompound;

public class HollowShowcaseComponent implements AreaDataComponent {
    private boolean jarsEnabled;
    private boolean potsEnabled;

    public HollowShowcaseComponent(boolean jarsEnabled, boolean potsEnabled) {
        this.jarsEnabled = jarsEnabled;
        this.potsEnabled = potsEnabled;
    }

    public HollowShowcaseComponent() {
        this.jarsEnabled = false;
        this.potsEnabled = false;
    }

    @Override
    public void load(AreaSavedData areaSavedData, NbtCompound nbtCompound) {
        this.jarsEnabled = nbtCompound.getBoolean("jars_enabled");
        this.potsEnabled = nbtCompound.getBoolean("pots_enabled");
    }

    @Override
    public NbtCompound save() {
        NbtCompound compound = new NbtCompound();

        compound.putBoolean("jars_enabled", jarsEnabled);
        compound.putBoolean("pots_enabled", potsEnabled);

        return compound;
    }

    public boolean jarsEnabled() {
        return jarsEnabled;
    }

    public boolean potsEnabled() {
        return potsEnabled;
    }
}
