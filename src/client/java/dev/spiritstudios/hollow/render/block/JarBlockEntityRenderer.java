package dev.spiritstudios.hollow.render.block;

import dev.spiritstudios.hollow.block.entity.JarBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;

public class JarBlockEntityRenderer implements BlockEntityRenderer<JarBlockEntity> {
    private final ItemRenderer itemRenderer;

    public JarBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(JarBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        World world = blockEntity.getWorld();
        DefaultedList<ItemStack> items = blockEntity.getItems();
        if (items.isEmpty() || world == null) return;
        
        matrices.push();
        matrices.translate(0.5F, 0.05F, 0.5F);
        matrices.scale(0.45F, 0.45F, 0.45F);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));

        int index = 0;
        float deg = 0;

        for (ItemStack item : items) {
            matrices.translate(0.0F, 0.0F, -0.0625F);
            long hashCode = MathHelper.hashCode(blockEntity.getPos().getX(), index, blockEntity.getPos().getY());
            float max = 0.05F;

            double x = MathHelper.clamp(((double)((float)(hashCode & 15L) / 15.0F) - 0.5) * 0.5, -max, max);
            double z = MathHelper.clamp(((double)((float)(hashCode >> 8 & 15L) / 15.0F) - 0.5) * 0.5, -max, max);

            deg += (float) (MathHelper.clamp(((double)((float)(hashCode >> 16 & 15L) / 15.0F) - 0.5) * 0.5, -max, max) * 5000);

            matrices.push();

            matrices.translate(x, z, 0);

            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) deg));

            this.itemRenderer.renderItem(item, ModelTransformationMode.FIXED, light, overlay, matrices, vertexConsumers, world, (int) blockEntity.getPos().asLong() + index);

            matrices.pop();

            index++;
        }

        matrices.pop();
    }
}
