package dev.spiritstudios.hollow.api;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.block.HollowLogBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateSupplier;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.TexturedModel;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.data.client.VariantsBlockStateSupplier;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

import static dev.spiritstudios.hollow.Hollow.MODID;

public final class HollowModelGeneration {
    public static final TexturedModel.Factory HOLLOW_LOG = TexturedModel.makeFactory(
            b -> {
                if (!(b instanceof HollowLogBlock block))
                    throw new IllegalArgumentException();

                return new TextureMap()
                        .put(TextureKey.SIDE, block.typeData.sideTexture())
                        .put(TextureKey.INSIDE, block.typeData.insideTexture())
                        .put(TextureKey.END, block.typeData.endTexture());
            },
            new Model(
                    Optional.of(Identifier.of(MODID, "block/hollow_log_template")),
                    Optional.empty(),
                    TextureKey.SIDE, TextureKey.INSIDE, TextureKey.END
            )
    );

    public static final TexturedModel.Factory HOLLOW_LOG_HORIZONTAL = TexturedModel.makeFactory(
            b -> {
                if (!(b instanceof HollowLogBlock block))
                    throw new IllegalArgumentException();

                return new TextureMap()
                        .put(TextureKey.SIDE, block.typeData.sideTexture())
                        .put(TextureKey.INSIDE, block.typeData.insideTexture())
                        .put(TextureKey.END, block.typeData.endTexture());
            },
            new Model(
                    Optional.of(Identifier.of(MODID, "block/hollow_log_horizontal_template")),
                    Optional.of("_horizontal"),
                    TextureKey.SIDE, TextureKey.INSIDE, TextureKey.END
            )
    );

    public static final TexturedModel.Factory HOLLOW_LOG_HORIZONTAL_MOSS = TexturedModel.makeFactory(
            b -> {
                if (!(b instanceof HollowLogBlock block))
                    throw new IllegalArgumentException();

                return new TextureMap()
                        .put(TextureKey.SIDE, block.typeData.sideTexture())
                        .put(TextureKey.INSIDE, block.typeData.insideTexture())
                        .put(TextureKey.END, block.typeData.endTexture())
                        .put(TextureKey.LAYER0, Hollow.id("block/moss_overlay"));
            },
            new Model(
                    Optional.of(Hollow.id("block/hollow_log_horizontal_layer_template")),
                    Optional.of("_horizontal_moss"),
                    TextureKey.SIDE, TextureKey.INSIDE, TextureKey.END, TextureKey.LAYER0
            )
    );

    public static final TexturedModel.Factory HOLLOW_LOG_HORIZONTAL_PALE_MOSS = TexturedModel.makeFactory(
            b -> {
                if (!(b instanceof HollowLogBlock block))
                    throw new IllegalArgumentException();

                return new TextureMap()
                        .put(TextureKey.SIDE, block.typeData.sideTexture())
                        .put(TextureKey.INSIDE, block.typeData.insideTexture())
                        .put(TextureKey.END, block.typeData.endTexture())
                        .put(TextureKey.LAYER0, Hollow.id("block/pale_moss_overlay"));
            },
            new Model(
                    Optional.of(Hollow.id("block/hollow_log_horizontal_layer_template")),
                    Optional.of("_horizontal_pale_moss"),
                    TextureKey.SIDE, TextureKey.INSIDE, TextureKey.END, TextureKey.LAYER0
            )
    );

    public static final TexturedModel.Factory HOLLOW_LOG_HORIZONTAL_SNOW = TexturedModel.makeFactory(
            b -> {
                if (!(b instanceof HollowLogBlock block))
                    throw new IllegalArgumentException();

                return new TextureMap()
                        .put(TextureKey.SIDE, block.typeData.sideTexture())
                        .put(TextureKey.INSIDE, block.typeData.insideTexture())
                        .put(TextureKey.END, block.typeData.endTexture())
                        .put(TextureKey.LAYER0, Hollow.id("block/snow_overlay"));
            },
            new Model(
                    Optional.of(Hollow.id("block/hollow_log_horizontal_layer_template")),
                    Optional.of("_horizontal_snow"),
                    TextureKey.SIDE, TextureKey.INSIDE, TextureKey.END, TextureKey.LAYER0
            )
    );

    public static void registerHollowLog(BlockStateModelGenerator generator, HollowLogBlock block) {
        Identifier hollowLog = HOLLOW_LOG.upload(block, generator.modelCollector);
        Identifier hollowLogHorizontal = HOLLOW_LOG_HORIZONTAL.upload(block, generator.modelCollector);
        Identifier hollowLogHorizontalMoss = HOLLOW_LOG_HORIZONTAL_MOSS.upload(block, generator.modelCollector);
        Identifier hollowLogHorizontalSnow = HOLLOW_LOG_HORIZONTAL_SNOW.upload(block, generator.modelCollector);

        generator.blockStateCollector.accept(createAxisRotatedBlockStateWithLayer(block, hollowLog, hollowLogHorizontal, hollowLogHorizontalMoss, hollowLogHorizontalSnow));
    }

    public static BlockStateSupplier createAxisRotatedBlockStateWithLayer(Block block, Identifier verticalModelId, Identifier horizontalModelId, Identifier horizontalMossModelId, Identifier horizontalSnowModelId) {
        return VariantsBlockStateSupplier.create(block)
                .coordinate(
                        BlockStateVariantMap.create(Properties.AXIS, HollowLogBlock.LAYER)
                                .register(Direction.Axis.Y, HollowLogBlock.Layer.NONE, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, verticalModelId))
                                .register(Direction.Axis.Z, HollowLogBlock.Layer.NONE, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, horizontalModelId))
                                .register(
                                        Direction.Axis.X,
                                        HollowLogBlock.Layer.NONE,
                                        BlockStateVariant.create()
                                                .put(VariantSettings.MODEL, horizontalModelId)
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                                )
                                .register(Direction.Axis.Y, HollowLogBlock.Layer.MOSS, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, verticalModelId))
                                .register(Direction.Axis.Z, HollowLogBlock.Layer.MOSS, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, horizontalMossModelId))
                                .register(
                                        Direction.Axis.X,
                                        HollowLogBlock.Layer.MOSS,
                                        BlockStateVariant.create()
                                                .put(VariantSettings.MODEL, horizontalMossModelId)
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                                )
                                .register(Direction.Axis.Y, HollowLogBlock.Layer.SNOW, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, verticalModelId))
                                .register(Direction.Axis.Z, HollowLogBlock.Layer.SNOW, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, horizontalSnowModelId))
                                .register(
                                        Direction.Axis.X,
                                        HollowLogBlock.Layer.SNOW,
                                        BlockStateVariant.create()
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                                                .put(VariantSettings.MODEL, horizontalSnowModelId)
                                )
                );
    }
}
