plugins {
    id("account-shield.common-conventions")
    id("maven-publish")
}


publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}