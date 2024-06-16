plugins {
    java
    id("fabric-loom") version "1.6.+"
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
    val cabinetApi = property("deps.cabinetapi").toString()
}

val mod = ModInfo()
val deps = Dependencies()

loom {
    runConfigs.create("datagen") {
            server()
            name = "Minecraft Data"
            vmArg("-Dfabric-api.datagen")
            vmArg("-Dfabric-api.datagen.output-dir=${file("src/main/generated")}")
            vmArg("-Dfabric-api.datagen.modid=${mod.id}")

            runDir = "build/datagen"
    }

    accessWidenerPath = file("src/main/resources/hollow.accesswidener")
}

sourceSets.main { resources { srcDir(file("src/main/generated")) } }

repositories {
    maven("https://maven.callmeecho.dev/snapshots/")
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    minecraft("com.mojang:minecraft:${deps.minecraft}")
    mappings("net.fabricmc:yarn:${deps.yarn}:v2")
    modImplementation("net.fabricmc:fabric-loader:${deps.loader}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${deps.fabricApi}")
    include("dev.callmeecho:cabinetapi:${deps.cabinetApi}")
    modImplementation("dev.callmeecho:cabinetapi:${deps.cabinetApi}")

    // not ported
    // modCompileOnly("maven.modrinth:lambdynamiclights:2.3.2+1.20.1")
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

val targetJavaVersion = 21
java {
    withSourcesJar()


    targetJavaVersion
        .let { JavaVersion.values()[it - 1] }
        .let {
            sourceCompatibility = it
            targetCompatibility = it
        }
}


tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release = targetJavaVersion
}

