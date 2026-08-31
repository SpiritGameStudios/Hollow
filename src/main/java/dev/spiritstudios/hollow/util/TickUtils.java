package dev.spiritstudios.hollow.util;

import net.minecraft.SharedConstants;

public final class TickUtils {
	private TickUtils() {}

	public static int ticksFromMinsAndSecs(int mins, int secs) {
		return ticksFromMins(mins) + ticksFromSecs(secs);
	}

	public static int ticksFromMins(int mins) {
		return SharedConstants.TICKS_PER_MINUTE * mins;
	}

	public static int ticksFromSecs(int secs) {
		return SharedConstants.TICKS_PER_SECOND * secs;
	}
}
