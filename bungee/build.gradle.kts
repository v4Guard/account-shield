plugins {
    id("account-shield.common-conventions")
    id("account-shield.shadow-conventions")
}

dependencies {
    implementation(project(":common"))
    implementation(libs.jackson.databind)


    compileOnly(libs.v4guard.connector)
    compileOnly(libs.bungeecord)
    compileOnly(libs.redisbungee.bungee)

    compileOnly(libs.nlogin)
    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar")))) //jpremium
}