plugins {
    java

    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.modpublish)
}

val modVersion = "2.0.0"
val modId = "hollow"
val modName = "Hollow"

val modrinthProject = "hollow"
val modrinthProjectId = ""
val githubRepository = "SpiritGameStudios/Hollow"

group = "dev.spiritstudios"
base.archivesName = modId

version = "$modVersion+${libs.versions.minecraft.get()}"

val debugArgs = listOf(
    "-enableassertions",

    // Mixin debugging, should make failures happen quicker
    "-Dmixin.debug.verify=true",
    "-Dmixin.debug.strict=true",
    "-Dmixin.debug.countInjections=true",

    // Memory Usage Optimization
    "-XX:+UseZGC",
    "-XX:+UseCompactObjectHeaders",
    "-XX:+UseStringDeduplication",

    "-XX:+AlwaysPreTouch" // Apparently makes startup faster
)


fabricApi {
    configureDataGeneration {
        client = true
    }
}

loom {
    runtimeOnlyLog4j = true

    splitEnvironmentSourceSets()

    accessWidenerPath = file("src/main/resources/${modId}.classtweaker")

    mods {
        create(name) {
            sourceSet("main")
            sourceSet("client")
        }
    }

    runs.configureEach { jvmArguments.addAll(debugArgs) }
}

@Suppress("UnstableApiUsage")
repositories {
    maven("https://maven.spiritstudios.dev/releases/") {
        name = "Spirit Studios Releases"
        content { includeGroupAndSubgroups("dev.spiritstudios") }
    }

    maven("https://maven.spiritstudios.dev/snapshots/") {
        name = "Spirit Studios Snapshots"
        content { includeGroupAndSubgroups("dev.spiritstudios") }
    }

    mavenCentral()
}

dependencies {
    minecraft(libs.minecraft)

    implementation(libs.fabric.loader)
    implementation(libs.fabric.api)
}

tasks.processResources {
    val map = mapOf(
        "version" to modVersion,
        "loader_version" to libs.versions.fabric.loader.get()
    )

    inputs.properties(map)

    filesMatching("fabric.mod.json") { expand(map) }
}

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release = 25
}

tasks.jar {
    inputs.property("archivesName", project.base.archivesName)
    from("LICENSE") { rename { "${it}_${inputs.properties["archivesName"]}" } }
}


publishMods {
    file = tasks.jar.get().archiveFile
    modLoaders.add("fabric")

    version = modVersion
    type = STABLE
    displayName = "$modName $modVersion for Minecraft ${libs.versions.minecraft.get()}"

    changelog = providers.fileContents(layout.projectDirectory.file("CHANGELOG.md")).asText

    modrinth {
        accessToken = providers.gradleProperty("secrets.modrinth_token")
        projectId = modrinthProjectId
        minecraftVersions.add(libs.versions.minecraft.get())

        projectDescription = providers.fileContents(layout.projectDirectory.file("README.md")).asText

        requires("fabric-api")
    }

    github {
        accessToken = providers.gradleProperty("secrets.github_token")
        repository = githubRepository
        commitish = "main"

        tagName = modVersion
    }
}
