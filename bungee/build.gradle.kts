plugins {
    id("account-shield.common-conventions")
}

dependencies {
    implementation(project(":common"))
    compileOnly(libs.bungeecord)
    implementation(libs.annotations)

    compileOnly(libs.nlogin)
    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar")))) //jpremium
}