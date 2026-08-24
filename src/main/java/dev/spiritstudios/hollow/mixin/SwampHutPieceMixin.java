package dev.spiritstudios.hollow.mixin;

import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.structures.SwampHutPiece;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SwampHutPiece.class)
public abstract class SwampHutPieceMixin extends ScatteredFeaturePiece {
	protected SwampHutPieceMixin(StructurePieceType type, CompoundTag tag) {
		super(type, tag);
	}

	@Inject(method = "postProcess", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/Blocks;POTTED_RED_MUSHROOM:Lnet/minecraft/world/level/block/Block;", opcode = Opcodes.GETSTATIC))
	private void addFireflyJar(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos, CallbackInfo ci) {
		this.placeBlock(level, HollowBlocks.FIREFLY_JAR.defaultBlockState(), random.nextBoolean() ? 1 : 5, 3, 4, chunkBB);
	}
}
