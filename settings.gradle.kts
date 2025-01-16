rootProject.name = "account-shield"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

listOf(
    "common", "velocity", "spigot", "bungee", "api"
).forEach { project ->
    include(project)
}