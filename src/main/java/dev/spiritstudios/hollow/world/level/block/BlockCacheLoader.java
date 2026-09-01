package dev.spiritstudios.hollow.world.level.block;

import com.google.common.cache.CacheLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

// Public copy of BlockPattern.BlockCacheLoader
public class BlockCacheLoader extends CacheLoader<BlockPos, BlockInWorld> {
	private final LevelReader level;
	private final boolean loadChunks;

	public BlockCacheLoader(LevelReader level, boolean loadChunks) {
		this.level = level;
		this.loadChunks = loadChunks;
	}

	public BlockInWorld load(final BlockPos key) {
		return new BlockInWorld(this.level, key, this.loadChunks);
	}
}
