plugins {
    java
    id("fabric-loom") version "1.7-SNAPSHOT"
}

class ModInfo {
    val id = property("mod.id").toString()
    val group = property("mod.group").toString()
    val version = property("mod.version").toString()
}

class Dependencies {
    val minecraft = property("deps.minecraft").toString()
    val loader = property("deps.loader").toString()
    val yarn = property("deps.yarn").toString()

    val fabricApi = property("deps.fabricapi").toString()
    val specter = property("deps.specter").toString()
}

val mod = ModInfo()
val deps = Dependencies()

loom {
    splitEnvironmentSourceSets()
    accessWidenerPath = file("src/main/resources/hollow.accesswidener")

    mods.create(mod.id) {
        sourceSet(sourceSets.getByName("main"))
        sourceSet(sourceSets.getByName("client"))
    }
}

fabricApi {
    configureDataGeneration()
}

repositories {
    maven("https://maven.callmeecho.dev/releases/")
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://api.modrinth.com/maven/")
}

dependencies {
    minecraft("com.mojang:minecraft:${deps.minecraft}")
    mappings("net.fabricmc:yarn:${deps.yarn}:v2")
    modImplementation("net.fabricmc:fabric-loader:${deps.loader}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${deps.fabricApi}")

    fun specterModule(name: String) {
        include("dev.spiritstudios.specter:specter-$name:${deps.specter}")
        modImplementation("dev.spiritstudios.specter:specter-$name:${deps.specter}")
    }

    specterModule("api")
    specterModule("block")
    specterModule("config")
    specterModule("core")
    specterModule("entity")
    specterModule("item")
    specterModule("registry")
    specterModule("render")

    modRuntimeOnly("dev.spiritstudios.specter:specter-debug:${deps.specter}")

    // will be switched back to lambdynamiclights once it's updated
    modCompileOnly("maven.modrinth:ryoamiclights:0.2.9+mc1.21-fabric")
}

tasks.processResources {
    inputs.property("id", mod.id)
    inputs.property("version", mod.version)
    inputs.property("loader_version", deps.loader)
    inputs.property("minecraft_version", deps.minecraft)

    val map = mapOf(
        "id" to mod.id,
        "version" to mod.version,
        "loader_version" to deps.loader,
        "minecraft_version" to deps.minecraft
    )

    filesMatching("fabric.mod.json") { expand(map) }
}

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}


tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release = 21
}

