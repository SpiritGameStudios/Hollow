package dev.spiritstudios.hollow.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.LilyPadBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = LilyPadBlock.class, priority = 1500)
@Implements(@Interface(iface = BonemealableBlock.class, prefix = "hollow$"))
public abstract class LilyPadBlockMixin {
	public boolean hollow$isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
		return false;
	}

	public boolean hollow$isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
		return false;
	}

	public void hollow$performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {}

	@SuppressWarnings({ "MixinAnnotationTarget", "UnresolvedMixinReference" })
	@WrapMethod(method = "isValidBonemealTarget")
    private boolean wrapIsValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, Operation<Boolean> original) {
        return original.call(level, pos, state) || state.is(Blocks.LILY_PAD);
    }

	@SuppressWarnings({ "MixinAnnotationTarget", "UnresolvedMixinReference" })
	@WrapMethod(method = "isBonemealSuccess")
    private boolean wrapIsBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state, Operation<Boolean> original) {
        return true;
    }

	@SuppressWarnings({ "MixinAnnotationTarget", "UnresolvedMixinReference" })
	@WrapMethod(method = "performBonemeal")
    private void wrapPerformBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state, Operation<Void> original) {
		level.setBlockAndUpdate(pos, HollowBlocks.FLOWERING_LILY_PAD.defaultBlockState());
		original.call(level, random, pos, state);
    }
}
