package dev.callmeecho.hollow.datagen;

import dev.callmeecho.cabinetapi.misc.ReflectionHelper;
import dev.callmeecho.cabinetapi.registry.RegistrarHandler;
import dev.callmeecho.hollow.main.block.HollowLogBlock;
import dev.callmeecho.hollow.main.registry.HollowBlockRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public class ModelProvider extends FabricModelProvider {
    public static final Model HOLLOW_LOG =  new Model(Optional.of(new Identifier("hollow", "block/" + "hollow_log_template")), Optional.empty(), TextureKey.SIDE, TextureKey.INSIDE, TextureKey.END);
    public static final Model HOLLOW_LOG_HORIZONTAL =  new Model(Optional.of(new Identifier("hollow", "block/" + "hollow_log_horizontal_template")), Optional.of("_horizontal"), TextureKey.SIDE, TextureKey.INSIDE, TextureKey.END);
    public static final Model HOLLOW_LOG_HORIZONTAL_MOSSY =  new Model(Optional.of(new Identifier("hollow", "block/" + "hollow_log_horizontal_mossy_template")), Optional.of("_horizontal_mossy"), TextureKey.SIDE, TextureKey.INSIDE, TextureKey.END);
    

    public ModelProvider(FabricDataOutput output) { super(output); }
    
    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        ReflectionHelper.forEachStaticField(HollowBlockRegistry.class, HollowLogBlock.class, (block, name, field) -> {
            TextureMap textureMap = new TextureMap().put(TextureKey.SIDE, new Identifier("minecraft", "block/" + block.sideTexture)).put(TextureKey.INSIDE, new Identifier("minecraft", "block/" + block.insideTexture)).put(TextureKey.END, new Identifier("minecraft", "block/" + block.endTexture));
            Identifier hollowLog = HOLLOW_LOG.upload(block, textureMap, blockStateModelGenerator.modelCollector);
            Identifier hollowLogHorizontal = HOLLOW_LOG_HORIZONTAL.upload(block, textureMap, blockStateModelGenerator.modelCollector);
            Identifier hollowLogHorizontalMossy = HOLLOW_LOG_HORIZONTAL_MOSSY.upload(block, textureMap, blockStateModelGenerator.modelCollector);
            blockStateModelGenerator.blockStateCollector.accept(createAxisRotatedBlockStateWithMossy(block, hollowLog, hollowLogHorizontal, hollowLogHorizontalMossy));
        });
    }

    public static BlockStateSupplier createAxisRotatedBlockStateWithMossy(Block block, Identifier verticalModelId, Identifier horizontalModelId, Identifier horizontalMossyModelId) {
        return VariantsBlockStateSupplier.create(block)
                .coordinate(
                        BlockStateVariantMap.create(Properties.AXIS, HollowLogBlock.MOSSY)
                                .register(Direction.Axis.Y, false, BlockStateVariant.create().put(VariantSettings.MODEL, verticalModelId))
                                .register(Direction.Axis.Z, false, BlockStateVariant.create().put(VariantSettings.MODEL, horizontalModelId).put(VariantSettings.X, VariantSettings.Rotation.R90))
                                .register(
                                        Direction.Axis.X,
                                        false,
                                        BlockStateVariant.create()
                                                .put(VariantSettings.MODEL, horizontalModelId)
                                                .put(VariantSettings.X, VariantSettings.Rotation.R90)
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                                )
                                .register(Direction.Axis.Y, true, BlockStateVariant.create().put(VariantSettings.MODEL, verticalModelId))
                                .register(Direction.Axis.Z, true, BlockStateVariant.create().put(VariantSettings.MODEL, horizontalMossyModelId).put(VariantSettings.X, VariantSettings.Rotation.R90))
                                .register(
                                        Direction.Axis.X,
                                        true,
                                        BlockStateVariant.create()
                                                .put(VariantSettings.MODEL, horizontalMossyModelId)
                                                .put(VariantSettings.X, VariantSettings.Rotation.R90)
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                                )
                );
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }
}
