plugins {
    id("account-shield.common-conventions")
    id("maven-publish")
}


dependencies {
    api(libs.annotations)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}