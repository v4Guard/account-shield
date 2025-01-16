plugins {
    id("account-shield.common-conventions")
    id("account-shield.shadow-conventions")
}

dependencies {
    implementation(project(":common"))
    compileOnly(libs.spigot)
    implementation(libs.jackson.databind)

    compileOnly(libs.authme.api)
    compileOnly(libs.nlogin)
}