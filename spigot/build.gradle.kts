plugins {
    id("account-shield.common-conventions")
}

dependencies {
    implementation(project(":common"))
    compileOnly(libs.spigot)
    implementation(libs.jackson.databind)

    compileOnly(libs.authme.api)
    compileOnly(libs.nlogin)
}