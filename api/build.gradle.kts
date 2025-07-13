plugins {
    id("account-shield.common-conventions")
    id("maven-publish")
}


dependencies {
    api(libs.annotations)
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}