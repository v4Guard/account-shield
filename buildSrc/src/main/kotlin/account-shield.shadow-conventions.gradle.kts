plugins {
    id("io.github.goooler.shadow")
    id("account-shield.common-conventions")
}

tasks {
    shadowJar {
        destinationDirectory.set(file("$rootDir/out"))
        archiveFileName.set("v4Guard-account-shield-${project.name}-${project.version}.jar")

        val prefix = "io.v4guard.shield.libs"

        //realocations for shadowJar
        listOf(
            "org.json", "org.checkerframework", "com.fasterxml.jackson", "org.intellij.lang.annotations", "org.jetbrains.annotations"
        ).forEach { relocate(it, "${prefix}.$it") }
    }

    named("assemble") {
        dependsOn("shadowJar")
    }
}