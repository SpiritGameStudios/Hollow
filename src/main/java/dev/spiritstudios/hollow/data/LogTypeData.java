package dev.spiritstudios.hollow.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.function.Function;

public record LogTypeData(Identifier id, Identifier sideTexture, Identifier insideTexture, Identifier endTexture) {
    public static final Codec<LogTypeData> CODEC = Codec.either(
            RecordCodecBuilder.<LogTypeData>create(instance -> instance.group(
                    Identifier.CODEC.fieldOf("id").forGetter(LogTypeData::id),
                    Identifier.CODEC.fieldOf("side_texture").forGetter(LogTypeData::sideTexture),
                    Identifier.CODEC.fieldOf("inside_texture").forGetter(LogTypeData::insideTexture),
                    Identifier.CODEC.fieldOf("end_texture").forGetter(LogTypeData::endTexture)
            ).apply(instance, LogTypeData::new)),
            Identifier.CODEC
    ).xmap(either -> either.map(Function.identity(), LogTypeData::byId), Either::left);

    public static LogTypeData byId(Identifier id) {
        return new LogTypeData(
                id,
                id.withPrefix("block/"),
                id.withPrefix("block/stripped_"),
                id.withPrefix("block/").withSuffix("_top")
        );
    }

    public static LogTypeData byIdStripped(Identifier id) {
        return new LogTypeData(
                id,
                id.withPrefix("block/"),
                id.withPrefix("block/"),
                id.withPrefix("block/").withSuffix("_top")
        );
    }
}
