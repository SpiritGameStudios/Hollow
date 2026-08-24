package dev.spiritstudios.hollow.world.level.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.core.particles.HollowParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class FireflyJarBlock extends BaseJarBlock {
	public static final MapCodec<FireflyJarBlock> CODEC = simpleCodec(FireflyJarBlock::new);
	public static final BooleanProperty JEB = BooleanProperty.create("jeb");

    public FireflyJarBlock(BlockBehaviour.Properties settings) {
        super(settings);
		this.registerDefaultState(this.defaultBlockState().setValue(JEB, false));
    }

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState stateForPlacement = super.getStateForPlacement(context);
		assert stateForPlacement != null;

		return stateForPlacement.setValue(JEB, hasJebName(context.getItemInHand()));
	}

	@Override
	protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (itemStack.is(Items.NAME_TAG) && !state.getValue(JEB) && hasJebName(itemStack)) {
			level.setBlock(pos, state.setValue(JEB, true), UPDATE_CLIENTS);
			itemStack.shrink(1);

			return InteractionResult.SUCCESS;
		}

		return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
	}

	private static boolean hasJebName(ItemStack itemStack) {
		Component customName = itemStack.getCustomName();
		return customName != null && customName.getString().equals("jeb_");
	}

	@Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		AABB shape = BODY_SHAPE.bounds().inflate(-0.0625);

		float x = Mth.randomBetween(random, (float) shape.minX, (float) shape.maxX);
		float y = Mth.randomBetween(random, (float) shape.minY, (float) shape.maxY);
		float z = Mth.randomBetween(random, (float) shape.minZ, (float) shape.maxZ);

		if (state.getValue(HANGING))
			y += HANGING_SHAPE_Y_DIFF;

		level.addParticle(HollowParticleTypes.JAR_FIREFLY, pos.getX() + x, pos.getY() + y, pos.getZ() + z, state.getValue(JEB) ? 1.0 : 0.0, 0.0, 0.0);
	}

	@Override
	protected MapCodec<? extends Block> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(JEB));
	}
}
