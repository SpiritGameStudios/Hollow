package dev.spiritstudios.hollow.world.level.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.world.level.block.entity.HollowBlockEntityTypes;
import dev.spiritstudios.hollow.world.level.block.entity.GlassJarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ListBackedContainer;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class GlassJarBlock extends BaseJarBlock implements EntityBlock {
    public static final MapCodec<GlassJarBlock> CODEC = simpleCodec(GlassJarBlock::new);

    public GlassJarBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GlassJarBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		Optional<GlassJarBlockEntity> blockEntity = world.getBlockEntity(pos, HollowBlockEntityTypes.GLASS_JAR);
		return blockEntity.isPresent() && blockEntity.get().tryUse(world, pos, player, hand) ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
	}

    @Override
    protected MapCodec<GlassJarBlock> codec() {
        return CODEC;
    }

	@Override
	protected boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
		return level.getBlockEntity(pos, HollowBlockEntityTypes.GLASS_JAR).map(ListBackedContainer::count).orElse(0);
	}
}
