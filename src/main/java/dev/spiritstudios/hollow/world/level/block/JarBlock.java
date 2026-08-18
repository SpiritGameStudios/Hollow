package dev.spiritstudios.hollow.world.level.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.world.level.block.entity.JarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class JarBlock extends BaseEntityBlock {
    public static final MapCodec<JarBlock> CODEC = simpleCodec(JarBlock::new);
    public static final VoxelShape SHAPE = Shapes.or(
            box(5.5, 12, 5.5, 10.5, 15, 10.5),
            box(3.5, 0, 3.5, 12.5, 14, 4.5),
            box(3.5, 0, 4.5, 4.5, 14, 11.5),
            box(3.5, 0, 11.5, 12.5, 14, 12.5),
            box(11.5, 0, 4.5, 12.5, 14, 11.5),
            box(4.5, 13, 4.5, 11.5, 14, 11.5),
            box(4.5, 0, 4.5, 11.5, 1, 11.5)
    );

    public JarBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new JarBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide()) return InteractionResult.SUCCESS;

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof JarBlockEntity jarBlockEntity) jarBlockEntity.use(world, pos, player, hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean moved) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof JarBlockEntity jarBlockEntity) Containers.dropContents(world, pos, jarBlockEntity);

        super.affectNeighborsAfterRemoval(state, world, pos, moved);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
