plugins {
    id("account-shield.common-conventions")
}

dependencies {
    implementation(project(":common"))
    annotationProcessor(libs.velocity)
    compileOnly(libs.velocity)
    compileOnly(libs.redisbungee.velocity)

    compileOnly(libs.nlogin)
    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar")))) //jpremium
}