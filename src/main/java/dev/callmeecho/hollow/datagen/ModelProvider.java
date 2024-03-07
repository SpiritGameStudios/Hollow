package dev.callmeecho.hollow.datagen;

import dev.callmeecho.cabinetapi.registry.RegistrarHandler;
import dev.callmeecho.hollow.main.block.HollowLogBlock;
import dev.callmeecho.hollow.main.registry.HollowBlockRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.Optional;

import static dev.callmeecho.hollow.main.Hollow.MODID;
import static dev.callmeecho.hollow.main.registry.HollowBlockRegistry.LOTUS_LILYPAD;
import static net.minecraft.data.client.BlockStateModelGenerator.createAxisRotatedBlockState;
import static net.minecraft.data.client.BlockStateModelGenerator.createBlockStateWithRandomHorizontalRotations;

public class ModelProvider extends FabricModelProvider {
    public static final Model HOLLOW_LOG =  new Model(Optional.of(new Identifier("hollow", "block/" + "hollow_log_template")), Optional.empty(), TextureKey.SIDE, TextureKey.INSIDE, TextureKey.END);
    public static final Model HOLLOW_LOG_HORIZONTAL =  new Model(Optional.of(new Identifier("hollow", "block/" + "hollow_log_horizontal_template")), Optional.of("_horizontal"), TextureKey.SIDE, TextureKey.INSIDE, TextureKey.END);


    public ModelProvider(FabricDataOutput output) { super(output); }
    
    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        RegistrarHandler.<HollowLogBlock>forEach(HollowBlockRegistry.class, (block) -> {
            TextureMap textureMap = new TextureMap().put(TextureKey.SIDE, new Identifier("minecraft", "block/" + block.sideTexture)).put(TextureKey.INSIDE, new Identifier("minecraft", "block/" + block.insideTexture)).put(TextureKey.END, new Identifier("minecraft", "block/" + block.endTexture));
            Identifier hollowLog = HOLLOW_LOG.upload(block, textureMap, blockStateModelGenerator.modelCollector);
            Identifier hollowLogHorizontal = HOLLOW_LOG_HORIZONTAL.upload(block, textureMap, blockStateModelGenerator.modelCollector);
            blockStateModelGenerator.blockStateCollector.accept(createAxisRotatedBlockState(block, hollowLog, hollowLogHorizontal));
        });
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }
}
