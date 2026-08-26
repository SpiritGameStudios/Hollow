package dev.spiritstudios.hollow.client.render.block.pot;

import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class FallingPotRenderState extends PotRenderState {
    public DoubleBlockHalf half = DoubleBlockHalf.UPPER;

    public float fallProgress;
    public Direction fallDirection = Direction.NORTH;

    public BlockModelRenderState model = new BlockModelRenderState();
}
