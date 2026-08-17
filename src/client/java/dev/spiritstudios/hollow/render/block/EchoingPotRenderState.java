package dev.spiritstudios.hollow.render.block;

import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import org.jspecify.annotations.Nullable;

public class EchoingPotRenderState extends BlockEntityRenderState {
    public float yRot;
    public DecoratedPotBlockEntity.@Nullable WobbleStyle wobbleStyle;
    public float wobbleProgress;
    public Direction direction = Direction.NORTH;

    public BlockModelRenderState model = new BlockModelRenderState();
}
