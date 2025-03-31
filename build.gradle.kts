import com.modrinth.minotaur.ModrinthExtension

plugins {
    java
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.minotaur)
}

class ModInfo {
    val id = property("mod.id").toString()
    val group = property("mod.group").toString()
    val version = property("mod.version").toString()
}

val mod = ModInfo()

version = mod.version
group = mod.group

base.archivesName = "${mod.id}-${mod.version}"


fabricApi {
    configureDataGeneration {
        client = true
    }
}

loom {
    splitEnvironmentSourceSets()
    accessWidenerPath = file("src/main/resources/hollow.accesswidener")

    mods.create(mod.id) {
        sourceSet(sourceSets.getByName("main"))
        sourceSet(sourceSets.getByName("client"))
    }
}

repositories {
    mavenCentral()
    maven("https://maven.spiritstudios.dev/releases/")
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://maven.gegy.dev/")
    maven("https://maven.nucleoid.xyz")
    maven("https://maven.bawnorton.com/releases")

    exclusiveContent {
        forRepository { maven("https://api.modrinth.com/maven/") }
        filter { includeGroup("maven.modrinth") }
    }
}

dependencies {
    minecraft(libs.minecraft)
    mappings(variantOf(libs.yarn) { classifier("v2") })
    modImplementation(libs.fabric.loader)

    modImplementation(libs.fabric.api)

    include(libs.bundles.specter)
    modImplementation(libs.bundles.specter)

    modRuntimeOnly(libs.specter.debug)

    modImplementation(libs.lambdynamiclights)
    modCompileOnly(libs.stapi)
    include("com.github.bawnorton.mixinsquared:mixinsquared-fabric:0.2.0")
    implementation("com.github.bawnorton.mixinsquared:mixinsquared-fabric:0.2.0")
    annotationProcessor("com.github.bawnorton.mixinsquared:mixinsquared-fabric:0.2.0")
}

tasks.processResources {
    val map = mapOf(
        "mod_id" to mod.id,
        "mod_version" to mod.version,
        "fabric_loader_version" to libs.versions.fabric.loader.get(),
        "minecraft_version" to libs.versions.minecraft.get()
    )

    inputs.properties(map)
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

tasks.jar { from("LICENSE") { rename { "${it}_${base.archivesName}" } } }

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set(mod.id)
    versionNumber.set(mod.version)
    uploadFile.set(tasks.remapJar)
    gameVersions.addAll(libs.versions.minecraft.get())
    loaders.addAll("fabric", "quilt")
    syncBodyFrom.set(rootProject.file("README.md").readText())
    dependencies {
        required.version("fabric-api", libs.versions.fabric.api.get())
        optional.version("lambdynamiclights", libs.versions.lambdynamiclights.get())
    }
}
