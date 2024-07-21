package dev.callmeecho.hollow.main.item;

import dev.callmeecho.hollow.main.block.GiantLilyPadBlock;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PlaceableOnWaterItem;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class GiantLilyPadItem extends PlaceableOnWaterItem {
    public GiantLilyPadItem(Block block, Settings settings) { super(block, settings); }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        BlockHitResult rayHit = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
        if (rayHit.getType() == BlockHitResult.Type.MISS) return TypedActionResult.pass(stack);
        if (rayHit.getType() != BlockHitResult.Type.BLOCK) return TypedActionResult.fail(stack);

        BlockPos pos = rayHit.getBlockPos();
        Direction side = rayHit.getSide();
        if (
                !world.canPlayerModifyAt(user, pos) || !user.canPlaceOn(pos, side, stack)
                || !world.canPlayerModifyAt(user, pos.east()) || !user.canPlaceOn(pos.east(), side, stack)
                || !world.canPlayerModifyAt(user, pos.south()) || !user.canPlaceOn(pos.south(), side, stack)
                || !world.canPlayerModifyAt(user, pos.east().south()) || !user.canPlaceOn(pos.east().south(), side, stack)
                ) return TypedActionResult.fail(stack);

        BlockPos above = pos.up();
        BlockState state = world.getBlockState(pos);
        FluidState fluidState = world.getFluidState(pos);

        if ((!fluidState.isOf(Fluids.WATER) && !state.isIn(BlockTags.ICE)) || !world.isAir(above)
                || (!world.getFluidState(pos.east()).isOf(Fluids.WATER) && !world.getBlockState(pos.east()).isIn(BlockTags.ICE)) || !world.isAir(above.east())
                || (!world.getFluidState(pos.south()).isOf(Fluids.WATER) && !world.getBlockState(pos.south()).isIn(BlockTags.ICE)) || !world.isAir(above.south())
                || (!world.getFluidState(pos.east().south()).isOf(Fluids.WATER) && !world.getBlockState(pos.east().south()).isIn(BlockTags.ICE)) || !world.isAir(above.east().south())
        ) return TypedActionResult.fail(stack);

        BlockState lilypadState = this.getBlock().getDefaultState().with(GiantLilyPadBlock.FACING, user.getHorizontalFacing());
        world.setBlockState(above, lilypadState.with(GiantLilyPadBlock.PIECE, GiantLilyPadBlock.Piece.NORTH_WEST), 11);
        world.setBlockState(above.east(), lilypadState.with(GiantLilyPadBlock.PIECE, GiantLilyPadBlock.Piece.NORTH_EAST), 11);
        world.setBlockState(above.south(), lilypadState.with(GiantLilyPadBlock.PIECE, GiantLilyPadBlock.Piece.SOUTH_WEST), 11);
        world.setBlockState(above.east().south(), lilypadState.with(GiantLilyPadBlock.PIECE, GiantLilyPadBlock.Piece.SOUTH_EAST), 11);

        if (user instanceof ServerPlayerEntity serverPlayer) {
            Criteria.PLACED_BLOCK.trigger(serverPlayer, above, stack);
            Criteria.PLACED_BLOCK.trigger(serverPlayer, above.east(), stack);
            Criteria.PLACED_BLOCK.trigger(serverPlayer, above.east().south(), stack);
            Criteria.PLACED_BLOCK.trigger(serverPlayer, above.south(), stack);
        }

        if (!user.getAbilities().creativeMode) stack.decrement(1);
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        world.playSound(user, pos, SoundEvents.BLOCK_LILY_PAD_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);

        return TypedActionResult.success(stack);
    }
}
