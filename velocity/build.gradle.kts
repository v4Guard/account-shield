plugins {
    id("account-shield.common-conventions")
    id("account-shield.shadow-conventions")
}

dependencies {
    implementation(project(":common"))
    implementation(libs.jackson.databind)

    annotationProcessor(libs.velocity)

    compileOnly(libs.v4guard.connector)
    compileOnly(libs.velocity)
    compileOnly(libs.redisbungee.velocity)

    compileOnly(libs.nlogin)
    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar")))) //jpremium
}