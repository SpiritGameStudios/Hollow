package dev.spiritstudios.hollow.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;

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
                id.withPrefixedPath("block/"),
                id.withPrefixedPath("block/stripped_"),
                id.withPrefixedPath("block/").withSuffixedPath("_top")
        );
    }

    public static LogTypeData byIdStripped(Identifier id) {
        return new LogTypeData(
                id,
                id.withPrefixedPath("block/"),
                id.withPrefixedPath("block/"),
                id.withPrefixedPath("block/").withSuffixedPath("_top")
        );
    }
}
