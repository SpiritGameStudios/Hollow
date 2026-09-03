package dev.spiritstudios.hollow.world.level.block.pot;

import com.mojang.datafixers.util.Function4;
import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.world.level.block.entity.HollowBlockEntityTypes;
import dev.spiritstudios.hollow.world.level.block.entity.pot.ObaboBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.regex.Pattern;

public class ObaboBlock extends EchoingPotBlock {
	public static final Pattern DIRE_CURSE = Pattern.compile("[oO0](?:[^0-9a-zA-Z]*|\\s)*[bB](?:[^0-9a-zA-Z]*|\\s)*[aA](?:[^0-9a-zA-Z]*|\\s)[bB](?:[^0-9a-zA-Z]*|\\s)*[oO0]");
	public static final MapCodec<ObaboBlock> CODEC = simpleCodec(ObaboBlock::new);
	public static Function4<BlockState, Level, BlockPos, RandomSource, Boolean> cb = null;

	public static void invokeCurse(ServerPlayer player) {
//			player.damage(player.getDamageSources().create(HollowDamageTypes.DIRE_CURSE), 100);
	}

	public ObaboBlock(Properties settings) {
		super(settings);
	}

	@Override
	protected MapCodec<ObaboBlock> codec() {
		return CODEC;
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
		return BaseEntityBlock.createTickerHelper(type, HollowBlockEntityTypes.OBABO, ObaboBlockEntity::tick);
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ObaboBlockEntity(pos, state);
	}
}
