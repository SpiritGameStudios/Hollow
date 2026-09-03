package dev.spiritstudios.hollow.util;

import net.minecraft.SharedConstants;

public final class TickUtils {
	private TickUtils() {}

	public static int from(int hrs, int mins, int secs) {
		return fromHrs(hrs) + fromMins(mins) + fromSecs(secs);
	}

	public static int fromHrs(int hrs) {
		return fromMins(hrs);
	}

	public static int fromMins(int mins) {
		return SharedConstants.TICKS_PER_MINUTE * mins;
	}

	public static int fromSecs(int secs) {
		return SharedConstants.TICKS_PER_SECOND * secs;
	}
}
