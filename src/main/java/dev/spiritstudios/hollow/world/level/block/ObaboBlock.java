package dev.spiritstudios.hollow.world.level.block;

import com.mojang.datafixers.util.Function4;
import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.world.level.block.entity.HollowBlockEntityTypes;
import dev.spiritstudios.hollow.world.level.block.entity.pot.ObaboBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.regex.Pattern;

public class ObaboBlock extends EchoingPotBlock {
	public static final Pattern DIRE_CURSE = Pattern.compile("[oO0](?:[^0-9a-zA-Z]*|\\s)*[bB](?:[^0-9a-zA-Z]*|\\s)*[aA](?:[^0-9a-zA-Z]*|\\s)[bB](?:[^0-9a-zA-Z]*|\\s)*[oO0]");
	public static final MapCodec<dev.spiritstudios.hollow.world.level.block.ObaboBlock> CODEC = simpleCodec(dev.spiritstudios.hollow.world.level.block.ObaboBlock::new);
	public static Function4<BlockState, Level, BlockPos, RandomSource, Boolean> cb = null;

	public static void invokeCurse(ServerPlayer player) {
//			player.damage(player.getDamageSources().create(HollowDamageTypes.DIRE_CURSE), 100);
	}

	public ObaboBlock(Properties settings) {
		super(settings);
	}

	@Override
	protected MapCodec<dev.spiritstudios.hollow.world.level.block.ObaboBlock> codec() {
		return CODEC;
	}

	protected static <E extends BlockEntity, A extends BlockEntity> @Nullable BlockEntityTicker<A> createTickerHelper(
		final BlockEntityType<A> actual, final BlockEntityType<E> expected, final @Nullable BlockEntityTicker<? super E> ticker
	) {
		return expected == actual ? (BlockEntityTicker<A>) ticker : null;
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
		return createTickerHelper(type, HollowBlockEntityTypes.OBABO, ObaboBlockEntity::tick);
	}

	@Override
	public @org.jetbrains.annotations.Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ObaboBlockEntity(pos, state);
	}
}
